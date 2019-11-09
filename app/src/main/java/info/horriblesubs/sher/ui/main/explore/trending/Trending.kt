package info.horriblesubs.sher.ui.main.explore.trending

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.adapter.TrendingAdapter
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.Info
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.dialog.InfoDialog
import info.horriblesubs.sher.ui.show.Show

class Trending: Fragment(), Observer<List<ItemShow>?>, ItemClick<ItemShow>, OnMenuItemClickListener {

    private val adapter = TrendingAdapter(this)
    private var recyclerView: RecyclerView? = null
    private var vm: TrendingVM? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._b_fragment_1_a, inflater)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        recyclerView = view.findViewById(R.id.recyclerView)
        val info: InfoDialog? = context?.let{InfoDialog(it)}
        view.findViewById<View>(R.id.button).setOnClickListener{info?.show<Any>(Info.TRENDING, null)}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(activity as AppCompatActivity).get(TrendingVM::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        vm?.result?.observe(viewLifecycleOwner, this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        vm?.refresh()
    }

    override fun onChanged(items: List<ItemShow>?) {
        when {
            items.isNullOrEmpty() -> recyclerView?.visibility = View.GONE
            else -> {
                recyclerView?.visibility = View.VISIBLE
                adapter.reset(items.toMutableList())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.stop()
    }

    override fun onClick(e: ItemShow?) {
        if (e == null) return
        val intent = Intent(activity, Show::class.java)
        intent.putExtra("link", e.link)
        startActivity(intent)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.refresh -> vm?.refresh(true)
            R.id.toggle_a -> adapter.toggle()
            else -> return false
        }
        return true
    }
}