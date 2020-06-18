package info.horriblesubs.sher.ui.c.detail.c

import android.os.Bundle
import androidx.core.text.parseAsHtml
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.viewModels
import info.horriblesubs.sher.ui.visible
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.detail.observe(viewLifecycleOwner) {
            view?.button?.apply { if ((it?.value?.body?.length ?: 0) > 150) visible else gone }
            view?.descriptionText?.text = it?.value?.body.short.parseAsHtml()
            view?.button?.setText(R.string.view_more)
            descToggle = false
            view?.button?.setOnClickListener { _ ->
                val b = descToggle
                view?.descriptionText?.text = (if (b) it?.value?.body else it?.value?.body?.short)?.parseAsHtml()
                view?.button?.setText(if (b) R.string.view_less else R.string.view_more)
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