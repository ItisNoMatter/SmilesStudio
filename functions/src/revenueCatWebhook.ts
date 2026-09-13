import { onRequest } from "firebase-functions/v2/https";
import { getFirestore } from "firebase-admin/firestore";
import * as logger from "firebase-functions/logger";

const SUBSCRIBED_EVENT_TYPES = new Set(["INITIAL_PURCHASE", "RENEWAL", "PRODUCT_CHANGE", "UNCANCELLATION"]);
const UNSUBSCRIBED_EVENT_TYPES = new Set(["CANCELLATION", "EXPIRATION", "BILLING_ISSUE"]);

export const revenueCatWebhook = onRequest(
  { secrets: ["REVENUECAT_WEBHOOK_SECRET"] },
  async (req, res) => {
    const expectedAuth = `Bearer ${process.env.REVENUECAT_WEBHOOK_SECRET}`;
    if (req.header("Authorization") !== expectedAuth) {
      res.status(401).send("Unauthorized");
      return;
    }

    const event = req.body?.event;
    const uid: string | undefined = event?.app_user_id;
    const type: string | undefined = event?.type;

    if (!uid || !type) {
      res.status(400).send("Invalid payload");
      return;
    }

    let isSubscribed: boolean | undefined;
    if (SUBSCRIBED_EVENT_TYPES.has(type)) {
      isSubscribed = true;
    } else if (UNSUBSCRIBED_EVENT_TYPES.has(type)) {
      isSubscribed = false;
    }

    if (isSubscribed !== undefined) {
      await getFirestore().collection("users").doc(uid).set({ isSubscribed }, { merge: true });
    } else {
      logger.info(`Ignoring unhandled RevenueCat event type: ${type}`);
    }

    res.status(200).send("OK");
  },
);
