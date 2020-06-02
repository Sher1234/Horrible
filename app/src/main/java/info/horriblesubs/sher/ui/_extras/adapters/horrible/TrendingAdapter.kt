package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import info.horriblesubs.sher.functions.orientation
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.visible

class TrendingAdapter(
    val listener: OnItemClickListener<ItemList>,
): RecyclerView.Adapter<TrendingAdapter.Holder>() {

    private var listClone = mutableListOf<ItemList>()
    private var list = mutableListOf<ItemList>()

    private var limiterValue: Boolean = true

    val toggleLimiter: Int
        get() {
            limiterValue = !limiterValue
            refreshAdapterItems()
            return itemCount
        }

    private fun refreshAdapterItems() {
        if (itemCount == 0) notifyDataSetChanged()
        else {
            if (list.size > 10) {
                if (limiterValue)
                    notifyItemRangeRemoved(10, list.size)
                else
                    notifyItemRangeInserted(10, list.size)
            } else
                notifyItemRangeChanged(0, itemCount)
        }
    }

    override fun getItemCount() = if (limiterValue && list.size > 10) 10 else list.size

    fun reset(list: List<ItemList>? = null) {
        this.list.clear()
        if (list.isNullOrEmpty()) {
            this.list = listClone
        } else {
            this.listClone.clear()
            this.list = list.toMutableList()
            this.listClone = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(orientation(
            R.layout.recycler_item_b,
            R.layout.recycler_item_a
        ), group, false),
        this
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    class Holder(
        view: View,
        private val adapter: TrendingAdapter,
    ): RecyclerView.ViewHolder(view) {

        private val titleExtended: MaterialTextView = itemView.findViewById(R.id.titleExtended)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun onBindToViewHolder(t: ItemList, position: Int) {
            itemView.setOnClickListener {
                adapter.listener.onItemClick(it, t, position)
            }
            Glide.with(imageView).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(4), orientation(CenterCrop(), FitCenter()))
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(imageView, progressBar)
            itemView.setOnLongClickListener{ view ->
                adapter.listener.onItemLongClick(view, t, position)
                true
            }
            titleExtended.text = t.title.parseAsHtml()
            titleExtended.visible
        }
    }
}