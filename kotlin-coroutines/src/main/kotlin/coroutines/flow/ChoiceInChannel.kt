package coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

suspend fun CoroutineScope.produceString(
    s: String,
    time: Long
)= produce {
    while(true){
        delay(time)
        send(s)
    }
}

suspend fun main() = runBlocking {
    val fooChannel = produceString("foo", 210L)
    val barChannel = produceString("BAR", 500L)

    repeat(7) {
        select {
            fooChannel.onReceive {
                println("From fooCh: $it")
            }
            barChannel.onReceive {
                println("From barCh: $it")
            }
        }
    }

    coroutineContext.cancelChildren()
}