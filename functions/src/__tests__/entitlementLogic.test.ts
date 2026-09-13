import { decideEntitlement, UserState } from "../entitlementLogic";

const CURRENT_MONTH = "2026-09";
const OTHER_MONTH = "2026-08";

describe("decideEntitlement", () => {
  test("allows and does not consume free tier when subscribed", () => {
    const state: UserState = {
      isSubscribed: true,
      freeTierCount: 5,
      freeTierMonth: CURRENT_MONTH,
    };

    const decision = decideEntitlement(state, CURRENT_MONTH);

    expect(decision).toEqual({ allowed: true, nextState: state });
  });

  test("allows and increments free tier count when under the limit", () => {
    const state: UserState = {
      isSubscribed: false,
      freeTierCount: 2,
      freeTierMonth: CURRENT_MONTH,
    };

    const decision = decideEntitlement(state, CURRENT_MONTH);

    expect(decision).toEqual({
      allowed: true,
      nextState: { isSubscribed: false, freeTierCount: 3, freeTierMonth: CURRENT_MONTH },
    });
  });

  test("denies with free_tier_exhausted when the monthly limit is reached", () => {
    const state: UserState = {
      isSubscribed: false,
      freeTierCount: 5,
      freeTierMonth: CURRENT_MONTH,
    };

    const decision = decideEntitlement(state, CURRENT_MONTH);

    expect(decision).toEqual({ allowed: false, reason: "free_tier_exhausted" });
  });

  test("resets the counter when the stored month differs from the current month", () => {
    const state: UserState = {
      isSubscribed: false,
      freeTierCount: 5,
      freeTierMonth: OTHER_MONTH,
    };

    const decision = decideEntitlement(state, CURRENT_MONTH);

    expect(decision).toEqual({
      allowed: true,
      nextState: { isSubscribed: false, freeTierCount: 1, freeTierMonth: CURRENT_MONTH },
    });
  });

  test("allows a brand new user (no stored state) and starts the counter at 1", () => {
    const decision = decideEntitlement(null, CURRENT_MONTH);

    expect(decision).toEqual({
      allowed: true,
      nextState: { isSubscribed: false, freeTierCount: 1, freeTierMonth: CURRENT_MONTH },
    });
  });
});
