package info.horriblesubs.sher.ui.b.explore.latest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.model.ItemLatest
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.LatestAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_2_c.view.*

class LatestFragment: BaseFragment(), OnItemClickListener<ItemLatest> {

    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private var infoDialog: InfoDialog? = null

    val model by viewModels<LatestModel>({requireActivity()})
    private val adapter = LatestAdapter(this)
    override val layoutId = R.layout.b_fragment_2_c
    override val name = "latest"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        errorDialog = context?.let{ NetworkErrorDialog(it) }
        infoDialog = context?.let{ InfoDialog(it) }

        view?.recyclerView?.setLinearLayoutAdapter(adapter)
        view?.titleTextView?.text = ("Latest Releases")
        view?.buttonA?.setOnClickListener {
            infoDialog?.show(Info.LATEST, model.resourceTime.value)
        }
        view?.buttonB?.setOnClickListener {
            view?.buttonB?.text = adapter.toggleLimiter.toString()
        }
        view?.buttonC?.setOnClickListener {
            model.refreshDataFromServer
        }
        model.resourceTimeLive.observe(viewLifecycleOwner, Observer {
            view?.subtitleTextView?.text = it ?: "Never"
        })
        model.resource.observe(viewLifecycleOwner, Observer { onChanged(it) })
    }

    override fun onDestroyView() {
        model.killTimer
        model.stopServerCall
        model.resource.removeObservers(viewLifecycleOwner)
        model.resourceTimeLive.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
        model.stopServerCall
        super.onDestroyView()
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        infoDialog?.dismiss()
    }

    private fun onChanged(t: RepositoryResult<List<ItemLatest>>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> onSetData(t.value)
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Unexpected error occurred!!!")
    }

    private fun onSetError(str: String) {
        onSetLoading(false)
        view?.buttonB?.text = adapter.itemCount.toString()
        errorDialog?.show("https://horriblesubs.info")
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }

    private fun onSetData(items: List<ItemLatest>?) {
        onSetLoading(false)
        adapter.reset(items)
        view?.buttonB?.text = adapter.itemCount.toString()
    }

    private fun onSetLoading(b: Boolean) {
        view?.buttonB?.text = 0.toString()
        loadingDialog = if (b) {
            onSetLoading(false)
            view?.progressBar?.visible
            view?.buttonC?.invisible
            view?.buttonC?.disable
            context?.let {
                LoadingDialog(it).apply {
                    show()
                }
            }
        } else {
            view?.progressBar?.gone
            view?.buttonC?.visible
            view?.buttonC?.enable
            loadingDialog?.dismiss()
            null
        }
    }

    override fun onItemClick(view: View, t: ItemLatest?, position: Int) {
        t?.link?.let { ShowActivity.startShowActivity(context, it) }
    }
}