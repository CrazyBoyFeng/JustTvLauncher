package com.github.crazyboyfeng.justTvLauncher.browse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.ViewModelProvider
import com.github.crazyboyfeng.justTvLauncher.model.Shortcut
import java.text.DateFormat
import java.util.Date


class BrowseFragment : BrowseSupportFragment() {
    private val handler = Handler(Looper.getMainLooper())
    private val tick = Runnable { startTick() }
    private val dateFormat = DateFormat.getTimeInstance()
    private fun startTick() {
        title = dateFormat.format(Date())
        handler.postDelayed(tick, 1000)
    }

    private lateinit var viewModel: BrowseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        headersState = HEADERS_DISABLED
        val factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(BrowseViewModel::class.java)
        viewModel.browseContent.observe(this, {
            adapter = BrowseAdapter(it!!)
        })
        setOnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Shortcut -> {
                    var intent: Intent? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent =
                            requireContext().packageManager.getLeanbackLaunchIntentForPackage(item.id)
                    }
                    if (intent == null) {
                        intent = requireContext().packageManager.getLaunchIntentForPackage(item.id)
                    }
                    startActivity(intent)
                    viewModel.incrementOpenCount(item)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startTick()
        packageChangedReceiver.register(requireContext())
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(tick)
        packageChangedReceiver.unregister(requireContext())
    }

    private val packageChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (SCHEME_PACKAGE != intent.scheme) {
                return
            }
            if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                return
            }
            if (Intent.ACTION_PACKAGE_REMOVED == intent.action) {
                val packageName = intent.data!!.schemeSpecificPart
                viewModel.removePackage(packageName)
            } else {
                viewModel.loadShortcutGroupList()
            }
        }

        fun register(context: Context) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
            intentFilter.addDataScheme(SCHEME_PACKAGE)
            context.registerReceiver(this, intentFilter)
        }

        fun unregister(context: Context) {
            context.unregisterReceiver(this)
        }
    }

    companion object {
        const val SCHEME_PACKAGE = "package"
    }
}