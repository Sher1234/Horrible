package info.horriblesubs.sher.ui.c.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.horrible.api.FHD
import info.horriblesubs.sher.data.horrible.api.HD
import info.horriblesubs.sher.data.horrible.api.SD
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.functions.orientation
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

class ReleaseViewFragment: BottomSheetDialogFragment(), OnItemClickListener<ItemRelease.Download> {

    private val model by viewModels<ShowModel>({requireActivity()})
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerView?.setGridLayoutAdapter(adapter, orientation(2, 3))
        model.liveSharedItem.observe(viewLifecycleOwner) { onChanged(it) }
        model.episodesTime.observe(viewLifecycleOwner) { onSetShowData() }
        view?.fhdText?.setOnClickListener { onItemChange(2) }
        view?.hdText?.setOnClickListener { onItemChange(1) }
        view?.sdText?.setOnClickListener { onItemChange(0) }
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
        view?.releaseText?.text = t?.release?.parseAsHtml
        view?.titleText?.text = t?.title?.parseAsHtml
        var booleanSD = false
        var booleanHD = false
        if (t.SD.isNullOrEmpty()) {
            setUnselected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
            view?.sdText?.gone
            booleanSD = true
        } else {
            view?.sdText?.visible
            adapter.reset(t.SD)
            onItemChange(0)
        }
        if (t.HD.isNullOrEmpty()) {
            setUnselected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
            view?.hdText?.gone
            booleanHD = true
        } else {
            view?.hdText?.visible
            if (booleanSD) {
                adapter.reset(t.HD)
                onItemChange(1)
            }
        }
        if (t.FHD.isNullOrEmpty()) {
            setUnselected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
            view?.fhdText?.gone
        } else {
            view?.fhdText?.visible
            if (booleanSD && booleanHD) {
                adapter.reset(t.FHD)
                onItemChange(2)
            }
        }
    }

    private fun onItemChange(state: Int = 0) {
        view?.selectedButtonText?.text = when (state) {
            1 -> {
                setUnselected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                setSelected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                adapter.reset(model.liveSharedItem.value.HD)
                "HD [720p]"
            }
            2 -> {
                setSelected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                setUnselected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                adapter.reset(model.liveSharedItem.value.FHD)
                "Full HD [1080p]"
            }
            else -> {
                setUnselected(view?.fhdText, view?.selectedFullHD1, view?.selectedFullHD2)
                setUnselected(view?.hdText, view?.selectedHD1, view?.selectedHD2)
                setSelected(view?.sdText, view?.selectedSD1, view?.selectedSD2)
                adapter.reset(model.liveSharedItem.value.SD)
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

    override fun onItemClick(view: View, t: ItemRelease.Download?, position: Int) {
        t?.link?.let { startBrowser(this.context, it, "Select an application to continue.") }
    }
}