package com.github.crazyboyfeng.justTvLauncher.repository

import android.content.Context
import com.github.crazyboyfeng.kotlin.SingletonHolder

class OpenCountRepository private constructor(context: Context) {
    companion object : SingletonHolder<OpenCountRepository, Context>(::OpenCountRepository)

    private val sharedPreferences = context.getSharedPreferences("open_count", 0)
    private val editor = sharedPreferences.edit()
    fun query(className: String): Int {
        return sharedPreferences.getInt(className, 0)
    }

    fun update(id: String, openCount: Int) {
        editor.putInt(id, openCount).apply()
    }

    fun delete(id: String) {
        editor.remove(id).apply()
    }
}