package coroutines.flowFuncs

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

suspend fun main() = coroutineScope {
    flowOf(1,2)
        .onEach { delay(1000) }
        .onCompletion { println("Completed") }
        .collect { println(it) }

    val job = launch {
        flowOf(1,2)
            .onEach { delay(1000) }
            .onCompletion { println("Completed") }
            .collect { println(it) }
    }
    delay(1100)
    job.cancel()
}