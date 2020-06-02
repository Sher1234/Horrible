package info.horriblesubs.sher.libs.preference.holder

import android.content.Context
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.SwitchPreference

class SwitchPreferenceHolder(
    view: View,
    context: Context?,
    adapter: PreferenceAdapter
): BasePreferenceHolder(view, context, adapter) {
    private val subtitleTextView: MaterialTextView? = itemView.findViewById(R.id.subtitleTextView)
    private val titleTextView: MaterialTextView? = itemView.findViewById(R.id.titleTextView)
    private val iconView: AppCompatImageView? = itemView.findViewById(R.id.iconView)
    private val switchView: Switch? = itemView.findViewById(R.id.switchWidget)

    override fun onBindViewHolder(t: BasePreference<out Any>, position: Int) {
        if (t is SwitchPreference) {
            switchView?.isChecked = t.value
            switchView?.setOnCheckedChangeListener { _, isChecked ->
                t.value = isChecked
                adapter.listener?.onPreferenceChange(t, position)
            }
            itemView.setOnClickListener {
                switchView?.isChecked = !(switchView?.isChecked?:false)
            }
        }
        subtitleTextView?.text = t.summary
        iconView?.setImageDrawable(t.icon)
        titleTextView?.text = t.title
    }
}