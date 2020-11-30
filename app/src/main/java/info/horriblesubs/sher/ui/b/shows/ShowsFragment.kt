package info.horriblesubs.sher.ui.b.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemList
import info.horriblesubs.sher.databinding.BFragment4Binding
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui._extras.adapters.MediaNoImageAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.setLinearLayoutAdapter
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewBindings
import info.horriblesubs.sher.ui.viewModels

class ShowsFragment: Fragment(), OnItemClickListener<ItemList>, Toolbar.OnMenuItemClickListener {

    val model by viewModels<ShowsModel>({requireActivity()})
    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var binding: BFragment4Binding
    private val adapter = MediaNoImageAdapter(this)
    private var infoDialog: InfoDialog? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_4, group)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.resourceShows.observe(viewLifecycleOwner) { onChanged(it) }
        binding.toolbar.menu?.findItem(R.id.info)?.title = "About Shows"
        binding.recyclerView.setLinearLayoutAdapter(adapter)
        errorDialog = context?.let{ NetworkErrorDialog(it) }
        binding.toolbar.setOnMenuItemClickListener(this)
        infoDialog = context?.let{ InfoDialog(it) }
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun onSetLoading(b: Boolean) {
        loadingDialog = if (b) {
            onSetLoading(false)
            context?.let {
                LoadingDialog(it).apply {
                    show()
                }
            }
        } else {
            loadingDialog?.dismiss()
            null
        }
    }

    private fun onSetShows(shows: List<ItemList> = model.resourceShows.value?.value ?: emptyList()) = adapter.apply {
        reset(shows)
        filter.filter("")
    }

    private fun onChanged(t: RepositoryResult<List<ItemList>>?) {
        when(t?.status) {
            null -> context.toast("Internal app error!!!")
            RepositoryResult.SUCCESS -> {
                onSetShows(t.value ?: emptyList())
                onSetLoading(false)
            }
            RepositoryResult.LOADING -> onSetLoading(true)
            RepositoryResult.FAILURE -> {
                errorDialog?.show("https://horriblesubs.info/release-schedule")
                context.toast(t.message)
                onSetLoading(false)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?) = when(item?.itemId) {
        R.id.info -> {
            infoDialog?.show(Info.SHOWS, model.resourceTime.value)
            true
        }
        R.id.refresh -> {
            model.refreshDataFromServer
            true
        }
        else -> false
    }

    override fun onDestroyView() {
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        infoDialog?.dismiss()
        super.onDestroyView()
        model.stopServerCall
        onSetLoading(false)
    }

    override fun onItemClick(view: View, t: ItemList?, position: Int) {
        t?.link?.let { ShowActivity.startShowActivity(context, it) }
    }
}