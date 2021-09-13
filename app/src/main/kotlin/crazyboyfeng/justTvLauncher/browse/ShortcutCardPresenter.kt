package crazyboyfeng.justTvLauncher.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import crazyboyfeng.justTvLauncher.R
import crazyboyfeng.justTvLauncher.databinding.PresenterShortcutCardBinding
import crazyboyfeng.justTvLauncher.model.Shortcut

class ShortcutCardPresenter : Presenter() {
    private var width = 320
    private var height = 180
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        width = context.resources.getDimension(R.dimen.card_width).toInt()
        height = context.resources.getDimension(R.dimen.card_height).toInt()
        val layoutInflater = LayoutInflater.from(context)
        val cardView = layoutInflater.inflate(R.layout.presenter_shortcut_card, parent, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val shortcut = item as Shortcut
        val binding = PresenterShortcutCardBinding.bind(viewHolder.view)
        if (shortcut.banner != null) {
            shortcut.banner.setBounds(0, 0, width, height)
            binding.content.setCompoundDrawables(shortcut.banner, null, null, null)
            binding.root.contentDescription = shortcut.title
        } else if (shortcut.icon != null) {
            shortcut.icon.setBounds(0, 0, height, height)
            binding.content.setCompoundDrawables(shortcut.icon, null, null, null)
            binding.content.text = shortcut.title
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val binding = PresenterShortcutCardBinding.bind(viewHolder.view)
        binding.content.setCompoundDrawables(null, null, null, null)
    }
}
