package info.horriblesubs.sher.libs.preference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.holder.*
import info.horriblesubs.sher.libs.preference.listeners.OnPreferenceChangeListener
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.GroupTitlePreference

class PreferenceAdapter(
    private val context: Context?,
    val listener: OnPreferenceChangeListener?
): RecyclerView.Adapter<BasePreferenceHolder>(), Filterable {

    init {
        PreferenceManager.getDefaultSharedPreferences(context)
            ?.registerOnSharedPreferenceChangeListener { _, key ->
                var position = -1
                list.forEachIndexed { index, preference ->
                    if (preference.key == key)
                        position = index
                }
                if (position != -1)
                    notifyItemRangeChanged(0, position)
            }
    }

    private val listClone = arrayListOf<BasePreference<out Any>>()
    private val list = arrayListOf<BasePreference<out Any>>()

    fun addAll(vararg preferences: BasePreference<out Any>) {
        listClone.addAll(preferences)
        list.addAll(preferences)
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    private fun getView(group: ViewGroup, @LayoutRes id: Int): View {
        return LayoutInflater.from(group.context).inflate(id, group, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePreferenceHolder {
        return when(viewType) {
            BasePreference.SWITCH_PREFERENCE -> SwitchPreferenceHolder(
                getView(parent, R.layout._lib_pref_switch),
                context,
                this
            )
            BasePreference.GROUP_TITLE -> GroupTitlePreferenceHolder(
                getView(parent, R.layout._lib_pref_group),
                context,
                this
            )
            BasePreference.LIST_PREFERENCE -> ListPreferenceHolder(
                getView(parent, R.layout._lib_pref_base),
                context,
                this
            )
            else -> PreferenceHolder(
                getView(parent, R.layout._lib_pref_base),
                context,
                this
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BasePreferenceHolder, position: Int) {
        holder.onBindViewHolder(list[position], position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filteredList: List<BasePreference<out Any>> = if (charSequence.isNullOrBlank())
                    listClone
                else listClone.filter {
                    if (it !is GroupTitlePreference)
                        it.title?.contains(charSequence, true) == true
                    else false
                }
                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                list.apply {
                    clear()
                    addAll(filterResults.values as? List<BasePreference<out Any>> ?: emptyList())
                }
                notifyDataSetChanged()
            }
        }
    }
}