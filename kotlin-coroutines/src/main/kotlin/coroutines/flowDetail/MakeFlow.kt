package coroutines.flowDetail

import kotlinx.coroutines.delay
import kotlin.*

suspend fun main() {
   f3()
}

fun f1(): () -> Unit {
    val f: () -> Unit = {
        print("A")
        print("B")
        print("C")
    }
    return f
}

suspend fun f2(): suspend () -> Unit {
    suspend fun innerFunction(): Unit {
        print("A")
        delay(1000)
        print("B")
        delay(1000)
        print("C")
    }
    return ::innerFunction
}

suspend fun f3(): Unit {
    val f: suspend ((String) -> Unit) -> Unit = {emit ->
        emit("A")
        emit("B")
        emit("C")
    }
    f { print(it) }
    f { print(it) }
}

fun interface cFlowCollector {
    suspend fun emit(value: String)
}

suspend fun f4(): Unit {
    val f: suspend (cFlowCollector) -> Unit = {
        it.emit("A")
        it.emit("B")
        it.emit("C")
    }
    f { print(it) }
    f { print(it) }
}

suspend fun f5(): Unit {
    val f: suspend cFlowCollector.() -> Unit = {
        emit("A")
        emit("B")
        emit("C")
    }
    f { print(it) }
    f { print(it) }
}

interface cFlow {
    suspend fun collect(collector: cFlowCollector)
}

suspend fun f6(): Unit {
    val builder: suspend cFlowCollector.() -> Unit = {
        emit("A")
        emit("B")
        emit("C")
    }
    val flow: cFlow = object : cFlow {
        override suspend fun collect(collector: cFlowCollector) {
            collector.builder()
        }
    }
    flow.collect { print(it) }
    flow.collect { print(it) }
}

fun flow(builder: suspend cFlowCollector.() -> Unit) = object : cFlow {
    override suspend fun collect(collector: cFlowCollector) {
        collector.builder()
    }
}

suspend fun f7() {
    val f: cFlow = flow {
        emit("A")
        emit("B")
        emit("C")
    }
    f.collect{ print(it)}
    f.collect{ print(it)}
}

