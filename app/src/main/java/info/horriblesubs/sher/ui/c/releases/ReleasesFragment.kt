package info.horriblesubs.sher.ui.c.releases

import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease
import info.horriblesubs.sher.data.subsplease.api.model.toArrayList
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.horrible.ReleaseAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.c.view.ReleaseViewFragment
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_2.view.*

class ReleasesFragment: BaseFragment(), OnItemClickListener<ItemRelease> {

    val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_2
    private val adapter = ReleaseAdapter(this)
    override val name: String = "show-releases"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView?.apply {
            adapter = this@ReleasesFragment.adapter
            setHasFixedSize(true)
        }
        view.toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.refresh) model.refreshReleases
            true
        }
        view.toolbar?.menu?.findItem(R.id.info)?.isVisible = false
        model.episodes.observe(viewLifecycleOwner) { onChanged(it) }
    }

    private fun onSetReleases(episodes: LinkedHashMap<String, ItemRelease> = model.episodes.value?.value ?: linkedMapOf()) = adapter.apply {
        reset(episodes.toArrayList())
        filter.filter("")
    }

    private fun onChanged(t: RepositoryResult<LinkedHashMap<String, ItemRelease>>?) {
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
        clearFindViewByIdCache()
        onSetLoading(false)
        super.onDestroyView()
    }

    override fun onItemClick(view: View, t: ItemRelease?, position: Int) {
        t?.let {
            model.liveSharedItem.value = it
            ReleaseViewFragment.get().show(childFragmentManager)
        }
    }
}