package info.horriblesubs.sher.data.mal.api.model.enums

enum class Days(private val type: String) {
    MONDAY("monday"), TUESDAY("tuesday"), WEDNESDAY("wednesday"),
    THURSDAY("thursday"), FRIDAY("friday"), SATURDAY("saturday"),
    SUNDAY("sunday"), OTHER("other"), UNKNOWN("unknown"), ALL("");

    override fun toString(): String = type
}