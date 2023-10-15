package com.dangjang.android.data.storage

import android.content.Context
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.storage.InAppStorageHelper

class InAppStorageHelperImpl(context: Context): InAppStorageHelper  {
    private val sharedPreferences = context.getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

    override fun setAccessToken(key: String, accessToken: String?) {
        sharedPreferences.edit().putString(key, accessToken).apply()
    }

    override fun getAccessToken(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

}