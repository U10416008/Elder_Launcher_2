package com.example.dingjie.elder_launcher_2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.widget.Toast
import java.net.Socket
import android.util.Log
import org.jetbrains.anko.db.*


class PhoneStateReceiver : BroadcastReceiver(){
    internal val client = Client("192.168.0.182", 1234)
    var LOG_TAG = "Phone"


    override fun onReceive(context: Context?, intent: Intent?) {


        try{
            Log.d(LOG_TAG, "Coming")
            val tm : TelephonyManager = context!!.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            var my_number = "0000000000"
            if(ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                my_number = tm.line1Number
            }
            when(tm.callState){

                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    val incomeNumber = intent!!.getStringExtra("incoming_number")
                    initServer(incomeNumber, my_number)

                    var db = MySQLite.getInstance(context!!.applicationContext).writableDatabase
                    db.use{

                        db.select("Fequency","time").whereArgs("(number = {call_number})",
                                "call_number" to incomeNumber).exec{
                            var time = parseOpt(IntParser)

                            if (time == null){
                                db.insert("Fequency",
                                        "number" to incomeNumber,
                                        "time" to 1
                                )
                                Log.d("Insert","1")
                            }else{

                                time++
                                db.update("Fequency", "time" to time).whereSimple("number = ?", incomeNumber).exec()
                                Log.d("Update", "" + time)
                            }
                            /*
                                    */

                        }


                    }



                }

            }



        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initServer(number :  String, my_number : String) {
        client.setClientCallback(object : Client.ClientCallback {
            override fun onMessage(message: String) {}

            override fun onConnect(socket: Socket?) {

                client.send("Hello World!\n")
                client.send("$number&$my_number")

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