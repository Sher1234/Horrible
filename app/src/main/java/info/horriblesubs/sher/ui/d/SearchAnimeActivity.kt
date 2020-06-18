package info.horriblesubs.sher.ui.d

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimeSearchItem
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.EXTRA_DATA
import info.horriblesubs.sher.ui._extras.adapters.mal.SearchAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.d_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
class SearchAnimeActivity: AppCompatActivity(), Toolbar.OnToolbarActionListener,
    OnItemClickListener<AnimeSearchItem> {

    private val viewModel by viewModels<SearchAnimeModel>()
    private val adapter =
        SearchAdapter(this)
    private var loadingDialog: LoadingDialog? = null

    @FlowPreview
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.d_activity)

        recyclerView?.apply {
            adapter = this@SearchAnimeActivity.adapter
            val layoutManager = layoutManager as GridLayoutManager
            recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.listScrolled(
                        layoutManager.childCount, //visibleItemCount
                        layoutManager.findLastVisibleItemPosition(), //lastVisibleItem
                        layoutManager.itemCount //totalItemCount
                    )
                }
            })
            setHasFixedSize(true)
            columnWidth = 1.25f
        }
        viewModel.repoResult.observe(this) {
            onSetLoading(it?.status == RepositoryResult.LOADING)
            when (it?.status) {
                RepositoryResult.FAILURE -> toast("\uD83D\uDE28 Wooops ${it.message}")
                null -> toast("\uD83D\uDE28 Wooops, internal app error.")
                RepositoryResult.SUCCESS -> adapter.submitList(it.value)
            }
        }
        val query = viewModel.searchObject?.query ?: intent.getStringExtra(EXTRA_DATA) ?: ""
        if (viewModel.repoResult.value == null) viewModel.onSearch(query)
        toolbar?.onToolbarActionListener = this
        toolbar?.query = query
        toolbar?.openSearch
    }

    override fun onQueryUpdated(text: String?) {
        if (text.isNullOrBlank()) return
        recyclerView?.scrollToPosition(0)
        viewModel.onSearch(text)
    }

    override fun onItemClick(view: View, t: AnimeSearchItem?, position: Int) {
        t?.url?.let { startBrowser(this, it) }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        loadingDialog?.dismiss()
        super.onDestroy()
    }

    private fun onSetLoading(b: Boolean) {
        loadingDialog = if (b) {
            onSetLoading(false)
            LoadingDialog(this).apply { show() }
        } else {
            loadingDialog?.dismiss()
            null
        }
    }
}