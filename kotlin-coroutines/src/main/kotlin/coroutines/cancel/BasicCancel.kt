package coroutines.cancel

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        repeat(1_000) { i ->
            delay(200)
            println("job: I'm sleeping $i ...")
        }
    }

    delay(1100L)
//    job.cancel()
//    job.join() // cancel 이 호출된 뒤 다음 작업을 진행하기 전에 취소 과정이 완료되는 걸 기다리기 위해 join 사용
    job.cancelAndJoin() // cancel + join 을 한번에 호출
    println("main: I'm tired of waiting!")
    println("main: Now I can quit.")
}