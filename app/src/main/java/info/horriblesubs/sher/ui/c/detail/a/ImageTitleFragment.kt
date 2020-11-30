package info.horriblesubs.sher.ui.c.detail.a

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.onBookmarkChange
import info.horriblesubs.sher.data.database.toBookmarkedShow
import info.horriblesubs.sher.data.subsplease.LibraryRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemShow
import info.horriblesubs.sher.data.subsplease.api.model.imageUrl
import info.horriblesubs.sher.databinding.CFragment1ABinding
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.c.releases.ReleasesFragment

class ImageTitleFragment: Fragment(), Observer<List<BookmarkedShow>> {

    private val model by viewModels<ShowModel>({requireActivity()})
    private lateinit var binding: CFragment1ABinding

    private var liveBookmark: LiveData<List<BookmarkedShow>> = MutableLiveData()
        set(value) {
            field.removeObserver(this)
            field = value
            field.observe(viewLifecycleOwner, this)
        }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.c_fragment_1_a, group)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.detail.observe(viewLifecycleOwner) { onChanged(it) }
    }

    private fun onSetDetail(t: ItemShow?) {
        if (t?.title.isNullOrBlank() && model.showLink.isNullOrBlank())
            context.toast("Undefined URL!")
        Glide.with(requireContext()).load(t?.imageUrl).transform().apply {
            transform(RoundedCorners(10), FitCenter())
            placeholder(R.drawable.app_placeholder)
            timeout(30000)
        }.into(binding.imageView, binding.progressBar)
        binding.titleTextView.text = t?.title?.parseAsHtml
        binding.buttonAllReleases.setOnClickListener {
            (activity as? ShowActivity)?.onChangeFragment(ReleasesFragment())
        }

        binding.buttonLibraryToggle.setOnClickListener {
            t?.toBookmarkedShow()?.let { onBookmarkChange(it) }
        }

        binding.buttonRefresh.setOnClickListener { model.refreshDataFromServer }

        binding.buttonOpen.setOnClickListener {
            val link  = t?.url ?: model.showLink
            if(link.isNullOrBlank()) context.toast("Internal app error, Unable to open link..")
            else startBrowser(context,
                if (link.contains("shows")) link else "https://subsplease.org/shows/$link")
        }

        binding.buttonShare.setOnClickListener {
            if(t != null && t.url.isNotBlank()) {
                val link = if (t.url.contains("shows")) t.url else
                    "https://subsplease.org/shows/${t.url}"
                val text = "${t.title}\n${t.synopsis.short}\n\n$link\n\n\nShared via an!me."
                startActivity(Intent.createChooser(
                    Intent().apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                    },
                    "Select Sharing App"
                ))
            } else
                context.toast("Internal app error, Unable to share.")
        }
        t?.toBookmarkedShow()?.let { liveBookmark = LibraryRepository.getByIdLive(it) }
    }

    private fun onChanged(t: RepositoryResult<ItemShow>?) = when(t?.status) {
        RepositoryResult.FAILURE -> context.toast(t.message)
        null -> context.toast("Internal app error!!!")
        RepositoryResult.SUCCESS -> onSetDetail(t.value)
        else -> {}
    }

    override fun onChanged(t: List<BookmarkedShow>?) = binding.buttonLibraryToggle.setIconResource(
        if (t.isNullOrEmpty()) R.drawable.ic_bookmark else R.drawable.ic_bookmarked
    )

    private val String?.short: String get() {
        return if (this.isNullOrBlank()) "" else
            this.substring(0, if (length > 100) 100 else length) + if (length > 100) "..." else ""
    }
}