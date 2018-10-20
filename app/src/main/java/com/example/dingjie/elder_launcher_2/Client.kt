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
                socket!!.tcpNoDelay = true
                socketOutput = socket!!.getOutputStream()
                socketInput = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                //ReceiveThread().start()
                Log.d("","thread create")
                if (listener != null)
                    listener!!.onConnect(socket)

                try {

                    while (socket!!.isConnected) {
                        try {
                            var message = socketInput?.readLine()

                            Log.d("receive", message)
                            // each line must end with a \n to be received
                            if (listener != null && message != null) {
                                listener!!.onMessage(message)
                                //Log.d("receive", message + "\n")
                            }
                        }catch (ex : Exception){
                            if (listener != null)
                                listener!!.onDisconnect(socket, ex.message!!)
                        }


                    }
                } catch (e: IOException) {
                    if (listener != null)
                        listener!!.onDisconnect(socket, e.message!!)
                }

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
                //message = socketInput!!.readLine()
                while (socket!!.isConnected) {
                    Log.d("","in while")
                    message = socketInput!!.readLine()// each line must end with a \n to be received
                    if (listener != null) {
                        listener!!.onMessage(message)
                        Log.d("receive", message + "\n")
                    }

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
