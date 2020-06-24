package info.horriblesubs.sher.ui.e.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.mal.api.allTitles
import info.horriblesubs.sher.data.mal.api.infoList
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimePage
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.InfoAdapter
import info.horriblesubs.sher.ui._extras.adapters.mal.MoreMalAdapter
import info.horriblesubs.sher.ui._extras.adapters.mal.ThemeSongAdapter
import info.horriblesubs.sher.ui._extras.adapters.mal.TypeAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.e.AnimeMalActivity
import info.horriblesubs.sher.ui.e.AnimeModel
import info.horriblesubs.sher.ui.e.character_staff.CharacterStaffFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.e_fragment_1.view.*

class AnimeHomeFragment: BaseFragment() {

    private val sharedModel by viewModels<AnimeModel>({requireActivity()})
    private val model by viewModels<HomeModel>({requireActivity()})
    override val layoutId: Int = R.layout.e_fragment_1
    private val adapterG by lazy { TypeAdapter(listenerBWT) }
    private val adapterS by lazy { TypeAdapter(listenerBWT) }
    private val adapterP by lazy { TypeAdapter(listenerBWT) }
    private val adapterM by lazy { MoreMalAdapter(listenerMM) }
    private val adapterL by lazy { TypeAdapter(listenerBWT) }
    private val adapterO by lazy { ThemeSongAdapter(listenerOPED) }
    private val adapterE by lazy { ThemeSongAdapter(listenerOPED) }
    private val adapterI by lazy { InfoAdapter() }
    override val name: String = "main-anime-mal"

    private val listenerOPED = object: OnItemClickListener<String> {
        override fun onItemClick(view: View, t: String?, position: Int) {
            context?.toast(t ?: "Nil")
        }
    }

    private val listenerMM = object: OnItemClickListener<BaseMalFragment> {
        override fun onItemClick(view: View, t: BaseMalFragment?, position: Int) {
            t?.let { (activity as? AnimeMalActivity)?.onChangeFragment(it) }
        }
    }

    private val listenerBWT = object: OnItemClickListener<BaseWithType> {
        override fun onItemClick(view: View, t: BaseWithType?, position: Int) {
            context?.toast("${t?.name}: ${t?.type}")
        }
    }

    private var synopsisToggle = false
        get() {
            field = !field
            return field
        }

    private var backgroundToggle = false
        get() {
            field = !field
            return field
        }

    private var openingsToggle = false
        get() {
            field = !field
            return field
        }

    private var endingsToggle = false
        get() {
            field = !field
            return field
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.recyclerViewA?.setFlexLayoutAdapter(adapterG)
        view?.recyclerViewB?.setFlexLayoutAdapter(adapterI)
        view?.recyclerViewC?.setFlexLayoutAdapter(adapterM)
        view?.recyclerViewD?.setGridLayoutAdapter(adapterO, 2)
        view?.recyclerViewE?.setGridLayoutAdapter(adapterE, 2)
        view?.recyclerViewF?.setFlexLayoutAdapter(adapterS)
        view?.recyclerViewG?.setFlexLayoutAdapter(adapterL)
        view?.recyclerViewH?.setFlexLayoutAdapter(adapterP)
        model.liveData.observe(viewLifecycleOwner) { onChanged(it) }
        sharedModel.liveSharedId.observe(viewLifecycleOwner) { model.initialize(it) }
    }

