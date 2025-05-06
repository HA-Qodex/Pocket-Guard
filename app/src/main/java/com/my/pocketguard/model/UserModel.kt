package com.my.pocketguard.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class UserModel(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @get:PropertyName("created_at") @set:PropertyName("created_at")
    var createdAt: Timestamp? = null
)
