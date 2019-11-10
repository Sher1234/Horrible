package info.horriblesubs.sher.ui.show.detail.c

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemStat
import info.horriblesubs.sher.adapter.StatsAdapter
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.show.ShowVM

class C : Fragment(), Observer<ItemShow?> {
    private var recyclerView: RecyclerView? = null
    private val adapter = StatsAdapter()
    private var model: ShowVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1_c, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        model?.result?.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: ItemShow?) {
        adapter.removeAll()
        adapter.add(ItemStat(R.drawable.ic_views, "Views", t?.views.toString()))
        adapter.add(ItemStat(R.drawable.ic_bookmarks, "Bookmarks", t?.favs.toString()))
        adapter.add(ItemStat(R.drawable.ic_rating, "Rating", t?.rating.toString()))
        adapter.add(ItemStat(R.drawable.ic_users, "Users", t?.users.toString()))
        adapter.add(ItemStat(R.drawable.ic_source, "Source", "HorribleSubs"))
        adapter.add(ItemStat(R.drawable.ic_time, "Cache Time", t?.time(Formats.FULL_12H)?:"Never"))
    }
}