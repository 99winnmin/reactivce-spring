package coroutines.flowFuncs

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

suspend fun main() {
    flowOf(1,2)
        .onEach { delay(1000) }
        .onStart { println("Before") }
        .collect{ println(it) }

    flowOf(1,2)
        .onEach { delay(1000) }
        .onStart { emit(0) }
        .collect{ println(it) }
}