import java.net.ServerSocket
import java.net.Socket
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import kotlin.concurrent.thread


data class Message(
    val proto: String,
    val filename: String,
    val filesize: String? = null,
    val data: String? = null,
)


fun handleClient(clientSocket: Socket) {
    println("Client connected: ${clientSocket.inetAddress.hostAddress}")

    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    val writer = PrintWriter(clientSocket.getOutputStream(), true)
    reader.read()
    var reading = false
    var file: File = File("server.txt")
    file.createNewFile()

    while (clientSocket.isConnected) {
        val messageFromClient = reader.readLine() ?: break
        println("Received from client: $messageFromClient")
        val parsedMessage = parseData(messageFromClient)

        println("Parsed Data: $parsedMessage")

        if (parsedMessage["proto"]=="initSend") {
            println("INIT")
//            if (!reading) {
//                reading = true
//                file = File("server.txt")
////                file.createNewFile()
//                println("INITSend, waiting for file data now.")
//            }
        } else if (parsedMessage["proto"] == "sendData") {
            if (parsedMessage.containsKey("data")) {
                file.appendText(parsedMessage["data"]!! + '\n')
//                file.writer().flush()
                println("GOT data.")
            }
        } else if (parsedMessage["proto"] == "endSend") {
            reading = false
            println("END data")
            break
        }

        sendAck(writer)
    }
    clientSocket.close()
    println("client closed.")
}

fun parseData(raw: String): Map<String, String> {
    val list = raw.split(";")
    val map = mutableMapOf<String, String>()

    list.forEach{
        val (key, value) = it.split(":")
        map[key] = value
    }
    return map.toMap()
}

fun sendAck(writer: PrintWriter): Boolean {
    // TODO ERROR HANDLING
    val ackMessage = "proto:ack"
    writer.println(ackMessage)
    return true
}


fun main() {
    val serverSocket = ServerSocket(9999) // Port number 12345
    val host = "0.0.0.0" // Bind to all available network interfaces

    println("Server started and listening on $host:9999")
    while (true) {
        val clientSocket: Socket = serverSocket.accept()
        thread {
            handleClient(clientSocket)
        }
    }



        // Responding to the client
//        writer.println("Hello from the server!")
//
//        clientSocket.close()
//        println("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
}
