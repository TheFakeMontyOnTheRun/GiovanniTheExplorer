package br.odb.giovanni.menus

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import br.odb.giovanni.R

class ShowGameOutcomeActivity : Activity(), View.OnClickListener {
    private var btnBack: Button? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_game_outcome)
        val outcome1 = "Parabéns, você fugiu da caverna!"
        val outcome2 = "Que pena! Você foi derrotado. Fim de jogo!"
        val victory = (intent.extras!!.getString("result")
                == "victory")
        val tvOutcome: TextView = findViewById<View>(R.id.tvOutcome) as TextView
        tvOutcome.text = if (victory) outcome1 else outcome2
        tvOutcome.setTextColor(if (victory) Color.BLACK else Color.WHITE)
        this.title = if (victory) outcome1 else outcome2
        if (ItCameView.playSounds) {
            MediaPlayer.create(this, if (victory) R.raw.win else R.raw.gameover).start()
        }
        btnBack = findViewById<View>(R.id.btnBack) as Button
        btnBack!!.setOnClickListener(this)
        val iv = findViewById<View>(R.id.ivOutcome) as ImageView
        iv.setImageResource(if (victory) R.drawable.end_victory else R.drawable.end_gameover)
        iv.imageAlpha = if (victory) 255 else 16
    }

    override fun onClick(v: View) {
        finish()
    }
}