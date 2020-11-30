package info.horriblesubs.sher.ui.b.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.BFragment2Binding
import info.horriblesubs.sher.ui.b.explore.latest.LatestFragment
import info.horriblesubs.sher.ui.b.explore.today.TodayFragment
import info.horriblesubs.sher.ui.viewBindings

class ExploreFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?) =
        viewBindings<BFragment2Binding>(inflater, R.layout.b_fragment_2, group).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().apply {
            LatestFragment().let { replace(R.id.fragment2, it, it.javaClass.name) }
            TodayFragment().let { replace(R.id.fragment1, it, it.javaClass.name) }
        }.commit()
    }
}