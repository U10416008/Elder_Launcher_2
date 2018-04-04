package com.example.dingjie.elder_launcher_2

/**
 * Created by dingjie on 2018/3/15.
 */

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

import java.util.ArrayList

class AppList : AppCompatActivity() {

    private var manager: PackageManager? = null
    private var apps: MutableList<Item>? = null
    private var list: ListView? = null
    override fun onCreate(savedIntanceState: Bundle?) {
        super.onCreate(savedIntanceState)
        setContentView(R.layout.app_list)
        loadApps()
        loadList()
        addClickListener()


    }

    private fun loadApps() {
        manager = packageManager
        apps = ArrayList()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val available = manager!!.queryIntentActivities(intent, 0)
        for (ri in available) {
            val app = Item()
            app.label = ri.activityInfo.packageName
            app.name = ri.loadLabel(manager)
            app.icon = ri.loadIcon(manager)
            apps!!.add(app)
        }
    }

    private fun loadList() {
        list = findViewById<View>(R.id.list) as ListView
        val adapter = object : ArrayAdapter<Item>(this, R.layout.item, apps) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var convertView = convertView
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item, null)

                }
                val appIcon = convertView!!.findViewById<View>(R.id.icon) as ImageView
                appIcon.layoutParams.height = MainActivity.screenHeight / 5
                appIcon.layoutParams.width = MainActivity.screenHeight / 5
                val drawable = apps!![position].icon

                appIcon.setImageDrawable(drawable)

                val appName = convertView.findViewById<View>(R.id.name) as TextView
                appName.text = apps!![position].name
                appName.textSize = 32f

                return convertView
            }
        }

        list!!.adapter = adapter

    }

    private fun addClickListener() {
        list!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val intent = manager!!.getLaunchIntentForPackage(apps!![i].label!!.toString())
            startActivity(intent)
        }
    }
}
