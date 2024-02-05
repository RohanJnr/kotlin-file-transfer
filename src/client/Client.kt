package client

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter


import java.net.Socket

class Client(val host: String, val port: Int) {
    val client = Socket(host, port)

    val reader = BufferedReader(InputStreamReader(client.getInputStream()))
    val writer = PrintWriter(client.getOutputStream(), true)

    fun sendDummyData() {
//        val data = "proto:initSend;filename:abc.txt".toByteArray(Charsets.UTF_8)
//        println("Sending $data")
        writer.println("proto:initSend;filename:abc.txt")
        // Receiving the response from the server
        val response = reader.readLine()
        println("Received from server: $response")
        client.close()
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

    fun startFileSend() {
        initSend("sample.txt")
        sendFile()
        endSend()
    }

    fun sendFile() {
        val file = File("sample.txt")
        val lines = file.readLines()

        for (line in lines) {
            val msg = "proto:sendData;data:$line"
            writer.println(msg)
        }

    }

    fun endSend() {
        val protocol = "proto:initSend"
        writer.println(protocol)
        getAck()
    }

    fun initSend(filename: String) {
        val protocol = "proto:initSend;filename:$filename"
        writer.println(protocol)
        getAck()
    }

    fun getAck() {
        val ack = reader.readLine()
        val data = parseData(ack)

    }

//    val outputStream = client.getOutputStream()
}


fun main() {
    val client = Client("127.0.0.1", 9999)

//    client.sendDummyData()
    client.startFileSend()
}