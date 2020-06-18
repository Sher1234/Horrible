package info.horriblesubs.sher.ui.c.detail.e

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.DownloadAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.d.SearchAnimeActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_e.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease.Download as Link

class ExternalLinksFragment: BaseFragment(), OnItemClickListener<Link> {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_e
    private val adapter = DownloadAdapter(this)
    override val name: String = "show-external"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.setGridLayoutAdapter(adapter, 2)
        model.detail.observe(viewLifecycleOwner) {
            it.value?.apply {
                adapter.reset(
                    Link(
                        source = (if (mal_id.isNullOrBlank()) "Search" else "View in") + " MyAnimeList",
                        link = if (mal_id.isNullOrBlank()) title else "https://myanimelist.net/anime/$mal_id"
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
        }
    }

    @ExperimentalCoroutinesApi
    override fun onItemClick(view: View, t: Link?, position: Int) {
        if (t?.source == "Search MyAnimeList") {
            startActivity(
                Intent(context, SearchAnimeActivity::class.java)
                    .apply { putExtra(EXTRA_DATA, t.link) }
            )
        } else {
            val url = t?.link
            if (!url.isNullOrBlank())
                startBrowser(context, url)
        }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }
}