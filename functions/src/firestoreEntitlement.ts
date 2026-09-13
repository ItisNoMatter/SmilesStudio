import type { Firestore, Transaction } from "firebase-admin/firestore";
import { decideEntitlement, EntitlementDecision, UserState } from "./entitlementLogic";

export function currentMonthKey(date: Date = new Date()): string {
  const year = date.getUTCFullYear();
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  return `${year}-${month}`;
}

export async function consumeEntitlement(db: Firestore, uid: string): Promise<EntitlementDecision> {
  const userRef = db.collection("users").doc(uid);
  const month = currentMonthKey();

  return db.runTransaction(async (tx: Transaction) => {
    const snapshot = await tx.get(userRef);
    const state = snapshot.exists ? (snapshot.data() as UserState) : null;
    const decision = decideEntitlement(state, month);
    if (decision.allowed) {
      tx.set(userRef, decision.nextState, { merge: true });
    }
    return decision;
  });
}
