package info.horriblesubs.sher.ui.c.detail.b

import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease
import info.horriblesubs.sher.data.subsplease.api.model.ItemReleasePage
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
        model.episodes.observe(viewLifecycleOwner) {
            onResetView(episodes = getMap(it?.value))
        }
        model.episodesTime.observe(viewLifecycleOwner) {
            view.lastUpdatedText?.text = (if (TimeLeftPreference.value)
                getRelativeTime(ZonedDateTime.now(), it) else
                TimeFormatPreference.format(it)) ?: "Never"
        }
    }

    private fun getMap(page: ItemReleasePage?): LinkedHashMap<String, ItemRelease> {
        val map = LinkedHashMap<String, ItemRelease>()
        page?.batch?.let { map.putAll(it) }
        page?.episode?.let { map.putAll(it) }
        return map
    }

    private fun onResetView(
        episodes: LinkedHashMap<String, ItemRelease> = getMap(model.episodes.value?.value)
    ) = if (episodes.isNotEmpty()) setAdapter(episodes) else {
        view?.recyclerView?.gone
        view?.textView?.visible
    }

    override fun onItemClick(view: View, t: ItemRelease?, position: Int) {
        t?.let {
            model.liveSharedItem.value = it
            ReleaseViewFragment.get().show(childFragmentManager)
        }
    }

    private fun setAdapter(list: HashMap<String, ItemRelease>?) {
        val size = if ((list?.size ?: 0) > 3) 4 else list?.size ?: 0
        val arrayList = arrayListOf<ItemRelease>()
        var counter = 0
        for (item in list ?: hashMapOf()) {
            counter++
            arrayList.add(item.value)
            if (counter == size) break
        }
        (view?.recyclerView)?.spanCount = 4
        view?.recyclerView?.visible
        adapter.reset(arrayList)
        view?.textView?.gone
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}