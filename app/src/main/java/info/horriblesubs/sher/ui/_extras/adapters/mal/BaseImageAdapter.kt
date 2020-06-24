package info.horriblesubs.sher.ui._extras.adapters.mal

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
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.into

class BaseImageAdapter(
    val listener: OnItemClickListener<BaseWithImage>,
    horizontal: Boolean = false
): RecyclerView.Adapter<BaseImageAdapter.Holder>(), Filterable {

    private var listClone = arrayListOf<MediaObject<BaseWithImage>>()
    private var list = arrayListOf<MediaObject<BaseWithImage>>()

    private val layout = if (horizontal) R.layout.recycler_item_l else R.layout.recycler_item_k

    fun set(list: List<BaseWithImage>? = null) {
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

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(
            LayoutInflater.from(group.context).inflate(layout, group, false),
            this
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filteredList: List<MediaObject<BaseWithImage>> = if (charSequence.isNullOrBlank())
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
                    addAll(filterResults.values as? List<MediaObject<BaseWithImage>> ?: emptyList())
                }
                notifyDataSetChanged()
            }
        }
    }

    class Holder(
        view: View,
        private val adapter: BaseImageAdapter,
    ): RecyclerView.ViewHolder(view) {

        private val title: MaterialTextView = itemView.findViewById(R.id.titleText)
        private val subtitle: MaterialTextView = itemView.findViewById(R.id.subtitleText)
        private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun onBindToViewHolder(t: MediaObject<BaseWithImage>, position: Int) {
            itemView.setOnClickListener {
                adapter.listener.onItemClick(it, t.value, position)
            }
            Glide.with(imageView).load(t.imageUrl).transform().apply {
                transform(RoundedCorners(4), FitCenter())
                placeholder(R.drawable.app_placeholder)
                timeout(30000)
            }.into(imageView, progressBar)
            itemView.setOnLongClickListener { view ->
                adapter.listener.onItemLongClick(view, t.value, position)
                true
            }
            subtitle.text = t.subhead?.parseAsHtml()
            title.text = t.header?.parseAsHtml()
        }
    }
}