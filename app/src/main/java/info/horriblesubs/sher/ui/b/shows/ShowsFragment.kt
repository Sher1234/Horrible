package info.horriblesubs.sher.ui.b.shows

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.MediaObjectAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_4.view.*

class ShowsFragment: BaseFragment(), OnItemClickListener<ItemList>,
    Toolbar.OnToolbarActionListener {

    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private var infoDialog: InfoDialog? = null

    val model by viewModels<ShowsModel>({requireActivity()})
    private val adapter = MediaObjectAdapter(this)
    override val layoutId = R.layout.b_fragment_4
    override val name = "shows"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        errorDialog = context?.let{ NetworkErrorDialog(it) }
        infoDialog = context?.let{ InfoDialog(it) }

        view?.recyclerView?.apply {
            adapter = this@ShowsFragment.adapter
            setHasFixedSize(true)
            columnWidth = 1.25f
        }
        view?.toolbar?.apply {
            onToolbarActionListener = this@ShowsFragment
            customizeMenu = {
                it?.findItem(R.id.info)?.title = getString(R.string.about_shows)
                it?.findItem(R.id.toggle)?.title = getString(
                    if (model.allShowing.value == true)
                        R.string.current_season else R.string.all_shows)
            }
            runCustomizeMenu
        }
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

    override fun onResume() {
        super.onResume()
        model.allShowing.observe(viewLifecycleOwner, Observer {
            view?.toolbar?.runCustomizeMenu
            onSetShows(isAll = it ?: false)
        })
        model.resourceCurrent.observe(viewLifecycleOwner, Observer {
            onChanged(it)
        })
    }

    private fun onSetShows(
        current: List<ItemList> = model.resourceCurrent.value?.value ?: emptyList(),
        all: List<ItemList> = model.resourceAll.value?.value ?: emptyList(),
        isAll: Boolean = model.allShowing.value ?: false
    ) = adapter.apply {
        reset(if (isAll) all else current)
        filter.filter("")
    }

    private fun onChanged(t: RepositoryResult<List<ItemList>>?) {
        when(t?.status) {
            null -> Toast.makeText(context, "Internal App Error !!!", Toast.LENGTH_SHORT).show()
            RepositoryResult.SUCCESS -> {
                t.value?.let { onSetShows(current = it) } ?: onSetShows()
                onSetLoading(false)
            }
            RepositoryResult.LOADING -> onSetLoading(true)
            RepositoryResult.FAILURE -> {
                errorDialog?.show("https://horriblesubs.info/release-schedule")
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                onSetLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        model.stopServerCall
        model.allShowing.removeObservers(viewLifecycleOwner)
        model.resourceCurrent.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
        onSetLoading(false)
        super.onDestroyView()
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        infoDialog?.dismiss()
    }

    override fun onMenuItemClickListener(item: MenuItem) = when(item.itemId) {
        R.id.settings -> {
            (activity as? MainActivity)?.onNavigationItemSelected(item)
            Unit
        }
        R.id.toggle -> {
            model.allShowing.value = !(model.allShowing.value ?: false)
        }
        R.id.refresh -> {
            model.refreshDataFromServer
        }
        R.id.info -> {
            infoDialog?.show(Info.SHOWS, model.resourceTime.value) ?: Unit
        }
        else -> Unit
    }

    override fun onQueryChanged(text: String?) {
        adapter.filter.filter(text)
    }

    override fun onItemClick(view: View, t: ItemList?, position: Int) {
        t?.link?.let { ShowActivity.startShowActivity(context, it) }
    }
}