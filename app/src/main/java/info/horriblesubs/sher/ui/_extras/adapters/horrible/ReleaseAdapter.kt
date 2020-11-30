package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.subsplease.api.model.FHD
import info.horriblesubs.sher.data.subsplease.api.model.HD
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease
import info.horriblesubs.sher.data.subsplease.api.model.SD
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class ReleaseAdapter(
    private val listener: OnItemClickListener<ItemRelease>
): RecyclerView.Adapter<ReleaseAdapter.Holder>(), Filterable {

    private var listClone = mutableListOf<ItemRelease>()
    private var list = mutableListOf<ItemRelease>()

    fun reset(list: List<ItemRelease>? = null) {
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

    override fun getItemId(position: Int) = list[position].episode.toLongOrNull() ?: -1

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_f, group, false),
        this@ReleaseAdapter
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filteredList: List<ItemRelease> = if (charSequence.isNullOrBlank())
                    listClone
                else listClone.filter {
                    it.episode.contains(charSequence, true)
                }
                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                list.apply {
                    clear()
                    addAll(filterResults.values as? List<ItemRelease> ?: emptyList())
                }
                notifyDataSetChanged()
            }
        }
    }

    class Holder (
        view: View,
        private val adapter: ReleaseAdapter
    ): RecyclerView.ViewHolder(view) {

        private val textView: MaterialTextView = itemView.findViewById(R.id.textView)
        private val fhd: View = itemView.findViewById(R.id.mark3)
        private val hd: View = itemView.findViewById(R.id.mark2)
        private val sd: View = itemView.findViewById(R.id.mark1)

        fun onBindToViewHolder(t: ItemRelease, position: Int) {
            itemView.setOnClickListener{ adapter.listener.onItemClick(it, t, position) }
            if (t.FHD == null) fhd.visibility = View.GONE
            if (t.HD == null) hd.visibility = View.GONE
            if (t.SD == null) sd.visibility = View.GONE
            textView.text = t.episode.parseAsHtml()
        }
    }
}