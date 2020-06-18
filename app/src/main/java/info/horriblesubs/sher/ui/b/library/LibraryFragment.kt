package info.horriblesubs.sher.ui.b.library

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.onBookmarkChange
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui._extras.adapters.MediaObjectAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.viewModels
import info.horriblesubs.sher.ui.visible
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_1.view.*


class LibraryFragment: BaseFragment(), OnItemClickListener<BookmarkedShow>,
    Toolbar.OnToolbarActionListener {

    private val infoDialog by lazy { context?.let{ InfoDialog(it) } }
    val model by viewModels<LibraryModel>({requireActivity()})
    private val adapter = MediaObjectAdapter(this)
    override val layoutId: Int = R.layout.b_fragment_1
    override val name: String = "library"

    override fun <T : AppCompatActivity> onBackPressed(t: T): Boolean {
        return if (model.isDeleteEnabled.value == true) {
            model.isDeleteEnabled.value = false
            false
        } else true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.apply {
            adapter = this@LibraryFragment.adapter
            setHasFixedSize(true)
            columnWidth = 1.25f
        }
        view?.toolbar?.apply {
            customizeMenu = {
                it?.findItem(R.id.info)?.title = getString(R.string.about_library)
                it?.findItem(R.id.toggle)?.title = getString(
                    if (model.isDeleteEnabled.value == true)
                        R.string.close_delete else R.string.delete_bookmarks)
                it?.findItem(R.id.refresh)?.let { item ->
                    it.removeItem(item.itemId)
                }
            }
            onToolbarActionListener = this@LibraryFragment
            inflateMenu(R.menu.menu_a)
        }
        model.resource.observe(viewLifecycleOwner) {
            adapter.apply {
                reset(it)
                filter.filter("")
            }
        }
        model.isDeleteEnabled.observe(viewLifecycleOwner) {
            view?.messageTextView?.apply {
                if (it == true) visible else gone
                view?.toolbar?.runCustomizeMenu
                setText(R.string.delete_help)
            }
        }
    }

    override fun onDestroyView() {
        model.resource.removeObservers(viewLifecycleOwner)
        clearFindViewByIdCache()
        super.onDestroyView()
    }

    override fun onItemClick(view: View, t: BookmarkedShow?, position: Int) {
        if (t == null) return
        if (model.isDeleteEnabled.value == true) onBookmarkChange(t)
        else ShowActivity.startShowActivity(context, t.link)
    }

    override fun onMenuItemClickListener(item: MenuItem) = when(item.itemId) {
        R.id.toggle -> {
            model.isDeleteEnabled.value = !(model.isDeleteEnabled.value ?: false)
        }
        R.id.settings -> {
            (activity as? MainActivity)?.onNavigationItemSelected(item)
            Unit
        }
        R.id.info -> {
            infoDialog?.show(Info.BOOKMARKS) ?: Unit
        }
        else -> Unit
    }

    override fun onQueryChanged(text: String?) {
        adapter.filter.filter(text)
    }
}