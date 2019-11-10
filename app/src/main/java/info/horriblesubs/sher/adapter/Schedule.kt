package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.load
import info.horriblesubs.sher.ui.main.settings.KeySettings
import java.time.ZonedDateTime

class ScheduleAdapter (click: ItemClick<ItemSchedule>?, items: List<ItemSchedule>?):
    BaseAdapter<ItemSchedule>(click) {

    init {reset(items?.toMutableList())}

    private var time: ZonedDateTime = ZonedDateTime.now()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemSchedule> {
        return ScheduleHolder(
            time,
            itemClick,
            this,
            parent
        )
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.sid?.toLong() ?: -1
    }

    override fun reset() {
        time = ZonedDateTime.now()
        super.reset()
    }

    override fun reset(listItems: MutableList<ItemSchedule>?) {
        time = ZonedDateTime.now()
        super.reset(listItems)
    }

    override fun search(x: String?) {
        items = listItems?.search(x)
        size = items?.size ?:0
        notifyDataSetChanged()
    }
}

class ScheduleHolder (private val time: ZonedDateTime, itemClick: ItemClick<ItemSchedule>?,
                      adapter: ScheduleAdapter, group: ViewGroup):
    BaseHolder<ItemSchedule>(itemClick, adapter, R.layout._recycler_d, group) {

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
    private val textView1: AppCompatTextView = itemView.findViewById(R.id.textView1)
    private val textView2: AppCompatTextView = itemView.findViewById(R.id.textView2)

    override fun load(t: ItemSchedule?) {
        itemView.setOnClickListener{itemClick?.onClick(t)}
        textView1.text = t?.title.fromHtml()
        imageView.load(t?.image)
        textView2.text =
            if(Constants.value(KeySettings.TimeLeft) as Boolean) t?.schedule?.timeLeft(time)
            else t?.schedule?.time()
    }
}

