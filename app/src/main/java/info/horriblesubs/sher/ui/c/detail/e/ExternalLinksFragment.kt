package info.horriblesubs.sher.ui.c.detail.e

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.horrible.DownloadAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.setGridLayoutAdapter
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_e.view.*
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease.Download as Link

class ExternalLinksFragment: BaseFragment(), OnItemClickListener<Link> {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_e
    private val adapter = DownloadAdapter(this)
    override val name: String = "show-external"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.setGridLayoutAdapter(adapter, 2)
        model.detail.observe(viewLifecycleOwner, Observer {
            it.value?.apply {
                adapter.reset(
                    Link(
                        source = if (mal_id.isNullOrBlank()) "Search" else "View in" + "  MyAnimeList",
                        link = "https://myanimelist.net/anime" + if (mal_id.isNullOrBlank())
                            ".php?q=${Uri.encode(title)}" else "/$mal_id"
                    ),
                    Link(
                        source = "Search Nyaa.si",
                        link = "https://nyaa.si/?f=0&c=0_0&q=${Uri.encode(title)}"
                    ),
                    Link(
                        source = "Search AnimeKaizoku",
                        link = "https://animekaizoku.com//?s=${Uri.encode(title)}"
                    ),
                    clear = true
                )
            }
        })
    }

    override fun onItemClick(view: View, t: Link?, position: Int) {
        val url = t?.link
        if (!url.isNullOrBlank())
            startBrowser(context, url)
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}