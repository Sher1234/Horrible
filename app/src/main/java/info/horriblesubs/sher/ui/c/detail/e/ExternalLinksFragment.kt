package info.horriblesubs.sher.ui.c.detail.e

import android.net.Uri
import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.horrible.DownloadAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.d.SearchAnimeActivity
import info.horriblesubs.sher.ui.setGridLayoutAdapter
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_e.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ExternalLinksFragment: BaseFragment(), OnItemClickListener<String> {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_e
    private val adapter = DownloadAdapter(this)
    override val name: String = "show-external"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView?.setGridLayoutAdapter(adapter, 3)
        model.detail.observe(viewLifecycleOwner) {
            it.value?.apply {
                adapter.reset(
                    arrayListOf("Search MAL", "Search Nyaa", "Search AnimeKaizoku"),
                    arrayListOf(title, "https://nyaa.si/?f=0&c=0_0&q=${Uri.encode(title)}",
                        "https://animekaizoku.com//?s=${Uri.encode(title)}")
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onItemClick(view: View, t: String?, position: Int) {
        when (position) {
            0 -> SearchAnimeActivity.startSearchAnimeActivity(context, t)
            else -> {
                val url = t
                if (!url.isNullOrBlank())
                    startBrowser(context, url)
            }
        }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}