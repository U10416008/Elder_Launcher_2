package com.example.dingjie.elder_launcher_2

import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by dingjie on 2018/3/14.
 */

class Client(private val ip: String, private val port: Int) {
    private var socket: Socket? = null
    private var socketOutput: OutputStream? = null
    private var socketInput: BufferedReader? = null
    private var listener: ClientCallback? = null

    fun connect() {
        Thread(Runnable {
            socket = Socket()
            val socketAddress = InetSocketAddress(ip, port)
            try {
                socket!!.connect(socketAddress)
                socketOutput = socket!!.getOutputStream()
                socketInput = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                ReceiveThread().start()

                if (listener != null)
                    listener!!.onConnect(socket)
            } catch (e: IOException) {
                if (listener != null)
                    listener!!.onConnectError(socket, e.message!!)
            }
        }).start()
    }

    fun disconnect() {
        try {
            socket!!.close()
        } catch (e: IOException) {
            if (listener != null)
                listener!!.onDisconnect(socket, e.message!!)
        }

    }

    fun send(message: String) {
        try {
            socketOutput!!.write(message.toByteArray())
            Log.d("send", message + "\n")
        } catch (e: IOException) {
            if (listener != null)
                listener!!.onDisconnect(socket, e.message!!)
        }

    }

    private inner class ReceiveThread : Thread(), Runnable {
        override fun run() {
            var message: String
            try {
                message = socketInput!!.readLine()
                while (message != null) {   // each line must end with a \n to be received
                    if (listener != null) {
                        listener!!.onMessage(message)
                        Log.d("receive", message + "\n")
                    }
                    message = socketInput!!.readLine()
                }
            } catch (e: IOException) {
                if (listener != null)
                    listener!!.onDisconnect(socket, e.message!!)
            }

        }
    }

    fun setClientCallback(listener: ClientCallback) {
        this.listener = listener
    }

    fun removeClientCallback() {
        this.listener = null
    }

    interface ClientCallback {
        fun onMessage(message: String)
        fun onConnect(socket: Socket?)
        fun onDisconnect(socket: Socket?, message: String)
        fun onConnectError(socket: Socket?, message: String)
    }
}
