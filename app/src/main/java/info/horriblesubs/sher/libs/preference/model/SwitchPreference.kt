package info.horriblesubs.sher.libs.preference.model

abstract class SwitchPreference: BasePreference<Boolean>() {
    override val type: Int = SWITCH_PREFERENCE
    override var value: Boolean
        get() = sharedPreferences?.getBoolean(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putBoolean(key, value)?.apply() }
}