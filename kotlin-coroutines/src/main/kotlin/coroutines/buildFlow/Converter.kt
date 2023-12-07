package coroutines.buildFlow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest

suspend fun main() {
    listOf(1,2,3,4,5)
        // 또는 setOf(1,2,3,4,5)
        // 또는 sequenceOf(1,2,3,4,5)
        .asFlow()
        .collect{ print(it)}
}