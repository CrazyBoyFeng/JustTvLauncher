package crazyboyfeng.justTvLauncher.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import crazyboyfeng.justTvLauncher.R
import crazyboyfeng.kotlin.SingletonHolder

class CategoryRepository private constructor(private val context: Context) {
    companion object : SingletonHolder<CategoryRepository, Context>(::CategoryRepository)

    private val sharedPreferences = context.getSharedPreferences("category", 0)
    private val editor = sharedPreferences.edit()
    fun load(packageName: String): String {
        val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        return if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            context.getString(R.string.title_system)
        } else {
            context.getString(R.string.title_apps)
        }
    }

    fun query(id: String): String? {
        return sharedPreferences.getString(id, null)
    }

    fun update(id: String, category: String) {
        editor.putString(id, category).apply()
    }

    fun delete(id: String) {
        editor.remove(id).apply()
    }
}