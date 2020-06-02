package info.horriblesubs.sher.libs.preference.model

abstract class ListPreference<T>: BasePreference<T>() {
    override val type: Int = LIST_PREFERENCE
    val entries = arrayListOf<String>()
    val entryValues = arrayListOf<T>()

    fun addEntries(vararg entries: String) {
        this.entries += entries
    }

    fun addEntryValues(vararg entryValues: T) {
        this.entryValues += entryValues
    }
}