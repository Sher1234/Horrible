package info.horriblesubs.sher.ui.main.explore.recent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.adapter.RecentAdapter
import info.horriblesubs.sher.api.horrible.model.ItemRecent
import info.horriblesubs.sher.api.horrible.result.Result
import info.horriblesubs.sher.common.Info
import info.horriblesubs.sher.common.LoadingListener
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.dialog.InfoDialog
import info.horriblesubs.sher.dialog.LoadingDialog
import info.horriblesubs.sher.ui.show.Show

class Recent: Fragment(), LoadingListener, Observer<Result<ItemRecent>?>, ItemClick<ItemRecent>,
    PopupMenu.OnMenuItemClickListener {

    private val adapter: RecentAdapter = RecentAdapter(this)
    private var textView: AppCompatTextView? = null
    private var recyclerView: RecyclerView? = null
    private var dialog: LoadingDialog? = null
    private var vm: RecentVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(activity as AppCompatActivity).get(RecentVM::class.java)
        vm?.initialize(this)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._b_fragment_1_c, inflater)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        textView = view.findViewById(R.id.textView2)
        recyclerView = view.findViewById(R.id.recyclerView)
        val info:InfoDialog? = context?.let{InfoDialog(it)}
        view.findViewById<View>(R.id.button).setOnClickListener{
            info?.show(Info.RECENT, vm?.result?.value)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.time?.observe(viewLifecycleOwner, Observer {textView?.text = it})
        vm?.result?.observe(viewLifecycleOwner, this)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        vm?.refresh()
    }

    override fun onChanged(result: Result<ItemRecent>?) {
        when {
            result == null || result.items.isNullOrEmpty() -> {
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                recyclerView?.visibility = View.GONE
                textView?.text = ("Never")
            }
            else -> {
                val list = result.items.toMutableList()
                recyclerView?.visibility = View.VISIBLE
                DataAccess.resetNotify(list)
                adapter.reset(list)
            }
        }
    }

    override fun stop() {
        dialog?.dismiss()
        dialog = null
    }

    override fun start() {
        if(dialog == null)
            dialog = context?.let{LoadingDialog(it)}
        dialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.stop()
        stop()
    }

    override fun onClick(e: ItemRecent?) {
        if (e == null) return
        val intent = Intent(activity, Show::class.java)
        intent.putExtra("link", e.link)
        startActivity(intent)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.toggle_b -> {
                adapter.toggle()
                true
            }
            R.id.refresh -> {
                vm?.refresh(true)
                true
            }
            else -> false
        }
    }
}