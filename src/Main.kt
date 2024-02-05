import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket
import javax.xml.crypto.Data

fun main() {
//    val file = File("/Users/alletirohanr/Downloads/sample.pdf")
//
//    val stream = file.inputStream()
//    val byteArray = ByteArray(15000)
//    val readBytes = stream.read(byteArray)
//    println("Read $readBytes from the file.")
//
//    println(byteArray[0])
//    println(byteArray[14999])
//    println(byteArray.count{ it.toInt() == 0})

    testServer()
}


fun testServer() {
    val server = ServerSocket(9999)
    val client = server.accept()

    val inputReader = client.getInputStream()

//    val file = File("test.html")
//    val stream = file.outputStream()
    val s = FileOutputStream("random.jpg", true)

//    stream.write()
//    file.createNewFile()
    var i = 0
//    val DataInputStream(inputReader)
    while (client.isConnected) {
//        DataOutputStream
        val byteArray = ByteArray(10*1024)
        val numBytes = inputReader.read(byteArray)

        println("$i . Got Data $numBytes")
        if (numBytes == -1) {
            break
        }
//        file.writeBytes()
//        file.appendBytes(byteArray)
//        stream.write(byteArray)
        s.write(byteArray)
        s.flush()
        println("DONE")
        i += 1
    }
    inputReader.close()
    s.close()
//    inputReader.
//    stream.close()
}