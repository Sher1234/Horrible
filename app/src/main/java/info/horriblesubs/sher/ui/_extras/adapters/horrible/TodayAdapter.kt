package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.ItemTodaySchedule
import info.horriblesubs.sher.data.subsplease.api.model.imageUrl
import info.horriblesubs.sher.databinding.RecyclerItemBBinding
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.viewBindings
import info.horriblesubs.sher.ui.visible

class TodayAdapter(
    val listener: OnItemClickListener<ItemTodaySchedule.Show>
): RecyclerView.Adapter<TodayAdapter.Holder>() {

    private var list = mutableListOf<ItemTodaySchedule.Show>()

    override fun getItemCount() = list.size

    fun reset(list: List<ItemTodaySchedule.Show>? = null) {
        this.list = (list ?: emptyList()).toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(viewBindings(R.layout.recycler_item_b, group))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val t = list[position]
        holder.binding.root.setOnClickListener { listener.onItemClick(it, t, position) }
        Glide.with(holder.binding.imageView).load(t.imageUrl).transform().apply {
            transform(RoundedCorners(8), CenterCrop())
            placeholder(R.drawable.app_placeholder)
            timeout(30000)
        }.into(holder.binding.imageView, holder.binding.progressBar)
        holder.binding.root.setOnLongClickListener{ view ->
            listener.onItemLongClick(view, t, position)
            true
        }
        holder.binding.airedTextView.text = if (t.aired) "Aired" else "Not yet aired"
        holder.binding.titleTextView.text = t.title.parseAsHtml()
        holder.binding.airedTextView.isChecked = t.aired
        holder.binding.timeTextView.text = t.time
        holder.binding.titleTextView.visible
    }

    class Holder(val binding: RecyclerItemBBinding): RecyclerView.ViewHolder(binding.root)
}