package info.horriblesubs.sher.common

import java.time.format.DateTimeFormatter

@Suppress("unused")
enum class Formats(private val format: String) {
    SERVER_FORMAT("yyyy-MM-d HH:mm:ss"),
    FULL_12H("MMMM dd, yyyy hh:mm a"),
    FULL_24H("MMMM dd, yyyy HH:mm"),
    TIME_12H("hh:mm a"),
    DATE("dd-MM-yyyy"),
    TIME_24H("HH:mm"),
    DAY("EEE");
    val formatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(format)
}