package br.odb.giovanni.menus

import android.app.Activity
import android.os.Bundle
import android.view.View
import br.odb.giovanni.R


class HowToPlayActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)
        findViewById<View>(R.id.btnBack).setOnClickListener(this)
    }

    override fun onClick(arg0: View) {
        finish()
    }
}