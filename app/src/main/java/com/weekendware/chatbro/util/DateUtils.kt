import java.text.SimpleDateFormat
import java.util.*

fun formatPrettyTimestamp(timestamp: Long): String {
    val now = Calendar.getInstance()
    val then = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return when {
        isSameDay(now, then) -> "Today • " + formatTime(timestamp)
        isYesterday(now, then) -> "Yesterday • " + formatTime(timestamp)
        else -> SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
}

private fun isYesterday(today: Calendar, date: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply {
        timeInMillis = today.timeInMillis
        add(Calendar.DAY_OF_YEAR, -1)
    }
    return isSameDay(yesterday, date)
}

private fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
