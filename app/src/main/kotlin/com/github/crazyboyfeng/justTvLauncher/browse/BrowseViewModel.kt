package com.github.crazyboyfeng.justTvLauncher.browse

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.crazyboyfeng.justTvLauncher.model.Shortcut
import com.github.crazyboyfeng.justTvLauncher.model.ShortcutGroup
import com.github.crazyboyfeng.justTvLauncher.repository.ShortcutRepository
import kotlin.collections.HashMap

/**
 * The lifecycle of this view-model is consistent with the application.
 * Since `stateNotNeeded` is set, the caching of `ViewModel` doesn't work.
 * I wrote it this way just for logical separation.
 */
class BrowseViewModel(application: Application) : AndroidViewModel(application) {
    private val shortcutRepository = ShortcutRepository(application)
    val browseContent = MutableLiveData<List<ShortcutGroup>>()
    init {
        /**
         * I don't know why the `ViewModel` in google tv samples passed a `Repository` as a parameter
         */
        loadShortcutGroupList()
    }

    fun loadShortcutGroupList(){
        val shortcutGroupByCategory = HashMap<String, ShortcutGroup>()
        shortcutRepository.load().forEach {
            if (shortcutGroupByCategory.containsKey(it.category)) {
                shortcutGroupByCategory[it.category]!!.add(it)
            } else {
                val shortcutGroup = ShortcutGroup(it.category, mutableListOf(it))
                shortcutGroupByCategory[it.category!!] = shortcutGroup
            }
        }
        val shortcutGroupList = shortcutGroupByCategory.values.sortedByDescending {
            Log.v(TAG,"${it.category}: ${it.openCount}")
            it.openCount
        }
        browseContent.postValue(shortcutGroupList)
    }
    fun incrementOpenCount(shortcut: Shortcut) {
        Log.v(TAG,"${shortcut.id}: ${shortcut.openCount}+1")
        shortcut.openCount++
        shortcutRepository.updateOpenCount(shortcut)
        loadShortcutGroupList()
    }
    fun removePackage(packageName: String) {
        shortcutRepository.deleteById(packageName)
        loadShortcutGroupList()
    }
    companion object {
        private const val TAG = "BrowseViewModel"
    }
}