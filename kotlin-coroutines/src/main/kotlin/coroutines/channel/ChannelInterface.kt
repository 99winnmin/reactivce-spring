package coroutines.channel

import kotlinx.coroutines.CancellationException

interface SendChannelCustom<in E> {
    suspend fun send(element: E)
    fun close(): Boolean
}

interface ReceiveChannelCustom<out E> {
    suspend fun receive(): E
    fun cancel(cause: CancellationException? = null)
}

interface ChannelCustom<E> : SendChannelCustom<E>, ReceiveChannelCustom<E>
