package gov.doge.ecfr.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Deferred

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
