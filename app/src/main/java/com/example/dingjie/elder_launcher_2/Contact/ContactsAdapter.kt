package com.example.dingjie.elder_launcher_2.Contact

/**
 * Created by dingjie on 2018/3/18.
 */

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.MainActivity
import com.example.dingjie.elder_launcher_2.R

import java.util.ArrayList

class ContactsAdapter(val list: ArrayList<Contacts>) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), View.OnClickListener {


    override fun onClick(v: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if (onRecyclerViewListener != null) {
            onRecyclerViewListener!!.onItemClick(v, v.tag as Int)

        }
    }

    private var onRecyclerViewListener: OnRecyclerViewListener? = null

    interface OnRecyclerViewListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(position: Int): Boolean
    }

    fun setOnRecyclerViewListener(onRecyclerViewListener: OnRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ContactsViewHolder {
        Log.d("TAG", "onCreateViewHolder, i: " + i)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_layout, null)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = lp
        view.setOnClickListener(this)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ContactsViewHolder?, position: Int) {
        Log.d("TAG", "onBindViewHolder, i: $position, viewHolder: $viewHolder")
        val holder = viewHolder as ContactsViewHolder
        holder.p = position
        val name = list[position].name
        holder.name.text = name
        holder.name.textAlignment = View.TEXT_ALIGNMENT_CENTER
        holder.name.setTypeface(Typeface.SERIF,Typeface.BOLD)
        val image = list[position].image
        if (image != null) {
            holder.image.setImageDrawable(image)
        } else {
           holder.image.setImageResource(R.drawable.contacts)

        }
        holder.image.layoutParams.height = MainActivity.screenHeight / 5
        holder.image.layoutParams.width = MainActivity.screenHeight / 5
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
        override fun onLongClick(v: View?): Boolean {
            return if (null != onRecyclerViewListener) {
                onRecyclerViewListener!!.onItemLongClick(p)
            } else false
        }

        var rootView: View
        var name: TextView
        var image: ImageView
        var p: Int = 0

        init {
            name = itemView.findViewById<View>(R.id.contacts_name) as TextView


            image = itemView.findViewById<View>(R.id.contacts_images) as ImageView

            rootView = itemView.findViewById(R.id.contacts_info)
            rootView.setOnClickListener(this)
            rootView.setOnLongClickListener(this)
        }


        override fun onClick(v: View) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener!!.onItemClick(v, p)

            }


        }
    }

}
