package coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// 플로우 빌더는 중단 함수가 아니기 때문에
// CoroutineScope 가 필요하지 않음
fun usersFlow(): Flow<String> = flow {
    repeat(3) {
        delay(1000)
        val ctx = currentCoroutineContext()
        val name = ctx[CoroutineName]?.name
        emit("User$it in $name")
    }
}

suspend fun main() {
    val users = usersFlow()

    withContext(CoroutineName("Name")) {
        val job = launch {
            // collect 는 중단함수!!!
            users.collect { println(it)}
        }
        launch {
            delay(2100)
            println("그만 줘!!")
            job.cancel()
        }
    }
}