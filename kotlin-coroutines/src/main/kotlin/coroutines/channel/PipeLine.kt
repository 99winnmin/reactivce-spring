package coroutines.channel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope

fun CoroutineScope.numbers(): ReceiveChannel<Int> =
    produce {
        repeat(3) {
            send(it + 1)
        }
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> =
    produce {
        for (i in numbers) {
            send(i * i)
        }
}

suspend fun main(): Unit = coroutineScope {
    val numbers = numbers()
    val squares = square(numbers)
    for (i in squares) {
        println(i)
    }
}