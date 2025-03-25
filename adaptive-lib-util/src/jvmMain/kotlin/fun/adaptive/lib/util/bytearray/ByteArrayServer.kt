package `fun`.adaptive.lib.util.bytearray

class ByteArrayServer {

    fun run(address: String = "127.0.0.1", port: Int = 34786) {
//
//        val selectorManager = SelectorManager(Dispatchers.IO)
//        val serverSocket = aSocket(selectorManager).udp().bind(InetSocketAddress(address, port))
//
//        runBlocking {
//            for (datagram in serverSocket.incoming) {
//                val receivedData = datagram.packet.readBytes()
//                val senderAddress = datagram.address
//
//                println("Received data: ${String(receivedData)} from $senderAddress")
//
//                serverSocket.send(
//                    Datagram(ByteReadPacket(receivedData), senderAddress)
//                )
//            }
//        }
    }


}