package `fun`.adaptive.lib.util.bytearray

//import io.ktor.network.selector.SelectorManager
//import io.ktor.network.sockets.Datagram
//import io.ktor.network.sockets.InetSocketAddress
//import io.ktor.network.sockets.aSocket
//import io.ktor.utils.io.core.ByteReadPacket
//import io.ktor.utils.io.core.readBytes
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.runBlocking

class ByteArrayClient {

//    fun run(address : String = "127.0.0.1", port : Int = 34786) {
//
//        val remoteAddress = InetSocketAddress(address, port)
//
//        val selectorManager = SelectorManager(Dispatchers.IO)
//        val socket = aSocket(selectorManager).udp().connect(remoteAddress)
//
//        runBlocking {
//            val message = "Hello, Server!".toByteArray()
//
//            socket.send(Datagram(ByteReadPacket(message), socket.remoteAddress))
//
//            println("Message sent to $address:$port")
//
//            // Receive response from server
//            val response = socket.incoming.receive()
//            val responseData = response.packet.readBytes()
//
//            println("Received from server: ${String(responseData)}")
//        }
//    }

}