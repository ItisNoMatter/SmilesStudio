import { GoogleGenAI, createPartFromBase64, createUserContent } from "@google/genai";

const MODEL = "gemini-3.5-flash";
const RECOGNITION_INSTRUCTION =
  "Identify the chemical structure drawn in this image and respond with ONLY its SMILES string, nothing else.";

export interface GeminiRecognitionResult {
  smiles: string | null;
}

export async function recognizeStructureWithGemini(
  apiKey: string,
  imageBase64: string,
  mimeType: string,
): Promise<GeminiRecognitionResult> {
  const ai = new GoogleGenAI({ apiKey });
  const response = await ai.models.generateContent({
    model: MODEL,
    contents: createUserContent([
      RECOGNITION_INSTRUCTION,
      createPartFromBase64(imageBase64, mimeType),
    ]),
  });
  const text = response.text?.trim();
  return { smiles: text && text.length > 0 ? text : null };
}
