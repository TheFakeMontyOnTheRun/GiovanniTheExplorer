package br.odb.giovanni.menus

import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import br.odb.giovanni.R

class ItCameFromTheCaveActivity : AppCompatActivity() {

	private var view: ItCameView? = null
	private var mp: MediaPlayer? = null
	private var hasSound: Boolean = false

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		hasSound = intent.extras!!.getBoolean("hasSound")
		view = ItCameView(this, hasSound)
		setContentView(view)
		setResult(RESULT_CANCELED)
	}

	override fun onPause() {
		if (mp != null) {
			mp!!.pause()
			mp!!.release()
		}

		mp = null
		view!!.playing = false

		super.onPause()
	}

	override fun onResume() {
		super.onResume()
		if (hasSound) {
			mp = MediaPlayer.create(this, R.raw.ingame)
			mp!!.isLooping = true
			mp!!.start()
		}
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return view!!.onKeyDown(keyCode, event)
	}

	override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
		return view!!.onKeyUp(keyCode, event)
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		view!!.playing = hasFocus

		super.onWindowFocusChanged(hasFocus)
	}
}