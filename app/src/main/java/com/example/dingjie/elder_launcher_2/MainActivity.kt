package com.example.dingjie.elder_launcher_2

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Contact.ContactActivity
import com.example.dingjie.elder_launcher_2.Game.GameActivity
import com.example.dingjie.elder_launcher_2.UI.MainActivityUI
import org.jetbrains.anko.*

import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    internal val client = Client("10.102.3.226", 1234)
    internal lateinit var button: Button
    internal lateinit var more: ImageView
    internal lateinit var contact: ImageView
    internal lateinit var chat: ImageView
    internal lateinit var game: ImageView
    var timeText : TextView?  = null
    var time = Calendar.getInstance().time;
    override fun onCreate(savedIntanceState: Bundle?) {
        super.onCreate(savedIntanceState)
        MainActivityUI().setContentView(this)

        button = find<Button>(R.id.button)

        button.setOnClickListener { }
        initChat()
        initGame()
        initMore()
        initContacts()
        initServer()
        //initTime()

    }

    fun initChat() {
        chat = find(R.id.chat)
        chat.layoutParams.height = screenHeight / 3
        chat.layoutParams.width = screenWidth / 3
        chat.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, ContactActivity::class.java)
            startActivity(intent)
        }
        chat.setOnLongClickListener { false }
    }
    fun initGame() {
        game = find(R.id.game)
        game.layoutParams.height = screenHeight / 3
        game.layoutParams.width = screenWidth / 3
        game.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        game.setOnLongClickListener { false }
    }
    fun initContacts() {
        contact = find(R.id.contacts)
        contact.layoutParams.height = screenHeight / 3
        contact.layoutParams.width = screenWidth / 3
        contact.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, ContactActivity::class.java)
            startActivity(intent)
        }
        contact.setOnLongClickListener { false }
    }

    fun initMore() {
        more = find(R.id.more)
        more.layoutParams.height = screenHeight / 3
        more.layoutParams.width = screenWidth / 3
        more.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, AppList::class.java)
            startActivity(intent)
        }
        more.setOnLongClickListener { false }
    }

    fun initServer() {
        client.setClientCallback(object : Client.ClientCallback {
            override fun onMessage(message: String) {}

            override fun onConnect(socket: Socket?) {

                client.send("Hello World!\n")
                client.send("0910832632")
                //client.disconnect();
            }

            override fun onDisconnect(socket: Socket?, message: String) {}

            override fun onConnectError(socket: Socket?, message: String) {}
        })

        client.connect()
    }
    fun initTime(){
        timeText = find<TextView>(R.id.time)
        timeText?.text = time.toString()
    }

    companion object {
        val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels

        val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }
}
