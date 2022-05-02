package com.soninhodobeb.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.soninhodobeb.R

class SoundButton(ctx: Context, attrs: AttributeSet? = null) : RelativeLayout(ctx, attrs) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.sound_button, this)

        val styleAttributes = context.obtainStyledAttributes(attrs, R.styleable.SoundButton)
        try {
            val btnText = styleAttributes.getText(R.styleable.SoundButton_text)
            val drawable = styleAttributes.getResourceId(R.styleable.SoundButton_image, 0)
            if (drawable != 0) {
                this.findViewById<TextView>(R.id.tv_btn_text).text = btnText
                this.findViewById<ImageView>(R.id.btn_img).setImageResource(drawable)
            }
        } finally {
            styleAttributes.recycle()
        }
    }

    fun setImage(drawable: Drawable?) {
        this.findViewById<ImageView>(R.id.btn_img).setImageDrawable(drawable)
    }

    fun setText(text: String) {
        this.findViewById<TextView>(R.id.tv_btn_text).text = text
    }
}