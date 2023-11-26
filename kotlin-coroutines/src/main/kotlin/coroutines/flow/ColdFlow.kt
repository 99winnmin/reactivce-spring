package coroutines.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

private fun makeFlow() = flow {
    println("Flow started")
    for (i in 1..3){
        delay(1000)
        emit(i)
    }
}

suspend fun main() = coroutineScope {
    val flow = makeFlow()

    delay(1000)
    println("Calling flow...")
    flow.collect { v -> println(v) }
    println("Consuming again...")
    flow.collect { v -> println(v) }
}