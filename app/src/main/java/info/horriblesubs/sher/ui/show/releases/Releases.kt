package info.horriblesubs.sher.ui.show.releases

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.adapter.ReleaseAdapter
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.*
import info.horriblesubs.sher.ui.show.Show
import info.horriblesubs.sher.ui.show.ShowVM

class Releases: Fragment(), Observer<ItemShow?>, ItemClick<Release>,
    PopupMenu.OnMenuItemClickListener, SearchListener, ErrorListener {

    private var smHandler: SearchMenuHandler? = null
    private var adapter = ReleaseAdapter(this)
    private var recyclerView: RecyclerView? = null
    private var eHandler: ErrorHandler? = null
    private var model: ShowVM? = null
    private var isAll: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_2, inflater)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        GoogleAds.BANNER.ad(view)
        eHandler = ErrorHandler(this, view)
        recyclerView = view.findViewById(R.id.recyclerView)
        smHandler = SearchMenuHandler(this, this, view, R.menu.menu_f, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model?.result?.observe(viewLifecycleOwner, this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
    }

    override fun onChanged(t: ItemShow?) {
        when (t) {
            null -> eHandler?.show()
            else -> adapterReset(t)
        }
    }

    private fun adapterReset(t: ItemShow? = model?.result?.value, isAll: Int = this.isAll) {
        val list: MutableList<Release> = mutableListOf()
        var span = Constants.orientation(4, 6)
        this.isAll = isAll
        when(isAll) {
            -1 -> {
                t?.batches?.forEach {
                    it.batch = true
                    list.add(it)
                }
                span = Constants.orientation(3, 5)
            }
            0 -> {
                t?.batches?.forEach {
                    it.batch = true
                    list.add(it)
                }
                t?.episodes?.let {list.addAll(it)}
                span = if(t?.batches.isNullOrEmpty()) 4 else Constants.orientation(3, 5)
            }
            1 -> {
                span = Constants.orientation(4, 6)
                t?.episodes?.let{list.addAll(it)}
            }
        }
        recyclerView?.layoutManager = GridLayoutManager(context, span)
        if (list.isEmpty()) eHandler?.show()
        else eHandler?.hide()
        adapter.reset(list)
    }

    override fun onClick(e: Release?) {
        if (e != null) (activity as Show?)?.viewReleases(e)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.episodes -> {
                adapterReset(isAll = 1)
                true
            }
            R.id.batches -> {
                adapterReset(isAll = -1)
                true
            }
            R.id.all -> {
                adapterReset(isAll = 0)
                true
            }
            else -> false
        }
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