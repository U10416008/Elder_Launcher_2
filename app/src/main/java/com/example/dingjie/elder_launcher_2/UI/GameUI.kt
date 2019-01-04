package com.example.dingjie.elder_launcher_2.UI

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.TextView
import com.example.dingjie.elder_launcher_2.Game.GameActivity
import com.example.dingjie.elder_launcher_2.R
import org.jetbrains.anko.*

class GameUI : AnkoComponent<GameActivity>{
    val TextView.chango: Typeface? get() =
        ResourcesCompat.getFont(this.context, R.font.chango)
    override fun createView(ui: AnkoContext<GameActivity>)= with(ui) {
            verticalLayout {
                textView(R.string.train) {
                    typeface = chango
                    textSize = 50f

                }.lparams{
                    width = matchParent
                    height = wrapContent
                    margin =dip(40)}

                button(R.string.simple) {
                    backgroundResource = R.drawable.simple
                    id = R.id.simple
                    typeface = chango
                    textSize = 50f

                }.lparams{
                    width = matchParent
                    height = wrapContent
                    margin =dip(30)}

                button(R.string.middle) {
                    backgroundResource = R.drawable.simple
                    id = R.id.simple
                    id = R.id.middle
                    typeface = chango
                    textSize = 50f

                }.lparams{
                    width = matchParent
                    height = wrapContent
                    margin =dip(30)}
                button(R.string.hard) {
                    backgroundResource = R.drawable.simple
                    id = R.id.simple
                    id = R.id.hard
                    typeface = chango
                    textSize = 50f

                }.lparams{
                    width = matchParent
                    height = wrapContent
                    margin =dip(30)}

            }

    }

}