package info.horriblesubs.sher.ui._extras.adapters.mal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithName
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class TypeAdapter<T: BaseWithName>(val listener: OnItemClickListener<T>?):
    RecyclerView.Adapter<TypeAdapter.Holder<T>>() {

    private val list = arrayListOf<T>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(this,
            LayoutInflater.from(group.context)
                .inflate(R.layout.recycler_item_i, group, false)
        )

    override fun onBindViewHolder(holder: Holder<T>, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun set(list: List<T>?) {
        if (list.isNullOrEmpty()) return
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class Holder<T: BaseWithName> (private val adapter: TypeAdapter<T>, view: View):
        RecyclerView.ViewHolder(view) {
        private val title: AppCompatTextView = itemView.findViewById(R.id.textView)
        fun onBindToViewHolder(t: T) {
            title.text = t.name
            itemView.setOnClickListener { adapter.listener?.onItemClick(it, t, bindingAdapterPosition) }
        }
    }
}