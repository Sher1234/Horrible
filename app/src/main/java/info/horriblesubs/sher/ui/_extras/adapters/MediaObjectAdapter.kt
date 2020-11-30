package info.horriblesubs.sher.ui._extras.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.common.MediaObject
import info.horriblesubs.sher.data.common.toMediaObjects
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into
import info.horriblesubs.sher.ui.visible

class MediaObjectAdapter<T>(
    val listener: OnItemClickListener<T>,
): RecyclerView.Adapter<MediaObjectAdapter.Holder<T>>(), Filterable {

    private var listClone = arrayListOf<MediaObject<T>>()
    private var list = arrayListOf<MediaObject<T>>()

    fun reset(list: List<T>? = null) {
        this.list.clear()
        if (list.isNullOrEmpty()) {
            this.list = listClone
        } else {
            this.listClone.clear()
            this.list = list.toMediaObjects()
            this.listClone = list.toMediaObjects()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_a, group, false),
        this
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder<T>, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filteredList: List<MediaObject<T>> = if (charSequence.isNullOrBlank())
                    listClone
                else listClone.filter {
                    it.searchTerm?.contains(charSequence, true) == true
                }
                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                list.apply {
                    clear()
                    addAll(filterResults.values as? List<MediaObject<T>> ?: emptyList())
                }
                notifyDataSetChanged()
            }
        }
    }

    class Holder<T>(
        view: View,
        private val adapter: MediaObjectAdapter<T>,
    ): RecyclerView.ViewHolder(view) {

        private val titleExtended: MaterialTextView = itemView.findViewById(R.id.titleExtended)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun onBindToViewHolder(t: MediaObject<T>, position: Int) {
            itemView.setOnClickListener {
                adapter.listener.onItemClick(it, t.value, position)
            }
            Glide.with(imageView).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(8), FitCenter())
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
}