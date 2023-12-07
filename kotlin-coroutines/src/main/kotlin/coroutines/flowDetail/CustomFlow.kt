package coroutines.flowDetail

import kotlinx.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.*

fun interface CustomFlowCollector<T> {
    suspend fun emit(value: T)
}

interface CustomFlow<T> {
    suspend fun collect(collector: CustomFlowCollector<T>)
}

fun <T> cflow(
    builder: suspend CustomFlowCollector<T>.() -> Unit
) = object : CustomFlow<T> {
    override suspend fun collect(collector: CustomFlowCollector<T>) {
        collector.builder()
    }
}
