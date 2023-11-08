package coroutines.cancel

import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) { // 새로운 잡이 부모로부터 상속받은 잡을 대체
        try {
            repeat(1_000) { i ->
                delay(Random.nextLong(2000))
                println("job: I'm sleeping $i ...")
            }
        } catch (e: CancellationException) {
            println(e)
            throw e
        } finally {
            println("Will Always execute")
        }
    }

    delay(1100L)
    job.cancelAndJoin()
    println("main: Now I can quit.")
    delay(1000)
}