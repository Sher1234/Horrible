package info.horriblesubs.sher.ui.b.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.onBookmarkChange
import info.horriblesubs.sher.databinding.BFragment1Binding
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.ui._extras.adapters.MediaObjectAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.viewBindings
import info.horriblesubs.sher.ui.viewModels
import info.horriblesubs.sher.ui.visible

class LibraryFragment: Fragment(), OnItemClickListener<BookmarkedShow>,
    Toolbar.OnMenuItemClickListener, OnBackPressedListener {

    private val infoDialog by lazy { context?.let{ InfoDialog(it) } }
    val model by viewModels<LibraryModel>({requireActivity()})
    private val adapter = MediaObjectAdapter(this)

    private lateinit var binding: BFragment1Binding

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_1, group)
        return binding.root
    }

    private fun onRefreshMenu(enabled: Boolean = (model.resource.value?.size ?: 0) > 0) {
        binding.toolbar.menu?.findItem(R.id.info)?.title = getString(R.string.about_library)
        binding.toolbar.menu?.findItem(R.id.toggle)?.let {
            it.title = getString(if (model.toggle) R.string.close_delete else R.string.delete_bookmarks)
            it.setIcon(if (model.toggle) R.drawable.ic_delete_off else R.drawable.ic_delete)
            it.isEnabled = enabled
        }
        binding.toolbar.setOnMenuItemClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            adapter = this@LibraryFragment.adapter
            setHasFixedSize(true)
            columnWidth = 1.45f
        }
        onRefreshMenu()
        model.resource.observe(viewLifecycleOwner) {
            binding.emptyLayout.apply { if (it.isNullOrEmpty()) visible else gone }
            adapter.reset(it)
        }
        model.isDeleteEnabled.observe(viewLifecycleOwner) {
            binding.messageTextView.apply {
                setText(R.string.delete_help)
                if (it == true) visible else gone
                onRefreshMenu()
            }
        }
    }

    override fun onDestroyView() {
        model.resource.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    override fun onItemClick(view: View, t: BookmarkedShow?, position: Int) {
        if (t == null) return
        if (model.toggle) onBookmarkChange(t)
        else ShowActivity.startShowActivity(context, t.url)
    }

    override fun onMenuItemClick(item: MenuItem?) = when(item?.itemId) {
        R.id.toggle -> {
            model.toggle = !(model.toggle)
            true
        }
        R.id.info -> {
            infoDialog?.show(Info.BOOKMARKS)
            true
        }
        else -> false
    }

    override fun <T : AppCompatActivity> onBackPressed(t: T): Boolean {
        return if (model.toggle) {
            model.toggle = false
            false
        } else true
    }
}