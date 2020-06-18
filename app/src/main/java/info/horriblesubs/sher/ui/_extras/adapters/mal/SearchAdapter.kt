package info.horriblesubs.sher.ui._extras.adapters.mal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.common.MediaObject
import info.horriblesubs.sher.data.mal.api.model.common.search.SearchItem
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.visible

class SearchAdapter<T: SearchItem>(val listener: OnItemClickListener<T>):
    ListAdapter<T, SearchAdapter.Holder<T>>(
        comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<T> {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_a, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: Holder<T>, position: Int) =
        holder.onBindToViewHolder(MediaObject(getItem(position)), position)

    class Holder<T: SearchItem>(
        view: View,
        private val adapter: SearchAdapter<T>,
    ): RecyclerView.ViewHolder(view) {

        private val titleExtended: MaterialTextView = itemView.findViewById(R.id.titleExtended)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun onBindToViewHolder(t: MediaObject<T>, position: Int) {
            itemView.setOnClickListener {
                adapter.listener.onItemClick(it, t.value, position)
            }
            Glide.with(imageView).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(3), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(imageView, progressBar)
            itemView.setOnLongClickListener { view ->
                adapter.listener.onItemLongClick(view, t.value, position)
                true
            }
            titleExtended.text = t.header?.parseAsHtml()
            titleExtended.visible
        }
    }

    companion object {
        private fun <T: SearchItem> comparator() = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) =
                oldItem.malId == newItem.malId

            override fun areContentsTheSame(oldItem: T, newItem: T) =
                oldItem.equals(newItem)
        }
    }
}