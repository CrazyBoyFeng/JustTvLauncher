package com.github.crazyboyfeng.justTvLauncher

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.github.crazyboyfeng.justTvLauncher.databinding.ActivityLauncherBinding


class LauncherActivity : FragmentActivity() {
//    private lateinit var backgroundManager: BackgroundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        backgroundManager = BackgroundManager.getInstance(this).apply {
//            if (!isAttached) {
//                attach(this@LauncherActivity.window)
//            }
//            drawable = WallpaperManager.getInstance(this@LauncherActivity).drawable
//        }
    }

    override fun onBackPressed() {
//        super.onBackPressed();
    }
}