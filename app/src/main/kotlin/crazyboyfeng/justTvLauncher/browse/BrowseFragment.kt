package crazyboyfeng.justTvLauncher.browse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.ViewModelProvider
import crazyboyfeng.justTvLauncher.model.Shortcut
import java.text.DateFormat
import java.util.*


class BrowseFragment : BrowseSupportFragment() {
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = DateFormat.getTimeInstance()
    private lateinit var viewModel: BrowseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        headersState = HEADERS_DISABLED
        val factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(BrowseViewModel::class.java)
        viewModel.browseContent.observe(this) {
            adapter = BrowseAdapter(it!!)
        }
        setOnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Shortcut -> {
                    launch(item.id)
                    viewModel.incrementOpenCount(item)
                    setSelect(item)
                }
            }
        }
    }

    private fun setTick() = handler.post {
        title = dateFormat.format(Date())
    }

    private fun launch(packageName: String) {
        val packageManager = requireContext().packageManager
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent = packageManager.getLeanbackLaunchIntentForPackage(packageName)
        }
        if (intent == null) {
            intent = packageManager.getLaunchIntentForPackage(packageName)
        }
        startActivity(intent)
    }


    private fun setSelect(shortcut: Shortcut) = handler.post {
        val position = viewModel.findPosition(shortcut)
        val task = ListRowPresenter.SelectItemViewHolderTask(position.second)
        task.isSmoothScroll = false
        rowsSupportFragment.setSelectedPosition(position.first, false, task)
    }

    override fun onStart() {
        super.onStart()
        val context = requireContext()
        context.registerReceiver(timeTickReceiver, timeTickReceiver.getIntentFilter())
        context.registerReceiver(packageChangedReceiver, packageChangedReceiver.getIntentFilter())
    }

    override fun onStop() {
        super.onStop()
        val context = requireContext()
        context.unregisterReceiver(timeTickReceiver)
        context.unregisterReceiver(packageChangedReceiver)
    }

    private val timeTickReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (Intent.ACTION_TIME_TICK == intent.action) {
                setTick()
            }
        }

        fun getIntentFilter(): IntentFilter {
            return IntentFilter(Intent.ACTION_TIME_TICK)
        }
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

        fun getIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
            intentFilter.addDataScheme(SCHEME_PACKAGE)
            return intentFilter
        }
    }

    companion object {
        private const val SCHEME_PACKAGE = "package"
    }
}