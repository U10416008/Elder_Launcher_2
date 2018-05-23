package com.example.dingjie.elder_launcher_2.UI

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Contact.ContactKeyBoard
import com.example.dingjie.elder_launcher_2.MainActivity
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*

class KeyBoard:AnkoComponent<ContactKeyBoard>{
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 60f
        }
    }
    override fun createView(ui: AnkoContext<ContactKeyBoard>): View = with(ui){
        verticalLayout {
            var number : TextView? = null
            linearLayout() {
                number = textView {
                    gravity = Gravity.CENTER
                    textSize = 60f
                    id = R.id.numberText
                }.lparams(
                        height = matchParent,
                        width = matchParent
                )


            }.lparams {
                width = matchParent
                height = wrapContent

            }
            verticalLayout {

                val rowCount = 4
                val columnCount = 3

                for (i in 1 until rowCount) {
                    linearLayout {
                        for (j in 1..columnCount) {
                            val num = (columnCount) * (i - 1) + j
                            button(num.toString()) {
                                gravity = Gravity.CENTER

                                setOnClickListener {
                                    number?.append(num.toString())
                                }
                            }.lparams {
                                weight = 1f
                                width = matchParent
                                height = matchParent
                            }
                        }
                    }.lparams{
                        weightSum = 3f
                        width = matchParent
                        height = wrapContent
                        weight =1f
                    }
                }
                linearLayout {
                    button("*") {
                        gravity = Gravity.CENTER
                        setOnClickListener {
                            number?.append("*")
                        }
                    }.lparams {
                        weight = 1f
                        width = matchParent
                        height = matchParent
                    }
                    button("0") {
                        gravity = Gravity.CENTER
                        setOnClickListener {
                            number?.append("0")
                        }
                    }.lparams {
                        weight = 1f
                        width = matchParent
                        height = matchParent

                    }
                    button("#") {
                        gravity = Gravity.CENTER
                        setOnClickListener {
                            number?.append("#")
                        }
                    }.lparams {
                        weight = 1f
                        width = matchParent
                        height = matchParent

                    }
                }.lparams{
                    weightSum = 3f
                    width = matchParent
                    height = wrapContent
                    weight =1f
                }
                button(R.string.back) {
                    setOnClickListener {
                        if(!number!!.text.isEmpty())
                            number!!.text = number!!.text.substring(0, number!!.text.lastIndex)
                    }
                }.lparams {
                    height = matchParent
                    width = matchParent
                    weight = 1f
                }
                button(R.string.call) {
                        setOnClickListener{

                            makeCall(number!!.text.toString())
                        }
                }.lparams {
                    height = matchParent
                    width = matchParent
                    weight = 1f
                }


            }.lparams {
                weightSum = 6f
                width = matchParent
                height = wrapContent

            }
        }.applyRecursively(customStyle)
    }

}