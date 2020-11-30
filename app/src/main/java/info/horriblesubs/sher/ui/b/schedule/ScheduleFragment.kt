package info.horriblesubs.sher.ui.b.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemSchedule
import info.horriblesubs.sher.databinding.BFragment3Binding
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewBindings
import info.horriblesubs.sher.ui.viewModels
import java.util.*

class ScheduleFragment: Fragment(), OnItemClickListener<ItemSchedule.Show>,
    Toolbar.OnMenuItemClickListener {

    private val adapter by lazy { SchedulePagerAdapter(this, binding.viewPager, binding.tabLayout) }
    val model by viewModels<ScheduleModel>({requireActivity()})
    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var binding: BFragment3Binding
    private var infoDialog: InfoDialog? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_3, group)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.menu?.findItem(R.id.info)?.title = getString(R.string.about_weekly_schedule)
        model.resource.observe(viewLifecycleOwner) { onChanged(it) }
        errorDialog = context?.let{ NetworkErrorDialog(it) }
        binding.toolbar.setOnMenuItemClickListener(this)
        infoDialog = context?.let{ InfoDialog(it) }
    }

    private fun onChanged(t: RepositoryResult<ItemSchedule>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> {
            adapter.result = t.value
            onSetLoading(false)
        }
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Unexpected error occurred!!!")
    }

    private fun onSetError(str: String) {
        context.toast(str)
        onSetLoading(false)
        errorDialog?.show("https://subsplease.org/schedule")
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
        binding.viewPager.currentItem = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    }

    override fun onDestroyView() {
        model.resource.removeObservers(viewLifecycleOwner)
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        infoDialog?.dismiss()
        super.onDestroyView()
        model.stopServerCall
    }

    override fun onMenuItemClick(item: MenuItem?) = when(item?.itemId) {
        R.id.settings -> {
            (activity as? MainActivity)?.onNavigationItemSelected(item)
            true
        }
        R.id.refresh -> {
            model.refreshDataFromServer
            true
        }
        R.id.info -> {
            infoDialog?.show(Info.SCHEDULE, model.resourceTime.value) ?: Unit
            true
        }
        else -> false
    }

    override fun onItemClick(view: View, t: ItemSchedule.Show?, position: Int) {
        t?.page?.let { ShowActivity.startShowActivity(context, it) }
    }
}