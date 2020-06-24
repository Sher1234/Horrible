package info.horriblesubs.sher.ui.e.character_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharacter
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.setLinearLayoutAdapter
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.e_fragment_2_a.view.*
import info.horriblesubs.sher.ui._extras.adapters.mal.BaseImageAdapter as BIAdapter

class ShortCharacterFragment: BottomSheetDialogFragment(), OnItemClickListener<BaseWithImage> {

    private val adapter = BIAdapter(this, true)
    private var character: AnimeCharacter? = null

    companion object {
        const val TAG: String = "character-short-dialog"
        fun get(character: AnimeCharacter) = ShortCharacterFragment().apply {
            this.character = character
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.e_fragment_2_a, group)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.button?.setOnClickListener { character?.url?.let { s -> startBrowser(context, s) } }
        view?.recyclerView?.setLinearLayoutAdapter(adapter, RecyclerView.HORIZONTAL)
        view?.subtitleText?.text = character?.role?.parseAsHtml
        view?.titleText?.text = character?.name
        adapter.set(character?.voiceActors)
        context?.let {
            Glide.with(it).load(character?.imageUrl).transform().apply {
                transform(RoundedCorners(6), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(view?.imageView, view?.progressBar)
        }
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    fun show(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        val previous = manager.findFragmentByTag(TAG)
        if (previous != null) transaction.remove(previous)
        transaction.addToBackStack(null)
        show(transaction, TAG)
    }

    override fun onItemClick(view: View, t: BaseWithImage?, position: Int) {
        context.toast(t?.name ?: "Nil")
    }
}