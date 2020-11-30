package info.horriblesubs.sher.ui.c.view

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.FHD
import info.horriblesubs.sher.data.subsplease.api.model.HD
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease
import info.horriblesubs.sher.data.subsplease.api.model.SD
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.DownloadAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.c_fragment_3.view.*
import java.time.ZonedDateTime

class ReleaseViewFragment: BottomSheetDialogFragment(), OnItemClickListener<String> {

    private val model by viewModels<ShowModel>({requireActivity()})
    private val titles = arrayListOf("Magnet", "Torrent", "XDCC")
    private val adapter = DownloadAdapter(this)

    companion object {
        const val TAG: String = "release.dialog"
        fun get() = ReleaseViewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.c_fragment_3, group)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.liveSharedItem.observe(viewLifecycleOwner) { onChanged(it) }
        model.episodesTime.observe(viewLifecycleOwner) { onSetShowData() }
        view.recyclerView?.setGridLayoutAdapter(adapter, 3)
        view.fhdText?.setOnClickListener { onItemChange(2) }
        view.hdText?.setOnClickListener { onItemChange(1) }
        view.sdText?.setOnClickListener { onItemChange(0) }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        model.sharedItem = null
        super.onDestroy()
    }

    fun show(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        val previous = manager.findFragmentByTag(TAG)
        if (previous != null) transaction.remove(previous)
        transaction.addToBackStack(null)
        show(transaction, TAG)
    }

    private fun onChanged(t: ItemRelease?) {
        view?.releaseText?.text = t?.episode?.parseAsHtml
        view?.titleText?.text = t?.show?.parseAsHtml
        onItemChange(0, t)
    }

    private fun onItemChange(state: Int = 0, release: ItemRelease? = model.sharedItem) {
        view?.selectedButtonText?.text = when (state) {
            1 -> {
                setUnselected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                setSelected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                val link = release?.HD
                val xdcc = if (link?.xdcc.isNullOrBlank()) null else "https://subsplease.org/xdcc/?search=${Uri.encode(link?.xdcc)}"
                adapter.reset(titles, arrayListOf(link?.magnet, link?.torrent, xdcc))
                "HD [720p]"
            }
            2 -> {
                setSelected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                setUnselected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                val link = release?.FHD
                val xdcc = if (link?.xdcc.isNullOrBlank()) null else "https://subsplease.org/xdcc/?search=${Uri.encode(link?.xdcc)}"
                adapter.reset(titles, arrayListOf(link?.magnet, link?.torrent, xdcc))
                "Full HD [1080p]"
            }
            else -> {
                setUnselected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                setSelected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                val link = release?.SD
                val xdcc = if (link?.xdcc.isNullOrBlank()) null else "https://subsplease.org/xdcc/?search=${Uri.encode(link?.xdcc)}"
                adapter.reset(titles, arrayListOf(link?.magnet, link?.torrent, xdcc))
                "HQ [360p] or SD [480p]"
            }
        }
    }

    private fun onSetShowData() {
        view?.lastUpdatedText?.text = (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), model.episodesTime.value) else
            TimeFormatPreference.format(model.episodesTime.value)) ?: "Never"
    }

    private fun setSelected(button: MaterialButton?, vararg views: View?) {
        button?.setTypeface(button.typeface, Typeface.BOLD)
        for(v in views) v?.visible
        button?.requestLayout()
    }

    private fun setUnselected(button: MaterialButton?, vararg views: View?) {
        button?.setTypeface(button.typeface, Typeface.NORMAL)
        for(v in views) v?.gone
        button?.requestLayout()
    }

    override fun onItemClick(view: View, t: String?, position: Int) {
        if (!t.isNullOrBlank()) startBrowser(context, t, "Select an application to continue.")
    }
}