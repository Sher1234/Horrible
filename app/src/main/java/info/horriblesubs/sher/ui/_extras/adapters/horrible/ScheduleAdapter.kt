package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.ItemSchedule
import info.horriblesubs.sher.data.subsplease.api.model.imageUrl
import info.horriblesubs.sher.databinding.RecyclerItemDBinding
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.viewBindings

class ScheduleAdapter (
    val listener: OnItemClickListener<ItemSchedule.Show>,
    private val listItems: List<ItemSchedule.Show>
): RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(group)

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val t = listItems[position]
        holder.binding.root.setOnClickListener { listener.onItemClick(it, t, position) }
        Glide.with(holder.binding.imageView).load(t.imageUrl).transform().apply {
            transform(RoundedCorners(8), CenterCrop())
            placeholder(R.drawable.app_placeholder)
            timeout(30000)
        }.into(holder.binding.imageView, holder.binding.progressBar)
        holder.binding.root.setOnLongClickListener { view ->
            listener.onItemLongClick(view, t, position)
            true
        }
        holder.binding.timeTextView.text = if (t.time.isNotBlank()) t.time else "Never"
        holder.binding.titleTextView.text = t.title.parseAsHtml()
    }

    class Holder (
        group: ViewGroup,
        val binding: RecyclerItemDBinding = viewBindings(R.layout.recycler_item_d, group)
    ): RecyclerView.ViewHolder(binding.root)
}