package info.horriblesubs.sher.ui.e.character_staff

import android.os.Bundle
import android.view.View
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharacter
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharactersStaffPage
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeStaff
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.BaseMalFragment
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.e.AnimeModel
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_fragment_3.view.*

class CharacterStaffFragment: BaseMalFragment(), OnItemClickListener<BaseWithImage> {

    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private val model by viewModels<CharacterStaffModel>({requireActivity()})
    private val sharedModel by viewModels<AnimeModel>({requireActivity()})
    override val title: String = "Characters & Staff"
    override val layoutId = R.layout.e_fragment_2
    override val icon: Int = R.drawable.ic_group
    override val name = "character-staff"

    private val adapter by lazy {
        CharacterStaffAdapter(this, view?.viewPager, view?.tabLayout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        errorDialog = context?.let{ NetworkErrorDialog(it) }

        model.liveData.observe(viewLifecycleOwner) { onChanged(it) }
        sharedModel.liveSharedId.observe(viewLifecycleOwner) { model.initialize(it) }
    }

    private fun onChanged(t: RepositoryResult<AnimeCharactersStaffPage>?) = when(t?.status) {
        RepositoryResult.SUCCESS -> onSetData(t.value)
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        else -> onSetError("Unexpected error occurred!!!")
    }

    private fun onSetError(str: String) {
        context.toast(str)
        onSetLoading(false)
        errorDialog?.show("https://horriblesubs.info/release-schedule")
    }

    private fun onSetData(page: AnimeCharactersStaffPage?) {
        onSetLoading(false)
        adapter.result = page
    }

    private fun onSetLoading(b: Boolean) {
        loadingDialog = if (b) {
            onSetLoading(false)
            context?.let {
                LoadingDialog(it).apply {
                    show()
                }
            }
        } else {
            loadingDialog?.dismiss()
            null
        }
    }

    override fun onDestroyView() {
        model.stopServerCall
        clearFindViewByIdCache()
        super.onDestroyView()
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
    }

    override fun onItemClick(view: View, t: BaseWithImage?, position: Int) {
        context.toast(when(t) {
            is AnimeCharacter -> {
                ShortCharacterFragment.get(t).show(childFragmentManager)
                "${t.name}"
            }
            is AnimeStaff -> "${t.name}: Staff"
            else -> t?.name ?: "null"
        })
    }
}