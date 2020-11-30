package info.horriblesubs.sher.ui.c.detail.c

import android.os.Bundle
import android.view.View
import androidx.core.text.parseAsHtml
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_c.view.*

class DescriptionFragment: BaseFragment() {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_c
    override val name: String = "description"

    private var descToggle = false
        get() {
            field = !field
            return field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.detail.observe(viewLifecycleOwner) {
            view.synopsisId?.apply {
                if ((it?.value?.synopsis?.length ?: 0) <= 150) checkMarkDrawable = null
                else setCheckMarkDrawable(R.drawable.da_up_down)
            }
            view.descriptionText?.text = it?.value?.synopsis.short.parseAsHtml()
            descToggle = false
            view.synopsisId?.setOnClickListener { _ ->
                val b = descToggle
                view.synopsisId?.isChecked = b
                view.descriptionText?.text = (if (b) it?.value?.synopsis else it?.value?.synopsis?.short)?.parseAsHtml()
            }
        }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    private val String?.short get() = if (this.isNullOrBlank()) "" else
        substring(0, if (length > 150) 150 else length) + if (length > 150) "..." else ""
}