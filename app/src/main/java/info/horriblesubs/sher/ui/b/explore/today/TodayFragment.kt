package info.horriblesubs.sher.ui.b.explore.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemTodaySchedule
import info.horriblesubs.sher.databinding.BFragment2ABinding
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.functions.orientation
import info.horriblesubs.sher.libs.dialog.InfoDialog
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.adapters.horrible.TodayAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.c.ShowActivity
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TodayFragment: Fragment(), OnItemClickListener<ItemTodaySchedule.Show> {

    private val infoDialog by lazy { context?.let{ InfoDialog(it) } }
    val model by viewModels<TodayModel>({requireActivity()})
    private lateinit var binding: BFragment2ABinding
    private val adapter = TodayAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_2_a, group) {
            val day = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEEE"))
            title = "$day's${orientation(" ", "\n")}Schedule"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.resource.observe(viewLifecycleOwner) { onChanged(it) }
        binding.apply {
            recyclerView.setLinearLayoutAdapter(adapter, orientation(HORIZONTAL, VERTICAL))
            buttonA.setOnClickListener { infoDialog?.show(Info.TODAY) }
            buttonB.setOnClickListener{ model.refreshDataFromServer }
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun onChanged(t: RepositoryResult<ItemTodaySchedule>?) = when(t?.status) {
        RepositoryResult.FAILURE -> onSetError(t.message)
        RepositoryResult.LOADING -> onSetLoading(true)
        RepositoryResult.SUCCESS -> {
            adapter.reset(t.value?.schedule)
            onSetLoading(false)
        }
        else -> onSetError("Internal App Error !!!")
    }

    private fun onSetLoading(b: Boolean) = binding.let {
        if (b) {
            it.progressBar.visible
            it.buttonB.invisible
            it.buttonB.disable
        } else {
            it.progressBar.gone
            it.buttonB.visible
            it.buttonB.enable
        }
    }

    private fun onSetError(str: String) {
        onSetLoading(false)
        context.toast(str)
    }

    override fun onDestroyView() {
        model.resource.removeObservers(viewLifecycleOwner)
        infoDialog?.dismiss()
        super.onDestroyView()
        model.stopServerCall
    }

    override fun onItemClick(view: View, t: ItemTodaySchedule.Show?, position: Int) {
        t?.page?.let { ShowActivity.startShowActivity(context, it) }
    }
}