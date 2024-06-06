package com.whiteandblack.app.ui.screen.main

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.whiteandblack.app.R
import com.whiteandblack.app.api.FirebaseRepository
import com.whiteandblack.app.api.UserInfo
import com.whiteandblack.app.data.storage.PrefManager
import com.whiteandblack.app.data.storage.models.History
import com.whiteandblack.app.utils.Const.Companion.DEF_CATEGORY_TYPE
import com.whiteandblack.app.utils.ToastHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class MainViewModel: ViewModel() {

    companion object {
        var user: FirebaseUser? = null
        var userInfo = MutableStateFlow(UserInfo())

        val operationType = MutableStateFlow("")
        val historyList = MutableStateFlow(listOf<History>())
    }

    val scoreText = MutableStateFlow("")
    val categoryName = MutableStateFlow(DEF_CATEGORY_TYPE)

    fun login(context: Context, login: String, password: String, callback: (Boolean) -> Unit) {
        if (login.isEmpty() || password.isEmpty()){
            ToastHelper().show(context, context.getString(R.string.toastEmptyToken))
            callback(false)
            return
        }
        if (password.length < 6) {
            ToastHelper().show(context, context.getString(R.string.toastErrorPass))
            callback(false)
            return
        }

        val activity = context as Activity

        FirebaseRepository().signInFirebase(activity, login, password) {
            callback(it != null)
        }
    }

    fun reg(context: Context, login: String, password: String, secondPassword: String, callback: (Boolean) -> Unit) {
        if (login.isEmpty() || password.isEmpty()){
            ToastHelper().show(context, context.getString(R.string.toastEmptyToken))
            callback(false)
            return
        }
        if (password.length < 6) {
            ToastHelper().show(context, context.getString(R.string.toastErrorPass))
            callback(false)
            return
        }
        if (password != secondPassword) {
            ToastHelper().show(context, context.getString(R.string.toastErrorPassEqual))
            callback(false)
            return
        }

        FirebaseRepository().signUpFirebase(context as Activity, login, password) {
            callback(it != null)
        }
    }

    fun logout() {
        PrefManager().clear()
        user = null
        userInfo.value = UserInfo()
        historyList.value = listOf()
        FirebaseRepository().signOutFirebase()
    }

    fun getUserInfo() {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            FirebaseRepository().getUserInfo(user!!) {
                if (it != null) userInfo.value = it
            }
        }
    }

    private fun updateUser(userInfo: UserInfo) {
        MainViewModel.userInfo.value = userInfo
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            FirebaseRepository().updateUserInfo(user!!, userInfo)
        }
    }

    fun loadHistory() {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {
            FirebaseRepository().getHistory(user!!.uid) {
                historyList.value = it
            }
        }
    }

    fun insertHistory(history: History) {
        if (user == null) FirebaseRepository.checkAuth { user = it }
        if (user != null) {

            if (history.type) userInfo.value.sum -= history.sum.toInt()
            else userInfo.value.sum += history.sum.toInt()
            updateUser(userInfo.value)

            history.id = user!!.uid
            FirebaseRepository().insertHistory(history)
        }
    }
}