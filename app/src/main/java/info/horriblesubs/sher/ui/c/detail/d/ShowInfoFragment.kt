package info.horriblesubs.sher.ui.c.detail.d

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.detailAgo
import info.horriblesubs.sher.data.horrible.api.detailTimeStamp
import info.horriblesubs.sher.data.horrible.api.model.ItemShow
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.ShowInfoAdapter
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.setLinearLayoutAdapter
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_d.view.*
import java.time.ZonedDateTime

class ShowInfoFragment: BaseFragment() {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_d
    override val name: String = "show-info"
    private val adapter =
        ShowInfoAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.setLinearLayoutAdapter(adapter, RecyclerView.HORIZONTAL)
        adapter.apply {
            adapter.removeAll()
            adapter.add(
                ShowInfoAdapter.ShowInfo("Total Views", 0.toString()),
                ShowInfoAdapter.ShowInfo("Total Bookmarked", 0.toString()),
                ShowInfoAdapter.ShowInfo("Data Source", "HorribleSubs"),
                ShowInfoAdapter.ShowInfo("Detail Cache Time", "Never"),
                ShowInfoAdapter.ShowInfo("Batches Cache Time", "Never"),
                ShowInfoAdapter.ShowInfo("Episodes Cache Time", "Never")
            )
        }
        model.episodesTime.observe(viewLifecycleOwner, Observer { onEpisodesChanged(it) })
        model.batchesTime.observe(viewLifecycleOwner, Observer { onBatchesChanged(it) })
        model.detail.observe(viewLifecycleOwner, Observer { onDetailChanged(it) })
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    private fun onDetailChanged(t: RepositoryResult<ItemShow>?) {
        t?.value.let {
            adapter.update(0) {
                value = (it?.views ?: 0).toString()
                this
            }
            adapter.update(1) {
                value = (it?.favs ?: 0).toString()
                this
            }
            adapter.update(3) {
                value = (if (TimeLeftPreference.value) it?.detailAgo
                else TimeFormatPreference.format(it.detailTimeStamp)) ?: "Never"
                this
            }
        }
    }
    private fun onEpisodesChanged(t: ZonedDateTime?) {
        adapter.update(5) {
            value = (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), t)
            else TimeFormatPreference.format(t)) ?: "Never"
            this
        }
    }
    private fun onBatchesChanged(t: ZonedDateTime?) {
        adapter.update(4) {
            value = (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), t)
            else TimeFormatPreference.format(t)) ?: "Never"
            this
        }
    }
}