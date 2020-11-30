package info.horriblesubs.sher.ui.b.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.ItemSchedule
import info.horriblesubs.sher.libs.recyclerview.AutoFitRecyclerView
import info.horriblesubs.sher.ui._extras.adapters.horrible.ScheduleAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class SchedulePagerAdapter (
    private val listener: OnItemClickListener<ItemSchedule.Show>,
    viewPager: ViewPager2?,
    tabLayout: TabLayout?
): RecyclerView.Adapter<SchedulePagerAdapter.Holder>() {

    private val tabs by lazy {
        listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Not yet scheduled")
    }

    private val keys by lazy {
        listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "TBD")
    }

    init {
        if (viewPager != null && tabLayout != null) {
            viewPager.adapter = this
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabs[position]
            }.attach()
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    var result: ItemSchedule? = null
        set(value) {
            field = value
            for (i in 0..8) notifyItemChanged(i)
        }

    override fun onCreateViewHolder(group: ViewGroup, i: Int) =
        Holder(LayoutInflater.from(group.context).inflate(R.layout.b_fragment_3_, group, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = result?.schedule?.get(keys[position]) ?: emptyList()
        holder.recyclerView?.apply {
            adapter = ScheduleAdapter(this@SchedulePagerAdapter.listener, list)
            setHasFixedSize(true)
            columnWidth = 1.45f
        }
    }

    override fun getItemCount() = tabs.size

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView = itemView.findViewById<AutoFitRecyclerView>(R.id.recyclerView)
    }
}