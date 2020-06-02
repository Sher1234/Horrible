package info.horriblesubs.sher.ui.b.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import info.horriblesubs.sher.libs.recyclerview.AutoFitRecyclerView
import info.horriblesubs.sher.ui._extras.adapters.horrible.ScheduleAdapter
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class SchedulePagerAdapter (
    private val listener: OnItemClickListener<ItemSchedule>,
    viewPager2: ViewPager2?,
    tabLayout: TabLayout?
): RecyclerView.Adapter<SchedulePagerAdapter.Holder>() {

    private val tabs by lazy {
        listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Not yet scheduled")
    }

    init {
        if (viewPager2 != null && tabLayout != null) {
            viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager2.adapter = this
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }

    var result: List<List<ItemSchedule>>? = null
        set(value) {
            field = value
            for (i in 0..8) notifyItemChanged(i)
        }

    override fun onCreateViewHolder(group: ViewGroup, i: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.b_fragment_3_, group, false),
        this@SchedulePagerAdapter
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(result?.get(position) ?: emptyList())
    }

    override fun getItemCount() = tabs.size

    inner class Holder(view: View, private val adapter: SchedulePagerAdapter) : RecyclerView.ViewHolder(view) {
        val recyclerView: AutoFitRecyclerView? = itemView.findViewById(R.id.recyclerView)

        fun onBindToViewHolder(list: List<ItemSchedule>) =
            recyclerView?.apply {
                adapter = ScheduleAdapter(this@Holder.adapter.listener, list)
                setHasFixedSize(true)
                columnWidth = 1.25f
            }
    }
}