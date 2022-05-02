package com.soninhodobeb.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.soninhodobeb.R

class PlayerControl(ctx: Context, attrs: AttributeSet? = null) : LinearLayout(ctx, attrs) {
    private var volumeSeeker = findViewById<SeekBar>(R.id.volume_bar)
    private var tvId = findViewById<TextView>(R.id.tv_sound_id)
    private var tvName = findViewById<TextView>(R.id.tv_sound_name)

    var volume: Int = 0
        set(value) {
            field = value
            volumeSeeker.progress = value
        }

    var soundId: Int = 0
        set(value) {
            field = value
            tvId.text = context.getString(R.string.sound_id_template, value)
        }

    var soundName: String = ""
        set(value) {
            field = value
            tvName.text = value
        }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.player_control, this)
        val styleAttributes = context.obtainStyledAttributes(attrs, R.styleable.PlayerControl)
        try {
            val text = styleAttributes.getText(R.styleable.PlayerControl_text)
            val id = styleAttributes.getResourceId(R.styleable.PlayerControl_id, 0)
            val volume = styleAttributes.getResourceId(R.styleable.PlayerControl_volume, 0)
            if (id != 0) {
                findViewById<TextView>(R.id.tv_sound_name).text = text
                findViewById<TextView>(R.id.tv_sound_id).text =
                    context.getString(R.string.sound_id_template, id)
                findViewById<SeekBar>(R.id.volume_bar).progress = volume
            }
        } finally {
            styleAttributes.recycle()
        }
    }

}