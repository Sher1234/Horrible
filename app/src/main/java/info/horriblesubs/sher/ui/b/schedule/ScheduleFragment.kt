package info.horriblesubs.sher.ui.b.schedule

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_3.view.*
import java.util.*

class ScheduleFragment: BaseFragment(),
    OnItemClickListener<ItemSchedule>, Toolbar.OnToolbarActionListener {

    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private var infoDialog: InfoDialog? = null

    val model by viewModels<ScheduleModel>({requireActivity()})
    override val layoutId = R.layout.b_fragment_3
    override val name = "schedule"
    private val adapter by lazy {
        SchedulePagerAdapter(this, view?.viewPager, view?.tabLayout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        errorDialog = context?.let{ NetworkErrorDialog(it) }
        infoDialog = context?.let{ InfoDialog(it) }

        view?.toolbar?.apply {
            customizeMenu = {
                it?.findItem(R.id.info)?.title = getString(R.string.about_weekly_schedule)
                it?.findItem(R.id.toggle)?.let { item ->
                    it.removeItem(item.itemId)
                }
            }
            inflateMenu(R.menu.menu_a)
            onToolbarActionListener = this@ScheduleFragment
        }
        model.resource.observe(viewLifecycleOwner, Observer { onChanged(it) })
    }

    private fun onChanged(t: RepositoryResult<List<List<ItemSchedule>>>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> onSetData(t.value)
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Unexpected error occurred!!!")
    }

    private fun onSetError(str: String) {
        onSetLoading(false)
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        errorDialog?.show("https://horriblesubs.info/release-schedule")
    }

    private fun onSetData(items: List<List<ItemSchedule>>?) {
        onSetLoading(false)
        adapter.result = items
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
        view?.viewPager?.currentItem = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    }

    override fun onDestroyView() {
        model.stopServerCall
        model.resource.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
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
        R.id.refresh -> {
            model.refreshDataFromServer
        }
        R.id.info -> {
            infoDialog?.show(Info.SCHEDULE, model.resourceTime.value) ?: Unit
        }
        else -> Unit
    }

    override fun onItemClick(view: View, t: ItemSchedule?, position: Int) {
        t?.link?.let { ShowActivity.startShowActivity(context, it) }
    }
}