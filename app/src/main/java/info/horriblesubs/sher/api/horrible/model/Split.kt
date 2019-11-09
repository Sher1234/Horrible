package info.horriblesubs.sher.api.horrible.model

import java.time.DayOfWeek
import java.time.DayOfWeek.*

object Split {
    private var list: List<ItemSchedule> = mutableListOf()
    private var map: MutableMap<String, List<ItemSchedule>> = mutableMapOf()
    private fun list(list: List<ItemSchedule>, day: DayOfWeek, b: Boolean): List<ItemSchedule> {
        val itemList: MutableList<ItemSchedule> = arrayListOf()
        for (item in list) {
            val dayOfWeek: DayOfWeek = item.schedule?.day() ?: continue
            if (b && item.schedule.scheduled != null && item.schedule.scheduled && dayOfWeek == day) itemList.add(item)
            if (!b && item.schedule.scheduled != null && !item.schedule.scheduled) itemList.add(item)
        }
        return itemList
    }
    fun schedule(list: List<ItemSchedule>?): Map<String, List<ItemSchedule>>? {
        return when (list) {
            null -> map
            Split.list -> {
                if (map.isNullOrEmpty()) map =
                    map()
                map
            }
            else -> {
                Split.list = list
                map.clear()
                map =
                    map()
                map
            }
        }
    }
    private fun map(): MutableMap<String, List<ItemSchedule>> {
        val map: MutableMap<String, List<ItemSchedule>> = mutableMapOf()
        val remaining: MutableList<ItemSchedule> = list.toMutableList()
        var list: List<ItemSchedule>?
        list = list(
            remaining,
            MONDAY,
            true
        )
        remaining.removeAll(list)
        map["Monday"] = list
        list = list(
            remaining,
            TUESDAY,
            true
        )
        remaining.removeAll(list)
        map["Tuesday"] = list
        list = list(
            remaining,
            WEDNESDAY,
            true
        )
        remaining.removeAll(list)
        map["Wednesday"] = list
        list = list(
            remaining,
            THURSDAY,
            true
        )
        remaining.removeAll(list)
        map["Thursday"] = list
        list = list(
            remaining,
            FRIDAY,
            true
        )
        remaining.removeAll(list)
        map["Friday"] = list
        list = list(
            remaining,
            SATURDAY,
            true
        )
        remaining.removeAll(list)
        map["Saturday"] = list
        list = list(
            remaining,
            SUNDAY,
            true
        )
        remaining.removeAll(list)
        map["Sunday"] = list
        list = list(
            remaining,
            MONDAY,
            false
        )
        map["To be scheduled"] = list
        remaining.removeAll(list)
        return map
    }
}