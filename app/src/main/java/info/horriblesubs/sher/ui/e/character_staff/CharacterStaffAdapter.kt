package info.horriblesubs.sher.ui.e.character_staff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharactersStaffPage
import info.horriblesubs.sher.libs.recyclerview.AutoFitRecyclerView
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui._extras.adapters.mal.BaseImageAdapter as BIAdapter

class CharacterStaffAdapter (
    val listener: OnItemClickListener<BaseWithImage>,
    viewPager2: ViewPager2?,
    tabLayout: TabLayout?
): RecyclerView.Adapter<CharacterStaffAdapter.Holder>() {

    private val tabs by lazy { listOf("Characters", "Staff") }

    init {
        if (viewPager2 != null && tabLayout != null) {
            viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager2.adapter = this
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }

    var result: AnimeCharactersStaffPage? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(group: ViewGroup, i: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.e_fragment_2_, group, false),
        this@CharacterStaffAdapter
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(if (position == 1) result?.staff else result?.characters)
    }

    override fun getItemCount() = tabs.size

    inner class Holder(view: View, private val adapter: CharacterStaffAdapter) : RecyclerView.ViewHolder(view),
        Toolbar.OnToolbarActionListener {

        val recyclerView: AutoFitRecyclerView? = itemView.findViewById(R.id.recyclerView)
        val toolbar: Toolbar? = itemView.findViewById(R.id.toolbar)

        init { toolbar?.onToolbarActionListener = this }

        fun onBindToViewHolder(list: List<BaseWithImage>?) {
            toolbar?.placeholderText = if (bindingAdapterPosition == 1) "Staff" else "Characters"
            recyclerView?.apply {
                adapter = BIAdapter(this@Holder.adapter.listener).apply { set(list) }
                setHasFixedSize(true)
                columnWidth = 1.25f
            }
        }

        override fun onQueryChanged(text: String?) {
            (recyclerView?.adapter as? BIAdapter)?.filter?.filter(text)
        }
    }
}