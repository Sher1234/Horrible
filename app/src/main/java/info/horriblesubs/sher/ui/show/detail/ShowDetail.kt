package info.horriblesubs.sher.ui.show.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.GoogleAds
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.show.detail.a.A
import info.horriblesubs.sher.ui.show.detail.b.B
import info.horriblesubs.sher.ui.show.detail.c.C
import info.horriblesubs.sher.ui.show.detail.d.D
import info.horriblesubs.sher.ui.show.detail.e.E

class ShowDetail: Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GoogleAds.BANNER.ad(view)
        fragment(R.id.fragment1,
            Keys.FragA
        )
        fragment(R.id.fragment2,
            Keys.FragB
        )
        fragment(R.id.fragment3,
            Keys.FragC
        )
        fragment(R.id.fragment4,
            Keys.FragD
        )
        fragment(R.id.fragment5,
            Keys.FragE
        )
    }

    private fun fragment(@IdRes id: Int, key: Keys) {
        childFragmentManager.beginTransaction().replace(id, key.fragment, key.name).commit()
    }

    private enum class Keys {
        FragA, FragB, FragC, FragD, FragE;
        val fragment: Fragment
            get() = when (this) {
                FragA -> A()
                FragB -> B()
                FragC -> C()
                FragD -> D()
                FragE -> E()
            }
    }
}