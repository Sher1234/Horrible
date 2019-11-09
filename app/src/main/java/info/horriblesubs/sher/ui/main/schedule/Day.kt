package info.horriblesubs.sher.ui.main.schedule

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.adapter.ScheduleAdapter
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.common.ErrorHandler
import info.horriblesubs.sher.common.ErrorListener
import info.horriblesubs.sher.common.inflate

class Day (group: ViewGroup): ViewHolder(group.inflate(R.layout._b_fragment_3_a)), ErrorListener {
    val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
    private val errorHandler = ErrorHandler(this, itemView)

    fun load(list: List<ItemSchedule>?, itemClick: ItemClick<ItemSchedule>?) {
        if(list.isNullOrEmpty()) errorHandler.show() else errorHandler.hide()
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = ScheduleAdapter(itemClick, list)
    }

    override fun errorVisible() {
        recyclerView.visibility = View.GONE
    }

    override fun errorHidden() {
        recyclerView.visibility = View.VISIBLE
    }
}

internal enum class Days(val day: String) {
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    OTHERS("To be scheduled");

    companion object {
        fun from(i: Int): Days {
            return when (i) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                7 -> SATURDAY
                else -> OTHERS
            }
        }
    }
}