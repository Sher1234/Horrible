package info.horriblesubs.sher.ui.b.explore.latest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemLatest
import info.horriblesubs.sher.databinding.BFragment2BBinding
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.LatestAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity

class LatestFragment: Fragment(), OnItemClickListener<ItemLatest> {

    val model by viewModels<LatestModel>({requireActivity()})
    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var binding: BFragment2BBinding
    private var infoDialog: InfoDialog? = null
    private val adapter = LatestAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_2_b, group) {
            title = "Latest Releases"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonA.setOnClickListener { infoDialog?.show(Info.LATEST, model.resourceTime.value) }
        binding.buttonB.setOnClickListener { model.refreshDataFromServer }
        errorDialog = context?.let{ NetworkErrorDialog(it) }
        binding.recyclerView.setLinearLayoutAdapter(adapter)
        infoDialog = context?.let{ InfoDialog(it) }
        model.resource.observe(viewLifecycleOwner) { onChanged(it) }
        model.resourceTimeLive.observe(viewLifecycleOwner) { binding.subtitleTextView.text = it ?: "Never" }
    }

    override fun onDestroyView() {
        model.resourceTimeLive.removeObservers(viewLifecycleOwner)
        model.resource.removeObservers(viewLifecycleOwner)
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        super.onDestroyView()
        infoDialog?.dismiss()
        model.stopServerCall
        model.stopServerCall
        model.killTimer
    }

    private fun onChanged(t: RepositoryResult<LinkedHashMap<String, ItemLatest>>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> {
            adapter.reset(t.value?.values?.toMutableList())
            onSetLoading(false)
        }
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Unexpected error occurred!!!")
    }

    private fun onSetError(str: String) {
        onSetLoading(false)
        errorDialog?.show("https://subsplease.org")
        context.toast(str)
    }

    private fun onSetLoading(b: Boolean) { loadingDialog = if (b) startLoading else stopLoading }

    private val stopLoading: LoadingDialog? get() = binding.let {
        loadingDialog?.dismiss()
        it.progressBar.gone
        it.buttonB.visible
        it.buttonB.enable
        return null
    }

    private val startLoading: LoadingDialog? get() = binding.let {
        it.progressBar.visible
        it.buttonB.invisible
        it.buttonB.disable
        stopLoading
        context?.let { LoadingDialog(it).apply { show() } }
    }

    override fun onItemClick(view: View, t: ItemLatest?, position: Int) {
        t?.page?.let { ShowActivity.startShowActivity(context, it) }
    }
}