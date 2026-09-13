import { onCall, HttpsError } from "firebase-functions/v2/https";
import { getFirestore } from "firebase-admin/firestore";
import { consumeEntitlement } from "./firestoreEntitlement";
import { recognizeStructureWithGemini } from "./geminiClient";

interface RecognizeImageRequest {
  imageBase64: string;
  mimeType: string;
}

interface RecognizeImageResponse {
  smiles: string;
  remainingFreeCount: number | null;
}

export const recognizeImage = onCall<RecognizeImageRequest, Promise<RecognizeImageResponse>>(
  { secrets: ["GEMINI_API_KEY"] },
  async (request) => {
    if (!request.auth) {
      throw new HttpsError("unauthenticated", "サインインが必要です。");
    }

    const { imageBase64, mimeType } = request.data;
    if (!imageBase64 || !mimeType) {
      throw new HttpsError("invalid-argument", "画像データが不正です。");
    }

    const decision = await consumeEntitlement(getFirestore(), request.auth.uid);
    if (!decision.allowed) {
      throw new HttpsError("resource-exhausted", "今月の無料枠を使い切りました。");
    }

    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      throw new HttpsError("internal", "サーバー設定エラーです。");
    }

    const result = await recognizeStructureWithGemini(apiKey, imageBase64, mimeType);
    if (!result.smiles) {
      throw new HttpsError("internal", "画像から構造式を認識できませんでした。");
    }

    return { smiles: result.smiles, remainingFreeCount: decision.remainingFreeCount };
  },
);
