package com.example.todolistapp.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_ACCOUNT_ID = "account_id"
        private const val KEY_NAME = "full_name"
        private const val KEY_EMAIL = "email"
    }

    // 🔐 Lưu thông tin user sau login
    fun saveUser(accountId: Int, name: String, email: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGIN, true)
            putInt(KEY_ACCOUNT_ID, accountId)
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            apply()
        }
    }

    // ✔ kiểm tra đã login chưa
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    // 📌 lấy account_id
    fun getAccountId(): Int {
        return prefs.getInt(KEY_ACCOUNT_ID, -1)
    }

    fun getName(): String? {
        return prefs.getString(KEY_NAME, null)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    // 🚪 logout
    fun logout() {
        prefs.edit().clear().apply()
    }
}