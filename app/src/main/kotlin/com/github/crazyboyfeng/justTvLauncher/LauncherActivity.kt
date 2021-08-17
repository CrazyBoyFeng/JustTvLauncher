package com.github.crazyboyfeng.justTvLauncher

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.github.crazyboyfeng.justTvLauncher.databinding.ActivityLauncherBinding


class LauncherActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {}
}