package gov.doge.ecfr.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Deferred
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow

@OptIn(DelicateCoroutinesApi::class)
inline fun <T> Iterable<T>.forEachAsync(
    scope: CoroutineScope = GlobalScope,
    crossinline action: suspend (T) -> Unit
): Job {
    return scope.launch {
        val jobs = map { item ->
            launch { action(item) } // Each item runs in a new coroutine
        }
        jobs.forEach { it.join() } // Wait for all to finish
    }
}

inline fun <T, R> Iterable<T>.mapAsync(
    scope: CoroutineScope = GlobalScope,
    crossinline transform: suspend (T) -> R
): Deferred<List<R>> {
    return scope.async {
        map { item -> async { transform(item) } }.awaitAll()
    }
}

fun getCurrentYear(): Int {
    val now = Clock.System.now()
    return now.toLocalDateTime(TimeZone.currentSystemDefault()).year
}

fun String.toColor(): Color {
    val hash = this.fold(0) { acc, c -> acc + c.code }
    return Color(
        red = (hash * 31 % 255) / 255f,
        green = (hash * 17 % 255) / 255f,
        blue = (hash * 7 % 255) / 255f
    )
}

fun Enum<*>.readableName(): String {
    return name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

fun Double.toReadableString(): String {
    val absValue = kotlin.math.abs(this)

    return when {
        absValue >= 1_000_000_000 -> (this / 1_000_000_000).roundTo(1) + "B"
        absValue >= 1_000_000 -> (this / 1_000_000).roundTo(1) + "M"
        absValue >= 1_000 -> (this / 1_000).roundTo(1) + "K"
        absValue >= 1 -> this.toInt().toString() // Remove decimals for whole numbers
        absValue > 0 -> this.roundTo(2).toString() // Keep up to 2 decimal places
        else -> "0"
    }
}

// Helper function to round manually
fun Double.roundTo(decimalPlaces: Int): String {
    val factor = 10.0.pow(decimalPlaces)
    return ((this * factor).toInt() / factor).toString()
}

@Composable
fun parseHtmlToAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        val regex = "<strong>(.*?)</strong>|<span class=\"elipsis\">(.*?)</span>".toRegex()

        regex.findAll(text).forEach { match ->
            append(text.substring(currentIndex, match.range.first))

            when {
                match.groups[1] != null -> {
                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            background = MaterialTheme.colorScheme.primaryContainer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    append(match.groupValues[1])
                    pop()
                }
                match.groups[2] != null -> {
                    append("...")
                }
            }

            currentIndex = match.range.last + 1
        }

        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }
}