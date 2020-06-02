package info.horriblesubs.sher.ui.b.explore

import android.os.Bundle
import android.view.MenuItem
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.b.explore.latest.LatestFragment
import info.horriblesubs.sher.ui.b.explore.random.RandomFragment
import info.horriblesubs.sher.ui.b.explore.trending.TrendingFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_2.view.*

class ExploreFragment: BaseFragment(), Toolbar.OnToolbarActionListener {
    override val layoutId: Int = R.layout.b_fragment_2
    override val name: String = "explore"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.toolbar?.apply {
            onToolbarActionListener = this@ExploreFragment
            inflateMenu(R.menu.menu_c)
        }
        childFragmentManager.beginTransaction().apply {
                TrendingFragment().let {
                    replace(R.id.fragmentContainerView2, it, it.name)
                }
                RandomFragment().let {
                    replace(R.id.fragmentContainerView3, it, it.name)
                }
                LatestFragment().let {
                    replace(R.id.fragmentContainerView4, it, it.name)
                }
            }.commit()
    }

    override fun onMenuItemClickListener(item: MenuItem) {
        if(item.itemId == R.id.settings)
            (activity as? MainActivity)?.onNavigationItemSelected(item)
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}