package info.horriblesubs.sher.ui.main.bookmarked

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
import info.horriblesubs.sher.adapter.BookmarkAdapter
import info.horriblesubs.sher.adapter.BookmarkClick
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.*
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.dialog.InfoDialog
import info.horriblesubs.sher.dialog.LoadingDialog
import info.horriblesubs.sher.ui.show.Show

class Bookmarked: Fragment(), LoadingListener, Observer<List<ItemShow>?>, BookmarkClick<ItemShow>,
    PopupMenu.OnMenuItemClickListener, SearchListener, ErrorListener {

    private val adapter: BookmarkAdapter = BookmarkAdapter(this)
    private var smHandler: SearchMenuHandler? = null
    private var recyclerView: RecyclerView? = null
    private var eHandler: ErrorHandler? = null
    private var dialog: LoadingDialog? = null
    private var vm: BookmarkedVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(activity as AppCompatActivity).get(BookmarkedVM::class.java)
        vm?.initialize(this)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._b_fragment_2, inflater)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        GoogleAds.BANNER.ad(view)
        eHandler = ErrorHandler(this, view)
        recyclerView = view.findViewById(R.id.recyclerView)
        smHandler = SearchMenuHandler(this, this, view, R.menu.menu_b)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView?.layoutManager = StaggeredGridLayoutManager(Constants.orientation(3, 5), StaggeredGridLayoutManager.VERTICAL)
        vm?.result?.observe(viewLifecycleOwner, this)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        vm?.refresh()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val info: InfoDialog? = context?.let{ InfoDialog(it) }
        when(item?.itemId) {
            R.id.info -> info?.show<Any>(Info.BOOKMARKS, null)
            R.id.toggle -> adapter.toggle()
            R.id.refresh -> vm?.refresh()
            else -> return false
        }
        return true
    }

    override fun onChanged(items: List<ItemShow>?) {
        when {
            items.isNullOrEmpty() -> eHandler?.show()
            else -> {
                adapter.reset(items.toMutableList())
                eHandler?.hide()
            }
        }
    }

    override fun onDelete(e: ItemShow?) {
        DataAccess.autoBookmark(e)
        this.adapter.pop(e)
    }

    override fun onClick(e: ItemShow?) {
        if (e == null) return
        val intent = Intent(activity, Show::class.java)
        intent.putExtra("link", e.link)
        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        vm?.stop()
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
}