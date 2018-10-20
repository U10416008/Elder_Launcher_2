package com.example.dingjie.elder_launcher_2.reminder

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import com.example.dingjie.elder_launcher_2.MainActivity
import com.example.dingjie.elder_launcher_2.R
import java.time.LocalDateTime
import java.util.*


class RemindAdapter(val list: ArrayList<Remind>) : RecyclerView.Adapter<RemindAdapter.RemindViewHolder>(), View.OnClickListener{
    private var onRecyclerViewListener: OnRecyclerViewListener? = null
    interface OnRecyclerViewListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(position: Int): Boolean
    }
    override fun onClick(v: View) {

        if (onRecyclerViewListener != null) {
            onRecyclerViewListener!!.onItemClick(v, v.tag as Int)

        }
    }




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RemindViewHolder, position: Int) {
        //val currentDateTime = LocalDateTime.now()
        val holder = holder as RemindViewHolder
        holder.p = position
        //if(list[position].month == currentDateTime.month.value && list[position].day == currentDateTime.dayOfMonth) {

            holder.thing.text = list[position].thing
            var min = if (list[position].min == 0) {
                "00"
            } else {
                list[position].min.toString()
            }
            var hour = if (list[position].hour == 0) {
                "00"
            } else {
                list[position].hour.toString()
            }

            var time_string = "$hour : $min"
            holder.time.text = time_string
        //}

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.remind_list_layout, null)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = lp
        view.setOnClickListener(this)
        return RemindViewHolder(view)
    }

    inner class RemindViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
        var rootView: View
        var thing: TextView
        var time: TextView
        var p: Int = 0

        init {
            thing = itemView.findViewById<View>(R.id.thing) as TextView


            time = itemView.findViewById<View>(R.id.date_time) as TextView
            time.setTextColor(Color.rgb((150..256).random(),(150..256).random(),(150..256).random()))
            rootView = itemView.findViewById(R.id.remind_info)
            rootView.setOnClickListener(this)
            rootView.setOnLongClickListener(this)
        }
        fun ClosedRange<Int>.random() =
                Random().nextInt(endInclusive - start) +  start
        override fun onLongClick(p0: View?): Boolean {
            return if (null != onRecyclerViewListener) {
                onRecyclerViewListener!!.onItemLongClick(p)
            } else false
        }
        
        override fun onClick(v: View) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener!!.onItemClick(v, p)

            }
        }

    }
}