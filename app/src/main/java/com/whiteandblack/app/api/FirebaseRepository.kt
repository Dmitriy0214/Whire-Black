package com.whiteandblack.app.api

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.whiteandblack.app.data.storage.models.History
import com.whiteandblack.app.utils.Const
import com.whiteandblack.app.utils.Const.Companion.USERS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseRepository {

    companion object {
        private var auth: FirebaseAuth = Firebase.auth

        fun checkAuth(callback: (FirebaseUser?) -> Unit) {
            callback(auth.currentUser)
        }
    }

    private var firestore: FirebaseFirestore = Firebase.firestore

    fun signInFirebase(
        activity: Activity,
        email: String,
        password: String,
        callback: (FirebaseUser?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.e("TEST", "signInWithEmail:success")
                        val user = auth.currentUser
                        callback(user)
                    } else {
                        Log.e("TEST", "signInWithEmail:failure", task.exception)

                        callback(null)
                    }
                }
        }
    }

    fun signUpFirebase(
        activity: Activity,
        email: String,
        password: String,
        callback: (FirebaseUser?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.e("TEST", "createUserWithEmail:success")
                        val user = auth.currentUser
                        callback(user)
                    } else {
                        Log.e("TEST", "createUserWithEmail:failure", task.exception)
                        callback(null)
                    }
                }
        }
    }

    fun signOutFirebase() {
        auth.signOut()
    }

    fun getUserInfo(user: FirebaseUser, callback: (UserInfo?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(USERS)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == user.uid) {
                            callback(
                                UserInfo(
                                    document.data[Const.SUM].toString().toInt()
                                )
                            )
                            return@addOnSuccessListener
                        }
                    }
                    callback(null)
                }
                .addOnFailureListener { exception ->
                    Log.e(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }

    fun updateUserInfo(user: FirebaseUser, userInfo: UserInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(USERS)
                .document(user.uid)
                .set(mapOf(Const.SUM to userInfo.sum))
        }
    }

    fun insertHistory(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection(Const.HISTORY)
                .document()
                .set(mapOf(
                    Const.ID to history.id,
                    Const.SUM to history.sum,
                    Const.CATEGORY to history.category,
                    Const.DATE to history.date,
                    Const.TYPE to history.type,
                ))
        }
    }

    fun getHistory(userKey: String, callback: (List<History>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = mutableListOf<History>()
            firestore.collection(Const.HISTORY)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val key = document.data[Const.ID].toString()
                        if (userKey == key) {
                            list.add(
                                History(
                                    document.data[Const.ID].toString(),
                                    document.data[Const.SUM].toString(),
                                    document.data[Const.CATEGORY].toString(),
                                    document.data[Const.DATE].toString(),
                                    document.data[Const.TYPE].toString().toBoolean()
                                )
                            )
                        }

                    }
                    callback(list)
                }
                .addOnFailureListener { exception ->
                    Log.e(Const.LOG_TAG, "Error getting documents.", exception)
                }
        }
    }
}

data class UserInfo(
    var sum: Int = 0,
)