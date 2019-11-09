package info.horriblesubs.sher.ui.show.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.DownloadAdapter
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.common.load
import info.horriblesubs.sher.ui.show.ShowVM
import info.horriblesubs.sher.ui.show.detail.a.text

class ReleaseView : Fragment(), TabLayout.OnTabSelectedListener, ItemClick<String> {
    
    private val adapter = DownloadAdapter(this)
    private var imageView: AppCompatImageView? = null
    private var textView1: AppCompatTextView? = null
    private var textView2: AppCompatTextView? = null
    private var textView3: AppCompatTextView? = null
    private var textView4: AppCompatTextView? = null
    private var textView5: AppCompatTextView? = null
    private var textView6: AppCompatTextView? = null
    private var model: ShowVM? = null
    private var position: Int = 0

    private val releaseObserver = Observer<Release?> {
        textView5?.text = it?.release.fromHtml()
        adapterReset(release = it)
    }
    private val showObserver = Observer<ItemShow?> {
        textView3?.text = if(it?.ongoing == true) "Currently Airing" else "Airing Completed"
        textView1?.text = it?.title.fromHtml()
        textView2?.text = it?.schedule.text()
        textView4?.text = it?.timePassed()
        imageView?.load(it?.image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_3, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)
        textView6 = view.findViewById(R.id.textView6)
        textView5 = view.findViewById(R.id.textView5)
        textView4 = view.findViewById(R.id.textView4)
        textView3 = view.findViewById(R.id.textView3)
        textView2 = view.findViewById(R.id.textView2)
        textView1 = view.findViewById(R.id.textView1)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.hq).setIcon(R.drawable.ic_hq))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.hd).setIcon(R.drawable.ic_hd))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.fhd).setIcon(R.drawable.ic_fhd))
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        model?.release?.observe(viewLifecycleOwner, releaseObserver)
        model?.result?.observe(viewLifecycleOwner, showObserver)
        recyclerView.itemAnimator = DefaultItemAnimator()
        tabLayout.addOnTabSelectedListener(this)
        recyclerView.adapter = adapter
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabSelected(tab: TabLayout.Tab?) {
        position = tab?.position?:position
        textView6?.text = tab?.text
        when(position) {
            0 -> adapterReset(0)
            1 -> adapterReset(1)
            2 -> adapterReset(2)
        }
    }

    private fun adapterReset(position: Int = this.position, release: Release? = model?.release?.value) {
        val map: Map<String, String?>? = release?.downloads?.get(position)
        this.position = position
        adapter.downloads = map
    }

    override fun onClick(e: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(e)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Service unavailable", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}