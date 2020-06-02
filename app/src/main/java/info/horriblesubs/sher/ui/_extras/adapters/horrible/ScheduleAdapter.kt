package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.left
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import info.horriblesubs.sher.data.horrible.api.zonedDateTime
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.visible
import java.time.ZonedDateTime

class ScheduleAdapter (
    val listener: OnItemClickListener<ItemSchedule>,
    private val listItems: List<ItemSchedule>
): RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    private val isTimeLeft = TimeLeftPreference.value
    private val currentTime = ZonedDateTime.now()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_d, group, false),
        this@ScheduleAdapter
    )

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(listItems[position], position)
    }

    class Holder (view: View, private val adapter: ScheduleAdapter): RecyclerView.ViewHolder(view) {
        private val titleExtended: MaterialTextView = itemView.findViewById(R.id.titleExtended)
        private val textViewTime = itemView.findViewById<MaterialTextView>(R.id.textViewTime)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun onBindToViewHolder(t: ItemSchedule, position: Int) {
            itemView.setOnClickListener {
                adapter.listener.onItemClick(it, t, position)
            }
            Glide.with(imageView).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(3), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(imageView, progressBar)
            itemView.setOnLongClickListener { view ->
                adapter.listener.onItemLongClick(view, t, position)
                true
            }
            textViewTime.text = if (adapter.isTimeLeft) t.left(adapter.currentTime) else
                TimeFormatPreference.format(t.zonedDateTime, true) ?: "Never"
            titleExtended.text = t.title.parseAsHtml()
            titleExtended.visible
        }
    }
}