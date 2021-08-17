package com.github.crazyboyfeng.justTvLauncher.browse

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import com.github.crazyboyfeng.justTvLauncher.model.ShortcutGroup

class BrowseAdapter(shortcutGroupList: List<ShortcutGroup>) :
    ArrayObjectAdapter(ListRowPresenter()) {
    init {
        addShortcutGroupList(shortcutGroupList)
    }

    private fun addShortcutGroupList(shortcutGroupList: List<ShortcutGroup>) {
        val cardPresenter = ShortcutCardPresenter()
        shortcutGroupList.forEach {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            listRowAdapter.addAll(0, it.shortcutList)
            val headerItem = HeaderItem(it.category)
            add(ListRow(headerItem, listRowAdapter))
        }
    }
}
