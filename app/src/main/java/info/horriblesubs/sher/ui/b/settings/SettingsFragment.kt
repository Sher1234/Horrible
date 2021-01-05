package info.horriblesubs.sher.ui.b.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.installations.FirebaseInstallations
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.BFragment5Binding
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.listeners.OnPreferenceChangeListener
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.GroupTitlePreference
import info.horriblesubs.sher.libs.preference.prefs.*
import info.horriblesubs.sher.ui.setLinearLayoutAdapter
import info.horriblesubs.sher.ui.startBrowser
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewBindings

class SettingsFragment: Fragment(), OnPreferenceChangeListener {

    private val adapter by lazy { PreferenceAdapter(requireContext(), this) }
    private lateinit var binding: BFragment5Binding

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        binding = viewBindings(inflater, R.layout.b_fragment_5, group)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setLinearLayoutAdapter(adapter)
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(ThemePreference.value)
        adapter.addAll(
            GroupTitlePreference("Notification settings"),
            NotificationPreference, TokenPreference,
            GroupTitlePreference("Schedule settings"),
            TimeLeftPreference, TimeFormatPreference,
            GroupTitlePreference("Application settings"),
            ThemePreference, MarkedPreference,
            GroupTitlePreference("Others"),
            PrivacyPolicyPreference, GithubPreference,
            BuildDatePreference, VersionPreference,
            AppCachePreference
        )
    }

    override fun <T> onPreferenceChange(preference: BasePreference<T>, position: Int) {
        when(preference) {
            is TimeFormatPreference, is TimeLeftPreference, is MarkedPreference ->
                context?.toast(preference.title + " updated.")
            is PrivacyPolicyPreference, is GithubPreference ->
                startBrowser(context, preference.value.toString())
            is BuildDatePreference, is VersionPreference ->
                context?.toast(preference.summary ?: "")
            is NotificationPreference -> {
                NotificationPreference.setDefaultNotificationMode(preference.value)
                context?.toast(preference.title + " updated.")
            }
            is TokenPreference -> {
                context?.toast("Fixing notification id...")
                FirebaseInstallations.getInstance().id.addOnCompleteListener {
                    if (it.isSuccessful) preference.value = it.result ?: "NULL"
                    else context?.toast("Error fixing notification id.")
                    context?.toast("Fixing notification id...")
                }
            }
            is AppCachePreference -> {
                context.toast("Deleting ${preference.value} B of cache...")
                preference.deleteCache()
            }
            is ThemePreference -> {
                AppCompatDelegate.setDefaultNightMode(preference.value)
                context?.toast(preference.summary ?: "")
            }
        }
    }

}