package info.horriblesubs.sher.ui.c.detail.d

import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.Info
import info.horriblesubs.sher.ui._extras.adapters.InfoAdapter
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.setFlexLayoutAdapter
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_d.view.*
import java.time.ZonedDateTime

class ShowInfoFragment: BaseFragment() {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_d
    override val name: String = "show-info"
    private val adapter = InfoAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView?.setFlexLayoutAdapter(adapter)
        adapter.apply {
            adapter.removeAll()
            adapter.add(
                Info("Data Source", "SubsPlease"),
                Info("Detail Cache Time", "Never"),
                Info("Releases Cache Time", "Never")
            )
        }
        model.episodesTime.observe(viewLifecycleOwner) { onEpisodesChanged(it) }
        model.detailTime.observe(viewLifecycleOwner) { onBatchesChanged(it) }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    private fun onEpisodesChanged(t: ZonedDateTime?) {
        adapter.update(2) {
            value = (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), t)
            else TimeFormatPreference.format(t)) ?: "Never"
            this
        }
    }

    private fun onBatchesChanged(t: ZonedDateTime?) {
        adapter.update(1) {
            value = (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), t)
            else TimeFormatPreference.format(t)) ?: "Never"
            this
        }
    }
}