package info.horriblesubs.sher.ui.main.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.MenuHandler
import info.horriblesubs.sher.ui.main.explore.Explore.Keys.*
import info.horriblesubs.sher.ui.main.explore.random.Random
import info.horriblesubs.sher.ui.main.explore.recent.Recent
import info.horriblesubs.sher.ui.main.explore.trending.Trending

class Explore: Fragment(), PopupMenu.OnMenuItemClickListener {
    private var menuHandler: MenuHandler? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout._b_fragment_1, group, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuHandler = MenuHandler(this, view, R.menu.menu_a)
        fragment(R.id.fragment1, TRENDING)
        fragment(R.id.fragment2, RANDOM)
        fragment(R.id.fragment3, RECENT)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        (childFragmentManager.findFragmentById(R.id.fragment1) as Trending?)?.onMenuItemClick(item)
        (childFragmentManager.findFragmentById(R.id.fragment3) as Recent?)?.onMenuItemClick(item)
        return true
    }

    private fun fragment(@IdRes id: Int, key: Keys) {
        childFragmentManager.beginTransaction().replace(id, key.fragment, key.name).commit()
    }

    private enum class Keys {
        RECENT, RANDOM, TRENDING;
        internal val fragment: Fragment
            get() = when (this) {
                TRENDING -> Trending()
                RECENT -> Recent()
                RANDOM -> Random()
            }
    }
}