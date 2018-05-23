package com.example.dingjie.elder_launcher_2

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import java.net.Socket
import android.util.Log


class PhoneStateReceiver : BroadcastReceiver(){
    internal val client = Client("10.1.209.8", 1234)
    var LOG_TAG = "Phone"

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {


        try{
            Log.d(LOG_TAG, "Coming")
            var state = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE)
            var number = intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                wasRinging = true
            }
            if(wasRinging &&state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                initServer(number)
                Toast.makeText(context,number,Toast.LENGTH_LONG).show()
            }
            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                wasRinging = false
            }



        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initServer(number :  String) {
        client.setClientCallback(object : Client.ClientCallback {
            override fun onMessage(message: String) {}

            override fun onConnect(socket: Socket?) {

                client.send("Hello World!\n")
                client.send(number)
                client.disconnect();
            }

            override fun onDisconnect(socket: Socket?, message: String) {}

            override fun onConnectError(socket: Socket?, message: String) {}
        })

        client.connect()
    }

    companion object {
        var wasRinging = false
    }

}