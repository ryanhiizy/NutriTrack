package com.fit2081.a1ryanhii34466576.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUserSession(userId: String) {
        prefs.edit {
            putString(KEY_USER_ID, userId)
                .putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun getLoggedInUserId(): String? {
        return if (isLoggedIn()) {
            prefs.getString(KEY_USER_ID, null)
        } else null
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        prefs.edit {
            remove(KEY_USER_ID)
                .putBoolean(KEY_IS_LOGGED_IN, false)
        }
    }
}
