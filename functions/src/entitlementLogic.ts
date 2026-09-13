export interface UserState {
  isSubscribed: boolean;
  freeTierCount: number;
  freeTierMonth: string;
}

export type EntitlementDecision =
  | { allowed: true; nextState: UserState; remainingFreeCount: number | null }
  | { allowed: false; reason: "free_tier_exhausted" };

const FREE_TIER_MONTHLY_LIMIT = 5;

export function decideEntitlement(
  state: UserState | null,
  currentMonth: string,
): EntitlementDecision {
  if (state?.isSubscribed) {
    return { allowed: true, nextState: state, remainingFreeCount: null };
  }

  const isNewMonth = state === null || state.freeTierMonth !== currentMonth;
  const currentCount = isNewMonth ? 0 : state.freeTierCount;

  if (currentCount >= FREE_TIER_MONTHLY_LIMIT) {
    return { allowed: false, reason: "free_tier_exhausted" };
  }

  const nextCount = currentCount + 1;
  return {
    allowed: true,
    nextState: {
      isSubscribed: state?.isSubscribed ?? false,
      freeTierCount: nextCount,
      freeTierMonth: currentMonth,
    },
    remainingFreeCount: FREE_TIER_MONTHLY_LIMIT - nextCount,
  };
}
