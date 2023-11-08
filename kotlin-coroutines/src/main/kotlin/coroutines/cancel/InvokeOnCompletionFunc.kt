package coroutines.cancel

import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        delay(Random.nextLong(2400))
        println("Finished")
    }
    delay(800)
    // invokeOnCompletion 은 잡이 completed 되거나 cancelled 와 같은 마지막 상태에 도달했을 때 호출될 핸들러를 지정
    // invokeOnCompletion 은 취소하는 중에 동기적으로 호출됨, 어떤 스레드에서 실행할지 결정할 수 없음
    job.invokeOnCompletion { exception: Throwable? ->
        println("Will always be printed")
        // 취소되었으면 cancellationException 이 출력됨
        // 잡이 예외 없이 끝났으면 null 이 출력됨
        println("The exception is $exception")
    }
    delay(800)
    job.cancelAndJoin()
}