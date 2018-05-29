package com.example.dingjie.elder_launcher_2.reminder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.find

class RemindApp : AppCompatActivity() {
    var listView : RecyclerView? = null
    var adapter : RemindAdapter? = null
    var remind_arrayList : ArrayList<Remind>? = null
    var add_Button : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remind_list)
        listView = find(R.id.remind_recycle)!!
        listView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(applicationContext)
        listView!!.layoutManager = layoutManager
        add_Button = find(R.id.add_thing)
        remind_arrayList = ArrayList<Remind>()
        adapter = RemindAdapter(remind_arrayList!!)
        listView!!.adapter = adapter
        add_Button!!.setOnClickListener{
            remind_arrayList!!.add(Remind("Do the laundry",13,0))
            adapter!!.notifyDataSetChanged()
        }



    }
}