package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.inLibrary
import info.horriblesubs.sher.data.horrible.api.model.ItemLatest
import info.horriblesubs.sher.libs.preference.prefs.MarkedPreference
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.visible

class LatestAdapter(
    private val listener: OnItemClickListener<ItemLatest>
): RecyclerView.Adapter<LatestAdapter.Holder>() {

    private var listClone: MutableList<ItemLatest> = mutableListOf()
    private var list: MutableList<ItemLatest> = mutableListOf()
    private var isMarkedAvailable = MarkedPreference.value
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

    override fun getItemId(position: Int) = listClone[position].sid?.toLong() ?: -1

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_c, group, false),
        this@LatestAdapter
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    fun reset(list: List<ItemLatest>? = null) {
        isMarkedAvailable = MarkedPreference.value
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

    class Holder(
        view: View,
        private val adapter: LatestAdapter
    ): RecyclerView.ViewHolder(view) {

        private val release: AppCompatTextView? = itemView.findViewById(R.id.textView2)
        private val title: AppCompatTextView? = itemView.findViewById(R.id.textView1)
        private val marker: View? = itemView.findViewById(R.id.marked)
        private val fhd: View? = itemView.findViewById(R.id.mark3)
        private val hd: View? = itemView.findViewById(R.id.mark2)
        private val sd: View? = itemView.findViewById(R.id.mark1)

        fun onBindToViewHolder(t: ItemLatest, position: Int) {
            itemView.setOnClickListener { adapter.listener.onItemClick(it, t, position) }
            release?.text = t.release?.parseAsHtml()
            title?.text = t.title.parseAsHtml()
            if (t.quality != null) {
                if (t.quality?.get(2) == false) fhd?.visibility = View.GONE
                if (t.quality?.get(1) == false) hd?.visibility = View.GONE
                if (t.quality?.get(0) == false) sd?.visibility = View.GONE
            }
            if (adapter.isMarkedAvailable)
                inLibrary(t.link) {
                    if (this) marker?.visible else marker?.gone
                }
        }
    }
}