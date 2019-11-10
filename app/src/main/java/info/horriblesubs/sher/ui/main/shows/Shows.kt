package info.horriblesubs.sher.ui.main.shows

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.adapter.ShowsAdapter
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.result.ResultShows
import info.horriblesubs.sher.api.horrible.result.isNull
import info.horriblesubs.sher.common.*
import info.horriblesubs.sher.dialog.InfoDialog
import info.horriblesubs.sher.dialog.LoadingDialog
import info.horriblesubs.sher.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.show.Show

class Shows: Fragment(), LoadingListener, Observer<ResultShows?>, ItemClick<ItemShow>,
    PopupMenu.OnMenuItemClickListener, SearchListener, ErrorListener {

    private var errorDialog: NetworkErrorDialog? = null
    private var smHandler: SearchMenuHandler? = null
    private val adapter = ShowsAdapter(this)
    private var recyclerView: RecyclerView? = null
    private var eHandler: ErrorHandler? = null
    private var dialog: LoadingDialog? = null
    private lateinit var vm: ShowsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(activity as AppCompatActivity).get(ShowsVM::class.java)
        vm.initialize(this)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout._b_fragment_4, group, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        GoogleAds.BANNER.ad(view)
        eHandler = ErrorHandler(this, view)
        errorDialog = context?.let{NetworkErrorDialog(it)}
        recyclerView = view.findViewById(R.id.recyclerView)
        smHandler = SearchMenuHandler(this, this, view, R.menu.menu_d)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView?.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        vm.allShowing.observe(viewLifecycleOwner, Observer {
            val item = smHandler?.menuHandler?.popupMenu?.menu?.findItem(R.id.toggle)
            item?.title = getString(if(it == true) R.string.current_season else R.string.all_shows)
            toggleShows(b = it?:false)
        })
        vm.result.observe(viewLifecycleOwner, this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        vm.refresh()
    }

    private fun toggleShows(v: ResultShows? = vm.result.value,
                            b: Boolean = vm.allShowing.value?:false) {
        val list = if (b) v?.items else v?.current
        when {
            list.isNullOrEmpty() -> eHandler?.show()
            else -> {
                adapter.reset(list.toMutableList())
                eHandler?.hide()
            }
        }
    }

    override fun onChanged(result: ResultShows?) {
        if (result.isNull())
            errorDialog?.show("https://horriblesubs.info/"
                    + if(vm.allShowing.value == true)"shows" else "current-season")
        toggleShows(v = result)
    }

    override fun onDestroy() {
        errorDialog?.dismiss()
        super.onDestroy()
        vm.stop()
        stop()
    }

    override fun start() {
        if(dialog == null)
            dialog = context?.let{LoadingDialog(it)}
        dialog?.show()
    }

    override fun stop() {
        dialog?.dismiss()
        dialog = null
    }

    override fun onClick(e: ItemShow?) {
        if (e == null) return
        val intent = Intent(activity, Show::class.java)
        intent.putExtra("link", e.link)
        startActivity(intent)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val info: InfoDialog? = context?.let{InfoDialog(it)}
        when(item?.itemId) {
            R.id.info -> info?.show(Info.SHOWS, vm.result.value)
            R.id.refresh -> vm.refresh(true)
            R.id.toggle -> vm.toggle()
            else -> return false
        }
        return true
    }

    override fun search(s: String?) {
        adapter.search(s)
    }

    override fun errorVisible() {
        recyclerView?.visibility = View.GONE
    }

    override fun errorHidden() {
        recyclerView?.visibility = View.VISIBLE
    }
}