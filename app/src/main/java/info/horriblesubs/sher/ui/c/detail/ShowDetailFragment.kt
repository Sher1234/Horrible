package info.horriblesubs.sher.ui.c.detail

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.c.detail.a.ImageTitleFragment
import info.horriblesubs.sher.ui.c.detail.b.NewReleaseFragment
import info.horriblesubs.sher.ui.c.detail.c.DescriptionFragment
import info.horriblesubs.sher.ui.c.detail.d.ShowInfoFragment
import info.horriblesubs.sher.ui.c.detail.e.ExternalLinksFragment

class ShowDetailFragment: BaseFragment() {

    override val layoutId: Int = R.layout.c_fragment_1
    override val name: String = "show-detail-home"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetFragment(R.id.fragmentContainerView5, ExternalLinksFragment())
        onSetFragment(R.id.fragmentContainerView3, DescriptionFragment())
        onSetFragment(R.id.fragmentContainerView2, NewReleaseFragment())
        onSetFragment(R.id.fragmentContainerView1, ImageTitleFragment())
        onSetFragment(R.id.fragmentContainerView4, ShowInfoFragment())
    }

    private fun onSetFragment(@IdRes id: Int, fragment: Fragment) =
        childFragmentManager.beginTransaction()
            .replace(id, fragment, fragment.javaClass.name).commit()
}