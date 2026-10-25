package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

data class PaymentInfo(
    val monCash: String = "MonCash: +509 38444413",
    val natCash: String = "NatCash: +509 42444114",
    val binance: String = "Binance ID: 1193247367"
)

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("football_prono_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PIN = "admin_pin"
        private const val KEY_MONCASH = "payment_moncash"
        private const val KEY_NATCASH = "payment_natcash"
        private const val KEY_BINANCE = "payment_binance"
        private const val KEY_IS_VIP = "is_vip_unlocked"
        private const val DEFAULT_PIN = "2468"
    }

    fun getAdminPin(): String = prefs.getString(KEY_PIN, DEFAULT_PIN) ?: DEFAULT_PIN

    fun setAdminPin(pin: String) {
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    fun getPaymentInfo(): PaymentInfo {
        return PaymentInfo(
            monCash = prefs.getString(KEY_MONCASH, "MonCash: +509 38444413") ?: "MonCash: +509 38444413",
            natCash = prefs.getString(KEY_NATCASH, "NatCash: +509 42444114") ?: "NatCash: +509 42444114",
            binance = prefs.getString(KEY_BINANCE, "Binance ID: 1193247367") ?: "Binance ID: 1193247367"
        )
    }

    fun savePaymentInfo(info: PaymentInfo) {
        prefs.edit()
            .putString(KEY_MONCASH, info.monCash)
            .putString(KEY_NATCASH, info.natCash)
            .putString(KEY_BINANCE, info.binance)
            .apply()
    }

    fun isVipUnlocked(): Boolean = prefs.getBoolean(KEY_IS_VIP, false)

    fun setVipUnlocked(unlocked: Boolean) {
        prefs.edit().putBoolean(KEY_IS_VIP, unlocked).apply()
    }
}
