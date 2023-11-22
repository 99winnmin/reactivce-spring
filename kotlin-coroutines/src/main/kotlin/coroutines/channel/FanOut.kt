package coroutines.channel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.produceNumbers() = produce<Int> {
    repeat(10) {
        delay(100)
        send(it)
    }
}

fun CoroutineScope.launchProcessor(
    id: Int,
    channel: ReceiveChannel<Int>
) = launch {
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
}

suspend fun main(): Unit = coroutineScope{
    val channel = produceNumbers()
    repeat(3) {
        launchProcessor(it, channel)
    }
}

