    package com.example.dingjie.elder_launcher_2.UI

    import android.graphics.Color
    import android.graphics.Typeface
    import android.support.v4.content.res.ResourcesCompat
    import android.util.Log
    import android.view.Gravity
    import android.view.View
    import android.widget.GridLayout
    import android.widget.ImageView
    import android.widget.TextClock
    import android.widget.TextView
    import com.example.dingjie.elder_launcher_2.MainActivity
    import com.example.dingjie.elder_launcher_2.R
    import org.jetbrains.anko.*

    /**
    * Created by dingjie on 2018/3/19.
    */
    class MainActivityUI : AnkoComponent<MainActivity> {
    val TextView.chango: Typeface? get() =
     ResourcesCompat.getFont(this.context, R.font.chango)

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        verticalLayout {

            gravity = Gravity.CENTER
            padding = dip(20)
            linearLayout{
                textClock {
                    typeface= chango
                    textColor = Color.YELLOW
                    textSize =35f
                }.lparams(height = wrapContent , width = wrapContent , weight = 0.40f)
                button("SOS" ) {
                    id = R.id.sosbutton
                    backgroundResource= R.drawable.sos_button
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    //backgroundColorResource = R.color.red
                    typeface = chango
                    gravity = Gravity.BOTTOM
                    textColor = Color.BLACK


                    /*onClick {
                        toast("Hey ${name.text}! Thank you for contacting us. We will get in touch with you soon.")
                    }*/
                }.lparams(height = wrapContent , width = wrapContent , weight = 0.6f)
            }


            linearLayout {
                verticalLayout {


                        imageView {

                            imageResource = R.drawable.contact
                            id = R.id.contacts

                        }.lparams {

                        }


                    textView {
                        typeface = chango
                        textResource = R.string.contact
                        textAlignment = View.TEXT_ALIGNMENT_CENTER


                    }.lparams {

                    }
                }.lparams{
                    weight = 0.5f
                }
                verticalLayout {
                    imageView {
                        id = R.id.chat
                        imageResource = R.drawable.schedule


                    }
                    textView {
                        typeface = chango
                        textResource = R.string.note
                        textAlignment = View.TEXT_ALIGNMENT_CENTER


                    }
                }.lparams{
                    weight = 0.5f
                }
            }

            linearLayout {
                verticalLayout {
                    imageView {
                        imageResource = R.drawable.game
                        id = R.id.game
                        textAlignment = View.TEXT_ALIGNMENT_CENTER

                    }
                    textView {
                        typeface = chango
                        textResource = R.string.game
                        textAlignment = View.TEXT_ALIGNMENT_CENTER


                    }
                }.lparams{
                    weight = 0.5f
                }
                verticalLayout {
                    imageView {
                        imageResource = R.drawable.more
                        id = R.id.more

                    }
                    textView {
                        typeface = chango
                        textResource = R.string.more
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                }.lparams{
                    weight = 0.5f
                }
            }




        }
    }
    }