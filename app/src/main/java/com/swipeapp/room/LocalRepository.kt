package com.swipeapp.room

import android.content.Context
import com.swipeapp.utils.PreferenceHelper

class LocalRepository(private val database: SwipeDatabase, private val context:Context) {

    private val preferences by lazy { PreferenceHelper(context) }

    private val wordsDao = database.wordsDao()

    fun deleteAllTables() = database.clearAllTables()


}