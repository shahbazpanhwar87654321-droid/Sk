package com.example.billing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PlanType(val title: String, val price: String) {
    FREE("Free Creator", "$0/mo"),
    PREMIUM_MONTHLY("Pro Creator Monthly", "$4.99/mo"),
    PREMIUM_YEARLY("Pro Creator Yearly", "$39.99/yr")
}

data class SubscriptionState(
    val isPremium: Boolean = false,
    val planType: PlanType = PlanType.FREE,
    val remainingFreeGenerations: Int = 10,
    val canExport4K: Boolean = false,
    val canUsePremiumTemplates: Boolean = false
)

interface SubscriptionManager {
    val state: StateFlow<SubscriptionState>
    fun upgradeToPremium(plan: PlanType)
    fun downgradeToFree()
    fun consumeFreeGeneration(): Boolean
}

class DefaultSubscriptionManager : SubscriptionManager {
    private val _state = MutableStateFlow(
        SubscriptionState(
            isPremium = false,
            planType = PlanType.FREE,
            remainingFreeGenerations = 10,
            canExport4K = false,
            canUsePremiumTemplates = false
        )
    )
    override val state: StateFlow<SubscriptionState> = _state.asStateFlow()

    override fun upgradeToPremium(plan: PlanType) {
        _state.value = _state.value.copy(
            isPremium = true,
            planType = plan,
            canExport4K = true,
            canUsePremiumTemplates = true
        )
    }

    override fun downgradeToFree() {
        _state.value = SubscriptionState(
            isPremium = false,
            planType = PlanType.FREE,
            remainingFreeGenerations = 10,
            canExport4K = false,
            canUsePremiumTemplates = false
        )
    }

    override fun consumeFreeGeneration(): Boolean {
        if (_state.value.isPremium) return true
        val current = _state.value.remainingFreeGenerations
        if (current > 0) {
            _state.value = _state.value.copy(remainingFreeGenerations = current - 1)
            return true
        }
        return false
    }
}
