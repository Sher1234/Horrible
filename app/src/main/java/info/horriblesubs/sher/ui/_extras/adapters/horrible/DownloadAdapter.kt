package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener

class DownloadAdapter(private val listener: OnItemClickListener<String>): RecyclerView.Adapter<DownloadAdapter.Holder>() {

    private var dTitles: ArrayList<String> = arrayListOf()
    private var dUrls: ArrayList<String?> = arrayListOf()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        this@DownloadAdapter,
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_h, group, false)
    )

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount() = dTitles.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(dTitles[position], dUrls[position], position)
    }

    fun reset(titles: ArrayList<String>, urls: ArrayList<String?>) {
        dTitles.clear()
        dTitles.addAll(titles)
        dUrls.clear()
        dUrls.addAll(urls)
        notifyDataSetChanged()
    }

    class Holder (val adapter: DownloadAdapter, view: View): RecyclerView.ViewHolder(view) {

        private val button: MaterialButton = itemView.findViewById(R.id.button)

        fun onBindToViewHolder(t: String, u: String?, position: Int) {
            button.setOnClickListener{
                adapter.listener.onItemClick(it, u, position)
                Log.d("OnClick", "$t: $u")
            }
            button.isEnabled = !u.isNullOrBlank()
            button.text = t
        }
    }
}