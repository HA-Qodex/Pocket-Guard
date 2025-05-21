package com.my.pocketguard.model

import com.google.firebase.firestore.PropertyName

data class FundModel(
    val id: String? = null,
    @get:PropertyName("fund_name") @set:PropertyName("fund_name")
    var fundName: String? = null,
    @get:PropertyName("fund_amount") @set:PropertyName("fund_amount")
    var fundAmount: Long? = null,
    @get:PropertyName("remaining_amount") @set:PropertyName("remaining_amount")
    var remainingAmount: Long? = null
)
