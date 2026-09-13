export interface UserState {
  isSubscribed: boolean;
  freeTierCount: number;
  freeTierMonth: string;
}

export type EntitlementDecision =
  | { allowed: true; nextState: UserState }
  | { allowed: false; reason: "free_tier_exhausted" };

const FREE_TIER_MONTHLY_LIMIT = 5;

export function decideEntitlement(
  state: UserState | null,
  currentMonth: string,
): EntitlementDecision {
  if (state?.isSubscribed) {
    return { allowed: true, nextState: state };
  }

  const isNewMonth = state === null || state.freeTierMonth !== currentMonth;
  const currentCount = isNewMonth ? 0 : state.freeTierCount;

  if (currentCount >= FREE_TIER_MONTHLY_LIMIT) {
    return { allowed: false, reason: "free_tier_exhausted" };
  }

  return {
    allowed: true,
    nextState: {
      isSubscribed: state?.isSubscribed ?? false,
      freeTierCount: currentCount + 1,
      freeTierMonth: currentMonth,
    },
  };
}
