package info.horriblesubs.sher.ui.c.releases

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.allEpisodesTimeStamp
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.data.horrible.api.someEpisodesTimeStamp
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.horrible.ReleaseAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.c.view.ReleaseViewFragment
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_2.view.*

class ReleasesFragment: BaseFragment(),
    OnItemClickListener<ItemRelease>, Toolbar.OnToolbarActionListener {

    private val isEpisodesShowing = MutableLiveData<Boolean>().apply {
        value = value ?: true
    }
    val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_2
    private val adapter = ReleaseAdapter(this)
    override val name: String = "show-releases"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.apply {
            adapter = this@ReleasesFragment.adapter
            setHasFixedSize(true)
        }
        view?.toolbar?.apply {
            onToolbarActionListener = this@ReleasesFragment
            customizeMenu = {
                it?.findItem(R.id.info)?.let { item ->
                    model.detail.value?.value?.let { value ->
                        (value.someEpisodesTimeStamp == null || value.allEpisodesTimeStamp == null)
                            .apply {
                                item.title = if (value.someEpisodesTimeStamp == null)
                                    getString(R.string.load_more_episodes) else
                                    getString(R.string.load_all_episodes)
                                item.isEnabled = this
                                item.isVisible = this
                            }
                    }
                }
                it?.findItem(R.id.refresh)?.let { item ->
                    item.title = if (isEpisodesShowing.value == false)
                        getString(R.string.refresh_new_batches) else
                        getString(R.string.refresh_new_episodes)
                }
                it?.findItem(R.id.toggle)?.title = getString(
                    if (isEpisodesShowing.value == false)
                        R.string.view_episodes else R.string.view_batches)
            }
            runCustomizeMenu
        }
    }

    override fun onResume() {
        super.onResume()
        isEpisodesShowing.observe(viewLifecycleOwner) {
            view?.recyclerView?.columnWidth = if (it == false) 1.45f else 1f
            onSetReleases(isEpisodesShowing = it ?: true)
            view?.toolbar?.runCustomizeMenu
        }
        model.episodes.observe(viewLifecycleOwner) {
            view?.toolbar?.runCustomizeMenu
            onChanged(it)
        }
    }

    private fun onSetReleases(
        episodes: List<ItemRelease> = model.episodes.value?.value ?: emptyList(),
        batches: List<ItemRelease> = model.batches.value?.value ?: emptyList(),
        isEpisodesShowing: Boolean = this.isEpisodesShowing.value ?: true
    ) = adapter.apply {
        reset(if (isEpisodesShowing) episodes else batches)
        filter.filter("")
    }

    private fun onChanged(t: RepositoryResult<List<ItemRelease>>?) {
        when(t?.status) {
            null -> context.toast("Internal app error!!!")
            RepositoryResult.SUCCESS -> {
                t.value?.let { onSetReleases(episodes = it) } ?: onSetReleases()
                onSetLoading(false)
            }
            RepositoryResult.LOADING -> onSetLoading(true)
            RepositoryResult.FAILURE -> {
                context.toast(t.message)
                onSetLoading(false)
            }
        }
    }

    private fun onSetLoading(b: Boolean) {
        if (b) {
            onSetLoading(false)
        }
    }

    override fun onDestroyView() {
        model.stopServerCall
        model.episodes.removeObservers(viewLifecycleOwner)
        isEpisodesShowing.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
        onSetLoading(false)
        super.onDestroyView()
    }

    override fun onMenuItemClickListener(item: MenuItem) {
        when(item.itemId) {
            R.id.info -> {
                model.detail.value?.value?.let {
                    when {
                        it.someEpisodesTimeStamp == null -> model.onLoadMoreEpisodes
                        it.allEpisodesTimeStamp == null -> model.onLoadAllEpisodes
                    }
                }
            }
            R.id.toggle -> {
                isEpisodesShowing.value = !(isEpisodesShowing.value ?: false)
            }
            R.id.refresh -> {
                if (isEpisodesShowing.value == false)
                    model.refreshBatches else model.refreshReleases
            }
            else -> Unit
        }
    }

    override fun onQueryChanged(text: String?) {
        adapter.filter.filter(text)
    }

    override fun onItemClick(view: View, t: ItemRelease?, position: Int) {
        t?.let {
            model.liveSharedItem.value = it
            ReleaseViewFragment.get().show(childFragmentManager)
        }
    }
}