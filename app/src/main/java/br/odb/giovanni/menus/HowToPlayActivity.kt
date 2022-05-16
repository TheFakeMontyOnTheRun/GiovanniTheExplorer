package br.odb.giovanni.menus

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.odb.giovanni.R

class HowToPlayActivity : AppCompatActivity(), View.OnClickListener {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_how_to_play)
		findViewById<View>(R.id.btnBack).setOnClickListener(this)
	}

	override fun onClick(arg0: View) {
		finish()
	}
}