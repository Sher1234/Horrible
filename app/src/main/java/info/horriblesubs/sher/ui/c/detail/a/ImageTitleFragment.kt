package info.horriblesubs.sher.ui.c.detail.a

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import info.horriblesubs.sher.data.horrible.LibraryRepository
import info.horriblesubs.sher.data.horrible.api.detailAgo
import info.horriblesubs.sher.data.horrible.api.detailTimeStamp
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemShow
import info.horriblesubs.sher.data.horrible.api.shortDisplay
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.c.ShowActivity
import info.horriblesubs.sher.ui.c.ShowModel
import info.horriblesubs.sher.ui.c.releases.ReleasesFragment
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_1_a.view.*

class ImageTitleFragment: BaseFragment(), Observer<List<BookmarkedShow>> {

    private val model by viewModels<ShowModel>({requireActivity()})
    override val layoutId: Int = R.layout.c_fragment_1_a
    override val name: String = "show-detail"

    private var liveBookmark: LiveData<List<BookmarkedShow>> = MutableLiveData()
        set(value) {
            field.removeObserver(this)
            field = value
            field.observe(viewLifecycleOwner, this)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.detail.observe(viewLifecycleOwner, Observer {
            onChanged(it)
        })
    }

    private fun onSetDetail(t: ItemShow?) {
        if (t?.title.isNullOrBlank()) {
            if (model.showLink.isNullOrBlank())
                Toast.makeText(context, "Undefined URL", Toast.LENGTH_SHORT).show()
        }
        context?.let {
            Glide.with(it).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(6), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(view?.imageView, view?.progressBar)
        }
        view?.airingStatusText?.text = if(t?.ongoing == true) "Currently Airing" else "Completed"
        view?.lastUpdatedText?.text = if (TimeLeftPreference.value) t.detailAgo else
            TimeFormatPreference.format(t.detailTimeStamp) ?: "Never"
        view?.titleText?.text = t?.title?.parseAsHtml
        view?.scheduleText?.text = t?.shortDisplay

        view?.buttonAllReleases?.setOnClickListener {
            (activity as? ShowActivity)?.onChangeFragment(ReleasesFragment())
        }

        view?.buttonLibraryToggle?.setOnClickListener { _ ->
            t?.toBookmarkedShow()?.let {
                onBookmarkChange(it)
            }
        }

        view?.buttonRefresh?.setOnClickListener {
            model.refreshDataFromServer
        }

        view?.buttonOpen?.setOnClickListener {
            val link  = t?.link ?: model.showLink
            if(link.isNullOrBlank())
                Toast.makeText(context, "Internal app error, Unable to share.", Toast.LENGTH_SHORT).show()
            else
                startBrowser(context, if (link.contains("shows")) link else
                    "https://horriblesubs.info/shows/$link")
        }

        view?.buttonShare?.setOnClickListener {
            if(t != null && t.link.isNotBlank()) {
                val link = if (t.link.contains("shows")) t.link else
                    "https://horriblesubs.info/shows/${t.link}"
                val text = "${t.title}\n${t.body.short}\n\n$link\n\n\nShared via an!me."
                startActivity(Intent.createChooser(
                    Intent().apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                    },
                    "Select Sharing App"
                ))
            } else {
                Toast.makeText(context, "Internal app error, Unable to share.", Toast.LENGTH_SHORT).show()
            }
        }
        t?.toBookmarkedShow()?.let {
            liveBookmark = LibraryRepository.getByIdLive(it)
        }
    }

    private fun onChanged(t: RepositoryResult<ItemShow>?) = when(t?.status) {
        null -> Toast.makeText(context, "Internal App Error !!!", Toast.LENGTH_SHORT).show()
        RepositoryResult.FAILURE -> Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        RepositoryResult.SUCCESS -> onSetDetail(t.value)
        else -> {}
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    override fun onChanged(t: List<BookmarkedShow>?) {
        view?.buttonLibraryToggle?.icon = ContextCompat.getDrawable(
            requireContext(),
            if (t.isNullOrEmpty()) R.drawable.ic_bookmark else R.drawable.ic_bookmarked
        )
    }

    private val String?.short: String get() {
        return if (this.isNullOrBlank()) "" else
            this.substring(0, if (length > 100) 100 else length) + if (length > 100) "..." else ""
    }
}