package com.smilestudio.android

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.smilestudio.android.subscription.SubscriptionStatus

/**
 * Signs in anonymously at launch and configures RevenueCat with the resulting UID (AnyDR 0130,
 * 0132), so every user -- BYOK, free tier, or subscribed -- is tracked from the start.
 */
class SmilesStudioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.auth.signInAnonymously().addOnSuccessListener { authResult ->
            val uid = authResult.user?.uid ?: return@addOnSuccessListener
            configureRevenueCat(uid)
        }
    }

    private fun configureRevenueCat(uid: String) {
        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(
            PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_API_KEY)
                .appUserID(uid)
                .build(),
        )
        Purchases.sharedInstance.updatedCustomerInfoListener =
            UpdatedCustomerInfoListener { customerInfo -> SubscriptionStatus.update(customerInfo) }
    }
}
