package com.smilestudio.android.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.revenuecat.purchases.CustomerInfo

private const val PRO_ENTITLEMENT_ID = "pro"

/**
 * Holds the current RevenueCat subscription state as Compose state, kept up to date by
 * [SmilesStudioApplication]'s CustomerInfo listener (AnyDR 0131).
 */
object SubscriptionStatus {
    var isSubscribed: Boolean by mutableStateOf(false)
        private set

    fun update(customerInfo: CustomerInfo) {
        isSubscribed = customerInfo.entitlements[PRO_ENTITLEMENT_ID]?.isActive == true
    }
}
