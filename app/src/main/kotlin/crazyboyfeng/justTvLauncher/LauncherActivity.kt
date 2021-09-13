package crazyboyfeng.justTvLauncher

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class LauncherActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }

    override fun onBackPressed() {}
}