package `fun`.adaptive.service.transport

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.service.model.TransportEnvelope
import `fun`.adaptive.utility.safeCall
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousCloseException
import java.nio.channels.Channels
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StreamServiceCallTransport(
    scope: CoroutineScope,
    inputStream: InputStream,
    val outputStream: OutputStream,
    override val wireFormatProvider: WireFormatProvider,
    serviceContext: ServiceContext? = null,
    val writeTimeout: Duration = 2.seconds,
) : ServiceCallTransport(scope) {

    val writeChannel = Channel<TransportEnvelope>(1)

    val inputChannel = Channels.newChannel(inputStream)
    val outputChannel = Channels.newChannel(outputStream)

    val readerThread = Thread { incoming() }
    val writerThread = Thread { outgoing() }

    val serviceContext: ServiceContext = serviceContext ?: ServiceContext(transport = this)

    override suspend fun start(serviceImplFactory: ServiceImplFactory) : ServiceCallTransport {
        super.start(serviceImplFactory)
        readerThread.start()
        writerThread.start()
        return this
    }

    override fun context(): ServiceContext =
        serviceContext

    override suspend fun disconnect() {
        stop()
    }

    override suspend fun stop() {
        if (trace) transportLog.fine("disconnecting")
        safeCall(transportLog) { disconnectPending() }
        safeCall(transportLog) { writeChannel.close() }
        safeCall(transportLog) { inputChannel.close() }
        safeCall(transportLog) { outputChannel.close() }
        safeCall(transportLog) { writerThread.interrupt() }
        safeCall(transportLog) { readerThread.interrupt() }
    }

    override suspend fun send(envelope: TransportEnvelope) {
        withTimeout(writeTimeout) {
            writeChannel.send(envelope)
        }
    }

    fun outgoing() {
        runBlocking {
            try {

                for (envelope in writeChannel) {

                    wireFormatProvider.writeMessage(wireFormatProvider.encode(envelope, TransportEnvelope)) {
                        val byteBuffer = ByteBuffer.wrap(it)
                        while (byteBuffer.hasRemaining()) {
                            outputChannel.write(byteBuffer)
                        }
                    }

                    outputStream.flush()
                }

            } catch (ex: Exception) {
                coroutineContext.ensureActive()
                transportLog.error(ex)
            } finally {
                stop()
            }
        }
    }

    fun incoming() {
        runBlocking {
            var buffer = ByteBuffer.allocate(1024)

            try {
                while (true) {
                    // Ensure there is space in the buffer to read new data
                    if (buffer.remaining() == 0) {
                        buffer = growBuffer(buffer)
                    }

                    // Read data into the buffer
                    val bytesRead = inputChannel.read(buffer)
                    if (bytesRead == - 1) break // End of stream

                    // Flip the buffer to prepare for reading
                    buffer.flip()

                    val bytesAvailable = buffer.remaining()
                    val offset = buffer.position()
                    val bytes = ByteArray(bytesAvailable)
                    buffer.get(bytes)

                    val (messages, remaining) = wireFormatProvider.readMessage(bytes, 0, bytesAvailable)

                    buffer.position(offset + bytesAvailable - remaining.size)

                    // Compact the buffer to make space for more data
                    buffer.compact()

                    messages.forEach { message ->
                        receive(message)
                    }
                }
            } catch (_: AsynchronousCloseException) {
                // this is fine, result of `disconnect`
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                transportLog.error(e)
            } finally {
                stop()
            }
        }
    }

    fun growBuffer(buffer: ByteBuffer): ByteBuffer {
        val newSize = (buffer.capacity() * 1.2).toInt()
        val newBuffer = ByteBuffer.allocate(newSize)
        buffer.flip() // Prepare buffer for reading
        newBuffer.put(buffer) // Copy the existing data to the new buffer
        return newBuffer
    }

}