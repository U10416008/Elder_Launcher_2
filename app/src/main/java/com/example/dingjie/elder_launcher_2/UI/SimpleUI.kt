package com.example.dingjie.elder_launcher_2.UI


import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import com.example.dingjie.elder_launcher_2.Game.SimpleGame
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*

class SimpleUI: AnkoComponent<SimpleGame> {
    private val customStyle = { v: Any ->
        when (v) {
            is ImageView -> v.scaleType = ImageView.ScaleType.FIT_CENTER

        }
    }
    override fun createView(ui: AnkoContext<SimpleGame>) = with(ui) {
        verticalLayout {
            linearLayout {
                imageView {
                    imageResource = R.drawable.contacts
                    id = R.id.question

                }
            }.lparams(width = matchParent , height = matchParent ,weight = 0.4f)
            linearLayout {

                linearLayout() {
                    imageView {

                        imageResource = R.drawable.contacts
                        id = R.id.image1
                    }
                }.lparams {
                    //width = MainActivity.screenWidth / 3
                    //height = MainActivity.screenHeight / 3
                    weight = 0.5f

                }
                linearLayout() {
                    imageView{
                        visibility = View.INVISIBLE
                        imageResource = R.drawable.contacts
                        id = R.id.image2
                    }
                }.lparams {
                    //width = MainActivity.screenWidth/3
                    //height =  MainActivity.screenWidth/3
                    weight = 0.5f

                }
            }.lparams(width = matchParent , height = matchParent,weight = 0.3f )
            linearLayout {

                linearLayout() {
                    imageView {
                        visibility = View.INVISIBLE

                        imageResource = R.drawable.contacts
                        id = R.id.image3
                    }
                }.lparams {
                    //width = MainActivity.screenWidth / 3
                    //height = MainActivity.screenHeight / 3
                    weight = 0.5f

                }
                linearLayout() {
                    imageView{
                        imageResource = R.drawable.contacts
                        id = R.id.image4
                    }
                }.lparams {
                    //width = MainActivity.screenWidth/3
                    //height =  MainActivity.screenWidth/3
                    weight = 0.5f

                }
            }.lparams(width = matchParent , height = matchParent,weight = 0.3f )

        }.applyRecursively(customStyle)
    }
}