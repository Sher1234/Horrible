package info.horriblesubs.sher.ui.b.explore.trending

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.functions.orientation
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.TrendingAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_2_a.view.*

class TrendingFragment: BaseFragment(), OnItemClickListener<ItemList> {

    private val infoDialog by lazy { context?.let{ InfoDialog(it) } }
    val model by viewModels<TrendingModel>({requireActivity()})
    private val adapter = TrendingAdapter(this)
    override val layoutId = R.layout.b_fragment_2_a
    override val name = "trending"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.apply {
            setLinearLayoutAdapter(this@TrendingFragment.adapter, orientation(HORIZONTAL, VERTICAL))
            setHasFixedSize(true)
        }
        view?.titleTextView?.text = ("Trending this season")
        view?.buttonA?.setOnClickListener {
            infoDialog?.show(Info.TRENDING)
        }
        view?.buttonB?.setOnClickListener{
            view?.buttonB?.text = adapter.toggleLimiter.toString()
        }
        view?.buttonC?.setOnClickListener{
            model.refreshDataFromServer
        }
        model.resource.observe(viewLifecycleOwner) {
            onChanged(it)
        }
    }

    private fun onChanged(t: RepositoryResult<List<ItemList>>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> onSetData(t.value)
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Internal App Error !!!")
    }

    private fun onSetData(items: List<ItemList>?) {
        onSetLoading(false)
        adapter.reset(items)
        view?.buttonB?.text = adapter.itemCount.toString()
    }

    private fun onSetLoading(b: Boolean) {
        view?.buttonB?.text = 0.toString()
        if (b) {
            view?.progressBar?.visible
            view?.buttonC?.invisible
            view?.buttonC?.disable
        } else {
            view?.progressBar?.gone
            view?.buttonC?.visible
            view?.buttonC?.enable
        }
    }

    private fun onSetError(str: String) {
        view?.buttonB?.text = adapter.itemCount.toString()
        onSetLoading(false)
        context.toast(str)
    }

    override fun onDestroyView() {
        model.resource.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
        model.stopServerCall
        super.onDestroyView()
        infoDialog?.dismiss()
    }

    override fun onItemClick(view: View, t: ItemList?, position: Int) {
        t?.link?.let { ShowActivity.startShowActivity(context, it) }
    }
}