package com.my.pocketguard.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class FundModel(
    val id: String? = null,
    @get:PropertyName("fund_name") @set:PropertyName("fund_name")
    var fundName: String? = null,
    @get:PropertyName("fund_amount") @set:PropertyName("fund_amount")
    var fundAmount: Int? = null
)
