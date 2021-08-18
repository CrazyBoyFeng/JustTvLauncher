package com.github.crazyboyfeng.justTvLauncher.model

import android.graphics.drawable.Drawable

class Shortcut(
    val id: String,
    val title: String,
    val icon: Drawable?,
    val banner: Drawable?
) {
    lateinit var category: String
    var openCount: Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Shortcut
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}