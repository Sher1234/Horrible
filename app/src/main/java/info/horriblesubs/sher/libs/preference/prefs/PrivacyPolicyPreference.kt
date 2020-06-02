package info.horriblesubs.sher.libs.preference.prefs

import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference

object PrivacyPolicyPreference: BasePreference<String>() {

    override val defaultValue = App.get().getString(R.string.privacy_policy_link)
    override val key: String get() = "privacy-policy"
    override val type: Int get() = DEFAULT
    override var value = defaultValue

    init {
        setSummary(R.string.privacy_policy_desc)
        setIcon(R.drawable.ic_policy)
        setTitle(R.string.privacy_policy)
    }
}