    private fun onSetDetail(t: AnimePage?) {
        if (t == null || t.title.isNullOrBlank() || (sharedModel.id ?: 0) <= 0) {
            context.toast("Undefined URL!")
            return
        }
        context?.let {
            Glide.with(it).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(6), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(view?.imageView, view?.progressBar)
        }
        view?.allTitleText?.text = t.allTitles.parseAsHtml
        view?.titleText?.text = t.title.parseAsHtml
        view?.statusTextView?.text = t.status
        adapterI.add(t.infoList)
        adapterG.set(t.genres)
        view?.synopsisId?.apply {
            if ((t.synopsis?.length ?: 0) <= 200) checkMarkDrawable = null
            else setCheckMarkDrawable(R.drawable.da_up_down)
        }
        view?.synopsisText?.text = t.synopsis.short.parseAsHtml
        synopsisToggle = false
        view?.synopsisId?.setOnClickListener { _ ->
            val b = synopsisToggle
            view?.synopsisId?.isChecked = b
            view?.synopsisText?.text = (if (b) t.synopsis else t.synopsis?.short)?.parseAsHtml
        }

        if (t.background.isNullOrBlank()) {
            view?.backgroundText?.gone
            view?.backgroundId?.gone
        } else {
            view?.backgroundText?.visible
            view?.backgroundId?.visible
        }

        view?.backgroundId?.apply {
            if ((t.background?.length ?: 0) <= 200) checkMarkDrawable = null
            else setCheckMarkDrawable(R.drawable.da_up_down)
        }
        view?.backgroundText?.text = t.background.short.parseAsHtml
        backgroundToggle = false
        view?.backgroundId?.setOnClickListener { _ ->
            val b = backgroundToggle
            view?.backgroundId?.isChecked = b
            view?.backgroundText?.text = (if (b) t.background else t.background?.short).parseAsHtml
        }

        if (t.openings.isNullOrEmpty()) {
            view?.openingsText?.visible
            view?.recyclerViewD?.gone
        } else {
            view?.recyclerViewD?.visible
            view?.openingsText?.gone
        }

        if (t.endings.isNullOrEmpty()) {
            view?.recyclerViewE?.gone
            view?.endingsText?.visible
        } else {
            view?.recyclerViewE?.visible
            view?.endingsText?.gone
        }

        view?.openingsId?.apply {
            if ((t.openings?.size ?: 0) < 4) checkMarkDrawable = null
            else setCheckMarkDrawable(R.drawable.da_up_down)
        }
        adapterO.set(t.openings.short)
        openingsToggle = false
        view?.openingsId?.setOnClickListener { _ ->
            val b = openingsToggle
            view?.openingsId?.isChecked = b
            adapterO.set(if (b) t.openings.full else t.openings.short)
        }

        view?.endingsId?.apply {
            if ((t.endings?.size ?: 0) < 4) checkMarkDrawable = null
            else setCheckMarkDrawable(R.drawable.da_up_down)
        }
        adapterE.set(t.endings.short)
        endingsToggle = false
        view?.endingsId?.setOnClickListener { _ ->
            val b = endingsToggle
            view?.endingsId?.isChecked = b
            adapterE.set(if (b) t.endings.full else t.endings.short)
        }
        onLoadAdapter(view?.recyclerViewF, view?.studiosText, adapterS, t.studios)
        onLoadAdapter(view?.recyclerViewG, view?.licensorsText, adapterL, t.licensors)
        onLoadAdapter(view?.recyclerViewH, view?.producersText, adapterP, t.producers)
        adapterM.set(listOf(CharacterStaffFragment()))
    }

    private fun onChanged(t: RepositoryResult<AnimePage>?) = when(t?.status) {
        RepositoryResult.FAILURE -> context.toast(t.message)
        null -> context.toast("Internal app error!!!")
        RepositoryResult.SUCCESS -> onSetDetail(t.value)
        else -> {}
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    private fun <T: BaseWithType> onLoadAdapter(r: RecyclerView?, v: View?, a: TypeAdapter<T>, list: List<T>?) {
        if (list.isNullOrEmpty()) {
            v.visible
            r.gone
        } else {
            v.gone
            r.visible
            a.set(list)
        }
    }

    private val String?.short: String get() {
        return if (this.isNullOrBlank()) "" else
            this.substring(0, if (length > 200) 200 else length) + if (length > 200) "..." else ""
    }

    private val <T> List<T>?.short: List<T> get() {
        val size = if ((this?.size ?: 0) >= 4) 4 else this?.size ?: 0
        return this?.subList(0, size) ?: emptyList()
    }

    private val <T> List<T>?.full: List<T> get() {
        return this ?: emptyList()
    }
}