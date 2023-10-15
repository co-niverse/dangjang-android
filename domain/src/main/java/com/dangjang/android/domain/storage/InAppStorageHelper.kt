package com.dangjang.android.domain.storage

interface InAppStorageHelper{
    fun setAccessToken(key: String, accessToken: String?)
    fun getAccessToken(key: String): String?
}