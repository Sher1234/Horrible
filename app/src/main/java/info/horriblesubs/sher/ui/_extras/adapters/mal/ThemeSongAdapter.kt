package info.horriblesubs.sher.ui._extras.adapters.mal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class ThemeSongAdapter(val listener: OnItemClickListener<String>): RecyclerView.Adapter<ThemeSongAdapter.Holder>() {

    private val list = arrayListOf<String>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(
            LayoutInflater.from(group.context).inflate(R.layout.recycler_item_j, group, false),
            this@ThemeSongAdapter
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun set(list: List<String>?) {
        if (list == null) return
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class Holder (view: View, private val adapter: ThemeSongAdapter): RecyclerView.ViewHolder(view) {
        private val value: AppCompatTextView = itemView.findViewById(R.id.textView)

        fun onBindToViewHolder(t: String) {
            itemView.setOnClickListener { adapter.listener.onItemClick(it, t, bindingAdapterPosition) }
            value.text = t
        }
    }
}