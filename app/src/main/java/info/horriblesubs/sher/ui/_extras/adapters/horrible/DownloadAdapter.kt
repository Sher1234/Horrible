package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease.Download as Link

class DownloadAdapter(
    private val listener: OnItemClickListener<Link>
): RecyclerView.Adapter<DownloadAdapter.Holder>() {

    private var list = mutableListOf<Link>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        this@DownloadAdapter,
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_g, group, false)
    )

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    fun reset(list: List<Link>?) {
        if (!list.isNullOrEmpty())
            this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun reset(vararg arrayOfDownloads: Link, clear: Boolean = false) {
        if (clear) this.list.clear()
        list.addAll(arrayOfDownloads)
        notifyDataSetChanged()
    }

    class Holder (val adapter: DownloadAdapter, view: View): RecyclerView.ViewHolder(view) {

        private val button: MaterialButton = itemView.findViewById(R.id.button)

        fun onBindToViewHolder(t: Link, position: Int) {
            button.setOnClickListener{ adapter.listener.onItemClick(it, t, position) }
            button.isEnabled = !t.link.isNullOrBlank()
            button.text = t.source?.parseAsHtml()
        }
    }
}