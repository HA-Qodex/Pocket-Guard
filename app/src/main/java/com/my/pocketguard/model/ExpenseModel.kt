package com.my.pocketguard.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class ExpenseModel(
    val id: String? = null,
    @get:PropertyName("category_ref")
    @set:PropertyName("category_ref")
    var categoryRef: DocumentReference? = null,
    @get:PropertyName("fund_ref")
    @set:PropertyName("fund_ref")
    var fundRef: DocumentReference? = null,
    val date: Timestamp? = null,
    val amount: Long? = null,
    val title: String? = null
)

data class Expense(
    val id: String? = null,
    var category: CategoryModel?,
    var fund: FundModel?,
    val date: Timestamp? = null,
    val amount: Long? = null,
    val title: String? = null
)
