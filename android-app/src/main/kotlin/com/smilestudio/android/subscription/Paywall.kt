package com.smilestudio.android.subscription

import androidx.compose.runtime.Composable
import com.revenuecat.purchases.ui.revenuecatui.ExperimentalPreviewRevenueCatUIPurchasesAPI
import com.revenuecat.purchases.ui.revenuecatui.Paywall
import com.revenuecat.purchases.ui.revenuecatui.PaywallOptions

/**
 * RevenueCat's official Paywalls UI (AnyDR 0118), wrapped for reuse. The caller decides how to
 * present this (e.g. inside a full-screen Dialog) -- wiring it into the actual entry point is
 * Issue #36's scope.
 */
@OptIn(ExperimentalPreviewRevenueCatUIPurchasesAPI::class)
@Composable
fun SubscriptionPaywall(onDismiss: () -> Unit) {
    Paywall(options = PaywallOptions.Builder(dismissRequest = onDismiss).build())
}
