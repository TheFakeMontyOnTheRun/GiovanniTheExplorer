package br.odb.giovanni.menus

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import br.odb.giovanni.R

class MainMenuActivity : AppCompatActivity(), View.OnClickListener,
	CompoundButton.OnCheckedChangeListener {

	private lateinit var swEnableSound: Switch
	private var mp: MediaPlayer? = null

	override fun onPause() {
		if (mp != null) {
			mp!!.pause()
			mp!!.release()
		}
		mp = null
		super.onPause()
	}

	override fun onResume() {
		super.onResume()
		if (swEnableSound.isChecked) {
			mp = MediaPlayer.create(this, R.raw.title)
			mp!!.isLooping = true
			mp!!.start()
		}
	}


	override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
		if (isChecked) {
			mp = MediaPlayer.create(this, R.raw.title)
			mp!!.isLooping = true
			mp!!.start()
		} else if (mp != null) {
			mp!!.stop()
			mp = null
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_main_menu)
		findViewById<View>(R.id.btnStartGame).setOnClickListener(this)
		findViewById<View>(R.id.btnHelp).setOnClickListener(this)
		swEnableSound = findViewById(R.id.swEnableSound)
		(findViewById<View>(R.id.btnStartGame) as Button).requestFocus()
		swEnableSound.isChecked = (application as GiovanniApplication)
			.mayEnableSound()

		swEnableSound.setOnCheckedChangeListener(this)
	}

	override fun onClick(v: View) {
		var intent: Intent? = null
		when (v.id) {
			R.id.btnStartGame -> {
				intent = Intent(this, ItCameFromTheCaveActivity::class.java)
				val bundle = Bundle()
				bundle.putBoolean("hasSound", swEnableSound.isChecked)
				intent.putExtras(bundle)
			}
			R.id.btnHelp -> intent = Intent(this, HowToPlayActivity::class.java)
		}
		this.startActivityForResult(intent, 1)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == 1) {
			if (resultCode == RESULT_OK && data != null) {
				val result = data.getStringExtra("result")
				val intent = Intent(this, ShowGameOutcomeActivity::class.java)
				val bundle = Bundle()
				bundle.putString("result", result)
				bundle.putBoolean("hasSound", swEnableSound.isChecked)
				intent.putExtras(bundle)
				this.startActivity(intent)
			}
		}
	}
}