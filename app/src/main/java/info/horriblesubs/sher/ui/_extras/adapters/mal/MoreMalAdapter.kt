package info.horriblesubs.sher.ui._extras.adapters.mal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.BaseMalFragment
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class MoreMalAdapter(val listener: OnItemClickListener<BaseMalFragment>?):
    RecyclerView.Adapter<MoreMalAdapter.Holder>() {

    private val list = arrayListOf<BaseMalFragment>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(this,
            LayoutInflater.from(group.context)
                .inflate(R.layout.recycler_item_j, group, false)
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun set(list: List<BaseMalFragment>?) {
        if (list.isNullOrEmpty()) return
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class Holder (private val adapter: MoreMalAdapter, view: View): RecyclerView.ViewHolder(view) {
        private val button: MaterialButton = itemView.findViewById(R.id.button)
        fun onBindToViewHolder(t: BaseMalFragment) {
            button.setOnClickListener { adapter.listener?.onItemClick(it, t, bindingAdapterPosition) }
            button.setIconResource(t.icon)
            button.text = t.title
        }
    }
}