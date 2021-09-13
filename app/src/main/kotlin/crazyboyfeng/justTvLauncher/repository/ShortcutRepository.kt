package crazyboyfeng.justTvLauncher.repository

import android.content.Context
import android.content.Intent
import android.os.Build
import crazyboyfeng.justTvLauncher.model.Shortcut

class ShortcutRepository(private val context: Context) {
    // Contain IO operations.
    // Using singleton mode to avoid problems
    // that may arise from repeated initialization
    // when using in multiple components at the same time.
    private val categoryData = CategoryRepository.getInstance(context)
    private val openCountData = OpenCountRepository.getInstance(context)
    fun load(): Set<Shortcut> {
        val all = load(Intent.CATEGORY_LAUNCHER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            all.addAll(load(Intent.CATEGORY_LEANBACK_LAUNCHER))
        }
        return all
    }

    private fun load(category: String): MutableSet<Shortcut> {
        val all = mutableSetOf<Shortcut>()
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(category)
        val activityList = packageManager.queryIntentActivities(intent, 0)
        for (it in activityList) {
            val packageName = it.activityInfo.packageName
            if (packageName == context.packageName) {
                continue
            }
            val label = it.activityInfo.loadLabel(packageManager).toString()
            val banner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                it.activityInfo.loadBanner(packageManager)
            } else {
                null
            }
            val icon = if (banner == null) {
                it.activityInfo.loadIcon(packageManager)
            } else {
                null
            }
            val shortcut = Shortcut(packageName, label, icon, banner)
            shortcut.category = categoryData.query(shortcut.id) ?: categoryData.load(packageName)
            shortcut.openCount = openCountData.query(shortcut.id)
            all.add(shortcut)
        }
        return all
    }

    fun updateOpenCount(shortcut: Shortcut) {
        openCountData.update(shortcut.id, shortcut.openCount)
    }

    fun deleteById(id: String) {
        categoryData.delete(id)
        openCountData.delete(id)
    }

}
