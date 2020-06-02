package info.horriblesubs.sher.data.horrible.api.result

import info.horriblesubs.sher.data.horrible.api.day
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek

object ScheduleSplit {
    suspend fun getSplit(list: ArrayList<ItemSchedule>?) = withContext(Dispatchers.IO) {
        when {
            list.isNullOrEmpty() -> null
            else -> list.split()
        }
    }

    private fun ArrayList<ItemSchedule>.split() = listOf(
        get(DayOfWeek.SUNDAY),
        get(DayOfWeek.MONDAY),
        get(DayOfWeek.TUESDAY),
        get(DayOfWeek.WEDNESDAY),
        get(DayOfWeek.THURSDAY),
        get(DayOfWeek.FRIDAY),
        get(DayOfWeek.SATURDAY),
        get(value = false),
    )

    fun ArrayList<ItemSchedule>.get(day: DayOfWeek = DayOfWeek.SUNDAY, value: Boolean = true): List<ItemSchedule>{
        val listed = arrayListOf<ItemSchedule>()
        forEach iterator@{
            if ((value && it.schedule?.scheduled == true && it.day == day) ||
                (!value && it.schedule?.scheduled == false))
                    listed.add(it)
        }
        removeAll(listed)
        return listed
    }
}