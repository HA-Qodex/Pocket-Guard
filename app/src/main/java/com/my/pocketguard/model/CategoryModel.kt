package com.my.pocketguard.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class CategoryModel(
    val id: String? = null,
    @get:PropertyName("category_name") @set:PropertyName("category_name")
    var categoryName: String? = null,
    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Timestamp? = null
)