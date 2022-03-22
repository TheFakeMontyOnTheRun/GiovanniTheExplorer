package br.odb.giovanni.menus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent

class ItCameFromTheCaveActivity : AppCompatActivity() {

    private var view: ItCameView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ItCameView(this, intent.extras!!.getBoolean("hasSound"))
        setContentView(view)
        setResult(RESULT_CANCELED)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return view!!.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        view!!.playing = false

        super.onPause()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return view!!.onKeyUp(keyCode, event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        view!!.playing = hasFocus

        super.onWindowFocusChanged(hasFocus)
    }
}