package y2k.joyreactor.ui.post

import android.content.Context
import y2k.joyreactor.App

/**
 * Created by omazhukin on 11/24/2015.
 */
object ProfileSession {

    val TAG = "AuthData"
    val TOKEN = "token"

    var context: Context = App.instance

    fun saveToken(token: String) {
        val preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(TAG, token)
        editor.apply()
    }

    fun getToken(): String {
        val preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return preferences.getString(TAG, "")
    }

    fun isActive(): Boolean {
        return getToken().length != 0
    }
}
