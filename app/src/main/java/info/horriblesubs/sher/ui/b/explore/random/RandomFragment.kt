package info.horriblesubs.sher.ui.b.explore.random

import android.os.Bundle
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.toBookmarkedShow
import info.horriblesubs.sher.libs.dialog.RandomDialog
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_2_b.*

class RandomFragment: BaseFragment() {
    override val layoutId: Int = R.layout.b_fragment_2_b
    private val model: RandomModel by viewModels()
    private var dialog: RandomDialog? = null
    override val name: String = "random"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.result.observe(viewLifecycleOwner) { show ->
            if (show != null)
                dialog = context?.let{RandomDialog(it, show.toBookmarkedShow())}
            dialog?.show()
        }
        button?.setOnClickListener {
            model.refresh()
        }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
        dialog?.dismiss()
        dialog = null
        model.stop()
    }
}