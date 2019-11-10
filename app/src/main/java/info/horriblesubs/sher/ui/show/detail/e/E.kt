package info.horriblesubs.sher.ui.show.detail.e

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
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
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.show.Show
import info.horriblesubs.sher.ui.show.ShowVM

class E : Fragment(), ItemClick<Release>, Observer<ItemShow?> {
    private val adapter = ReleaseAdapter(this)
    private var textView: AppCompatTextView? = null
    private var recyclerView: RecyclerView? = null
    private var model: ShowVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1_e, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.textView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = GridLayoutManager(context, 4)
        model?.result?.observe(viewLifecycleOwner, this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
    }

    override fun onClick(e: Release?) {
        if (e != null) (activity as Show?)?.viewReleases(e)
    }

    override fun onChanged(t: ItemShow?) {
        when {
            t == null || (t.episodes.isNullOrEmpty()) && (t.batches.isNullOrEmpty()) -> {
                recyclerView?.visibility = View.GONE
                textView?.visibility = View.VISIBLE
            }
            else -> {
                val size = if (!t.batches.isNullOrEmpty()) 3 else 4
                var list: MutableList<Release> = mutableListOf()
                t.batches?.forEach {
                    it.batch = true
                    list.add(it)
                }
                t.episodes?.let {list.addAll(it)}
                (recyclerView?.layoutManager as GridLayoutManager?)?.spanCount = size
                list = list.subList(0, if(list.size > size) size else list.size)
                recyclerView?.visibility = View.VISIBLE
                textView?.visibility = View.GONE
                adapter.reset(list)
            }
        }
    }
}