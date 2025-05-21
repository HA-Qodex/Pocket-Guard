package com.my.pocketguard.services

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.ExpenseModel
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.util.AppUtils.getCurrentMonthStartAndEnd
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val store: FirebaseFirestore,
    auth: FirebaseAuth
) {
    val userId = auth.currentUser?.uid ?: ""
    val userRef = store.collection("users").document(userId)

    fun fetchCategory(): Flow<List<CategoryModel>> = callbackFlow {
        val listener = store.collection("categories").whereEqualTo("created_by", userRef)
            .orderBy("category_name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                Log.d("FIRESTORE", "fetchCategory: $error")
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(CategoryModel::class.java) ?: emptyList())
            }
        awaitClose {
            listener.remove()
        }
    }

    fun storeCategory(
        categoryData: HashMap<String, Any>,
        result: (UIState) -> Unit
    ) {
        val uid = UUID.randomUUID().toString()

        store.collection("categories").whereEqualTo("created_by", userRef)
            .whereEqualTo("category_name", categoryData["category_name"]).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    val userRef = store.document("users/${userId}")
                    categoryData["id"] = uid
                    categoryData["created_by"] = userRef
                    categoryData["created_at"] = FieldValue.serverTimestamp()
                    val ref = store.collection("categories").document(uid)
                    ref.set(categoryData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            result(UIState.Success("CATEGORY"))
                        } else {
                            result(UIState.Error("Failed to add category"))
                        }
                    }
                } else {
                    result(UIState.Error("Category already exists"))
                }
            }
    }

    fun updateCategory(
        categoryData: HashMap<String, Any>,
        result: (UIState) -> Unit
    ) {
        store.collection("categories").document(categoryData["id"].toString()).update(categoryData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result(UIState.Success("CATEGORY"))
                } else {
                    result(UIState.Error("Failed to update category"))
                }
            }
    }

    fun fetchFunds(): Flow<List<FundModel>> = callbackFlow {
        val listener = store.collection("funds").whereEqualTo("created_by", userRef)
            .orderBy("fund_name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                Log.d("FIRESTORE", "fetchFunds: $error")
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(FundModel::class.java) ?: emptyList())
            }
        awaitClose {
            listener.remove()
        }
    }

    fun storeFund(
        fundData: HashMap<String, Any>,
        result: (UIState) -> Unit
    ) {
        val uid = UUID.randomUUID().toString()

        store.collection("funds").whereEqualTo("created_by", userRef)
            .whereEqualTo("fund_name", fundData["fund_name"]).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    val userRef = store.document("users/${userId}")
                    fundData["id"] = uid
                    fundData["created_by"] = userRef
                    fundData["created_at"] = FieldValue.serverTimestamp()
                    store.collection("funds").document(uid).set(fundData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                result(UIState.Success("FUND"))
                            } else {
                                result(UIState.Error("Failed to add fund"))
                            }
                        }
                } else {
                    result(UIState.Error("Fund already exists"))
                }
            }
    }

    fun updateFund(
        fundData: HashMap<String, Any>,
        result: (UIState) -> Unit
    ) {
        store.collection("funds").document(fundData["id"].toString()).update(fundData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result(UIState.Success("FUND"))
                } else {
                    result(UIState.Error("Failed to update fund"))
                }
            }
    }

    fun fetchExpense(): Flow<List<Expense>> = callbackFlow {
        val listenerRegistration = store.collection("expenses")
            .whereEqualTo("created_by", userRef)
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val expenseModels = snapshot.toObjects(ExpenseModel::class.java)

                        // Collect all unique category and fund document refs
                        val categoryRefs = expenseModels.mapNotNull { it.categoryRef }.toSet()
                        val fundRefs = expenseModels.mapNotNull { it.fundRef }.toSet()

                        // Batch-fetch all category and fund docs in parallel
                        val categoryDeferred = async {
                            categoryRefs.associateWith {
                                it.get().await().toObject(CategoryModel::class.java)
                                    ?: CategoryModel()
                            }
                        }
                        val fundDeferred = async {
                            fundRefs.associateWith {
                                it.get().await().toObject(FundModel::class.java) ?: FundModel()
                            }
                        }

                        val categoryMap = categoryDeferred.await()
                        val fundMap = fundDeferred.await()

                        val resolvedExpenses = expenseModels.map { expense ->
                            Expense(
                                id = expense.id,
                                category = categoryMap[expense.categoryRef],
                                fund = fundMap[expense.fundRef],
                                date = expense.date,
                                amount = expense.amount,
                                title = expense.title
                            )
                        }.toList()

                        trySend(resolvedExpenses).isSuccess
                    }
                } else {
                    trySend(emptyList()).isSuccess
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    fun fetchExpenseAnalytics(): Flow<List<Expense>> = callbackFlow {
        Log.d("ANALYTICS", "${getCurrentMonthStartAndEnd().first}  ${getCurrentMonthStartAndEnd().second}")
        val listenerRegistration = store.collection("expenses")
            .whereEqualTo("created_by", userRef)
            .whereGreaterThanOrEqualTo("date", Timestamp(getCurrentMonthStartAndEnd().first / 1000, 0))
            .whereLessThan("date", Timestamp(getCurrentMonthStartAndEnd().second / 1000, 0))
            .orderBy("created_at", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val expenseModels = snapshot.toObjects(ExpenseModel::class.java)

                        // Collect all unique category and fund document refs
                        val categoryRefs = expenseModels.mapNotNull { it.categoryRef }.toSet()
                        val fundRefs = expenseModels.mapNotNull { it.fundRef }.toSet()

                        // Batch-fetch all category and fund docs in parallel
                        val categoryDeferred = async {
                            categoryRefs.associateWith {
                                it.get().await().toObject(CategoryModel::class.java)
                                    ?: CategoryModel()
                            }
                        }
                        val fundDeferred = async {
                            fundRefs.associateWith {
                                it.get().await().toObject(FundModel::class.java) ?: FundModel()
                            }
                        }

                        val categoryMap = categoryDeferred.await()
                        val fundMap = fundDeferred.await()

                        val resolvedExpenses = expenseModels.map { expense ->
                            Expense(
                                id = expense.id,
                                category = categoryMap[expense.categoryRef],
                                fund = fundMap[expense.fundRef],
                                date = expense.date,
                                amount = expense.amount,
                                title = expense.title
                            )
                        }.toList()

                        trySend(resolvedExpenses).isSuccess
                    }
                } else {
                    trySend(emptyList()).isSuccess
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    fun storeExpense(
        expenseList: List<Expense>,
        result: (UIState) -> Unit
    ) {
        try {
            data class ExpenseWriteData(
                val ref: DocumentReference,
                val data: Map<String, Any?>
            )

            data class FundUpdateData(
                val ref: DocumentReference,
                val newRemainingAmount: Long
            )

            val expenseWriteList = mutableListOf<ExpenseWriteData>()
            val fundUpdateList = mutableListOf<FundUpdateData>()

            store.runTransaction { transaction ->

                expenseList.forEach { expense ->
                    val expenseId = expense.id ?: throw Exception("Missing expense ID")
                    val categoryId = expense.category?.id ?: throw Exception("Missing category ID")
                    val fundId = expense.fund?.id ?: throw Exception("Missing fund ID")
                    val amount = (expense.amount as? Number)?.toLong()
                        ?: throw Exception("Invalid amount for expense: $expenseId")

                    val expenseRef = store.collection("expenses").document(expenseId)
                    val categoryRef = store.collection("categories").document(categoryId)
                    val fundRef = store.collection("funds").document(fundId)

                    val remainingAmount = (transaction.get(fundRef).get("remaining_amount") as? Number)?.toLong()
                        ?: throw Exception("Missing or invalid remaining amount for fund: $fundId")

                    if (amount > remainingAmount) {
                        throw FirebaseFirestoreException(
                            "Insufficient fund for expense '${expense.title ?: ""}'",
                            FirebaseFirestoreException.Code.ABORTED
                        ) as Throwable
                    }

                    val expenseData = mapOf(
                        "id" to expenseId,
                        "date" to expense.date,
                        "amount" to amount,
                        "title" to (expense.title?.trim() ?: ""),
                        "created_at" to FieldValue.serverTimestamp(),
                        "created_by" to userRef,
                        "category_ref" to categoryRef,
                        "fund_ref" to fundRef
                    )

                    expenseWriteList.add(ExpenseWriteData(expenseRef, expenseData))
                    fundUpdateList.add(FundUpdateData(fundRef, remainingAmount - amount))
                }

                expenseWriteList.forEach { transaction.set(it.ref, it.data) }
                fundUpdateList.forEach { transaction.update(it.ref, "remaining_amount", it.newRemainingAmount) }

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result(UIState.Success("EXPENSE"))
                    Log.d("EXPENSE", "Successfully added expense")
                } else {
                    result(UIState.Error("Failed to add expense. ${task.exception?.message}"))
                    Log.e("EXPENSE", "Failed to add expense. ${task.exception?.message}")
                }
            }
        } catch (e: Exception) {
            result(UIState.Error("Expense error. " + e.message.toString()))
            Log.e("EXPENSE", "Expense error ", e)
        }
    }
}