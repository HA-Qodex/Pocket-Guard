package com.my.pocketguard.repository

import android.app.Activity
import android.util.Log
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.my.pocketguard.R
import com.my.pocketguard.navigation.AppRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore
) {

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _user

    fun authNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun getCurrentUser() {
        _user.value = firebaseAuth.currentUser
    }

    suspend fun handleGoogleAuth(activity: Activity, navController: NavController) {
        val window = activity.window
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(authNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        try {
            val credentialManager = CredentialManager.create(activity)
            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result.user?.let { firebaseUser ->
                                _user.value = firebaseUser
                                saveUserToDB(firebaseUser)
                                val insetsController = WindowInsetsControllerCompat(window, window.decorView)
                                insetsController.show(WindowInsetsCompat.Type.statusBars())
                                insetsController.show(WindowInsetsCompat.Type.navigationBars())
                                navController.navigate(AppRoutes.DASHBOARD.route) {
                                    popUpTo(0)
                                }
                            }
                            Log.d("FIREBASE_AUTH", "Successfully logged in.")
                        } else {
                            Log.d("FIREBASE_AUTH", "Failed to log in.")
                        }
                    }
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("FIREBASE_AUTH", "Auth failed: ${e.message}")
                }
            } else {
                Log.e("FIREBASE_AUTH", "Credential is not of type Google ID!")
            }
        } catch (e: Exception) {
            if (e.message?.contains("cancelled") == true) {
                Log.e("FIREBASE_AUTH", e.message.toString())
            } else {
                Log.e("FIREBASE_AUTH", "Auth failed: ${e.message}")
            }
        }
    }

    fun saveUserToDB(user: FirebaseUser) {
        val userData = hashMapOf(
            "id" to user.uid,
            "name" to user.displayName,
            "email" to user.email,
            "phone" to user.phoneNumber,
            "created_at" to FieldValue.serverTimestamp()
        )
        try {
            store.collection("users").document(user.uid).get().addOnSuccessListener {
                if (it.exists().not()) {
                    store.collection("users").document(user.uid).set(userData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FIREBASE", "Successfully added user.")
                            } else {
                                Log.d("FIREBASE", "Failed to add user")
                            }
                        }
                }
            }
        } catch (e: Exception){
            if (e is FirebaseFirestoreException) {
                val firestoreException = e
                val code = firestoreException.code
                when (code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> Log.e(
                        "FIREBASE",
                        "Permission denied"
                    )

                    FirebaseFirestoreException.Code.UNAVAILABLE -> Log.e(
                        "FIREBASE",
                        "Service unavailable"
                    )

                    else -> Log.e("FIREBASE", "Error: " + firestoreException.message)
                }
            } else {
                Log.e("FIREBASE", "Unknown error", e)
            }
        }
    }

    suspend fun signOut(activity: Activity) {
        try {
            firebaseAuth.signOut()
            _user.value = null
            val credentialManager = CredentialManager.create(activity)
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e("FIREBASE_AUTH", "Couldn't clear user credentials: ${e.localizedMessage}")
        }
    }
}