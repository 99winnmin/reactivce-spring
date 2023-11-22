package coroutines.channel

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    overflow()
}

suspend fun overflow(): Unit = coroutineScope {
    val channel = Channel<Int>(capacity = 3, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    launch {
        repeat(5) {
            channel.send(it * 2)
            delay(100)
            println("Sent")
        }
        channel.close()
    }

    delay(1000)
    for (i in channel) {
        println("Consuming next one : " + i)
        delay(1000)
    }
}

