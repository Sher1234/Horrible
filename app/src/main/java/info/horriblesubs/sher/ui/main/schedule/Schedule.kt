package info.horriblesubs.sher.ui.main.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import info.horriblesubs.sher.R
import info.horriblesubs.sher.adapter.ItemClick
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.common.Info
import info.horriblesubs.sher.common.LoadingListener
import info.horriblesubs.sher.common.MenuHandler
import info.horriblesubs.sher.dialog.InfoDialog
import info.horriblesubs.sher.dialog.LoadingDialog
import info.horriblesubs.sher.ui.show.Show
import java.util.*
import kotlin.collections.ArrayList

class Schedule: Fragment(), LoadingListener, Observer<Map<String, List<ItemSchedule>>?>,
    PopupMenu.OnMenuItemClickListener, ItemClick<ItemSchedule> {

    private val dayAdapter: DaysAdapter = DaysAdapter(this)
    private var dLoading: LoadingDialog? = null
    private var handler: MenuHandler? = null
    private var vm: ScheduleVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(activity as AppCompatActivity).get(ScheduleVM::class.java)
        vm?.initialize(this)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout._b_fragment_3, group, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        viewPager.adapter = dayAdapter
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy{t, p ->
            t.text = dayAdapter.days[p].day
        }).attach()
        handler = MenuHandler(this, view, R.menu.menu_c)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.schedule?.observe(viewLifecycleOwner, this)
        vm?.refresh()
    }

    override fun onChanged(result: Map<String, List<ItemSchedule>>?) {
        dayAdapter.result = result
        stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.stop()
        stop()
    }

    override fun start() {
        if(dLoading == null)
            dLoading = context?.let{LoadingDialog(it)}
        dLoading?.show()
    }

    override fun stop() {
        dLoading?.dismiss()
        dLoading = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val info: InfoDialog? = context?.let{InfoDialog(it)}
        when(item?.itemId) {
            R.id.info -> info?.show(Info.SCHEDULE, vm?.result?.value)
            R.id.refresh -> vm?.refresh(true)
            else -> return false
        }
        return true
    }

    override fun onClick(e: ItemSchedule?) {
        if (e == null) return
        val intent = Intent(activity, Show::class.java)
        intent.putExtra("link", e.link)
        startActivity(intent)
    }
}

private class DaysAdapter (private val itemClick: ItemClick<ItemSchedule>): Adapter<Day>() {
    var result: Map<String, List<ItemSchedule>>? = null
        set(value) {
            days = days()
            field = value
            notifyDataSetChanged()
        }

    var days: MutableList<Days> = this.days()
        private set

    private fun days(): MutableList<Days> {
        val i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val list:MutableList<Days> = ArrayList()
        for (j in i..7)
            list.add(Days.from(j))
        for (j in 1 until i)
            list.add(Days.from(j))
        list.add(Days.OTHERS)
        return list
    }

    override fun onCreateViewHolder(group: ViewGroup, i: Int): Day {
        return Day(group)
    }

    override fun onBindViewHolder(holder: Day, position: Int) {
        holder.load(result?.get(days[position].day), itemClick)
    }

    override fun getItemCount(): Int {
        return days.size
    }
}
