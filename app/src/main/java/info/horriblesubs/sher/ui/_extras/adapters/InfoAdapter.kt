package info.horriblesubs.sher.ui._extras.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.RecyclerItemFBinding
import info.horriblesubs.sher.ui.viewBindings

class InfoAdapter: RecyclerView.Adapter<InfoAdapter.Holder>() {

    private val list = arrayListOf<Info>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(viewBindings(R.layout.recycler_item_f, group))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun add(vararg l: Info) {
        val start = itemCount
        list.clear()
        if (start != 0) notifyItemRangeRemoved(0, start)
        list.addAll(l)
        notifyItemRangeInserted(0, itemCount)
    }

    fun add(list: List<Info>?) {
        if (list == null) return
        val start = itemCount
        this.list.clear()
        if (start != 0) notifyItemRangeRemoved(0, start)
        this.list.addAll(list)
        notifyItemRangeInserted(0, itemCount)
    }

    fun update(position: Int, info: Info.() -> Info = {this}) {
        if (position !in 0 until itemCount) return
        list[position] = list[position].info()
        notifyItemChanged(position)
    }

    fun removeAll() {
        if (itemCount > 0)
            notifyItemRangeRemoved(0, itemCount)
        list.clear()
    }

    class Holder(private val binding: RecyclerItemFBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBindToViewHolder(t: Info) {
            binding.data = t
        }
    }
}