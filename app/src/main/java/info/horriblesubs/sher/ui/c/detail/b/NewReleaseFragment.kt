package info.horriblesubs.sher.ui.c.detail.b

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.ReleaseAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.c.view.ReleaseViewFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_b.view.*
import java.time.ZonedDateTime

class NewReleaseFragment: BaseFragment(), OnItemClickListener<ItemRelease> {

    private val adapter = ReleaseAdapter(this)
    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_b
    override val name: String = "show-new-release"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView?.setGridLayoutAdapter(adapter, 4)
        model.episodes.observe(viewLifecycleOwner, Observer {
            it?.value?.let { list -> onResetView(episodes = list) }
        })
        model.batches.observe(viewLifecycleOwner, Observer {
            it?.value?.let { list -> onResetView(batches = list) }
        })
        model.episodesTime.observe(viewLifecycleOwner, Observer {
            view.lastUpdatedText?.text = (if (TimeLeftPreference.value)
                getRelativeTime(ZonedDateTime.now(), it) else
                TimeFormatPreference.format(it)) ?: "Never"
        })
    }

    private fun onResetView(
        episodes: List<ItemRelease> = model.episodes.value?.value ?: emptyList(),
        batches: List<ItemRelease> = model.batches.value?.value ?: emptyList()
    ) {
        if (episodes.isNotEmpty() && batches.isNotEmpty()) {
            setAdapter(arrayListOf<ItemRelease>().apply {
                addAll(batches)
                addAll(episodes)
            }, 3)
        }
        else if (episodes.isNotEmpty()) setAdapter(episodes, 4)
        else if (batches.isNotEmpty()) setAdapter(batches, 3)
        else {
            view?.recyclerView?.gone
            view?.textView?.visible
        }
    }

    override fun onItemClick(view: View, t: ItemRelease?, position: Int) {
        t?.let {
            model.liveSharedItem.value = it
            ReleaseViewFragment.get().show(childFragmentManager)
        }
    }

    private fun setAdapter(list: List<ItemRelease>?, span: Int) {
        val size = if (list?.size ?: 0 > (span - 1)) span else list?.size ?: 0
        adapter.reset(list?.subList(0, size)?: emptyList())
        (view?.recyclerView)?.spanCount = span
        view?.recyclerView?.visible
        view?.textView?.gone
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}