package coroutines.buildFlow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend fun main() {
    makeFlow()
        .collect{ print(it)}
}

fun makeFlow(): Flow<Int> = flow {
    repeat(3) {num ->
        delay(1000)
        emit(num)
    }
}