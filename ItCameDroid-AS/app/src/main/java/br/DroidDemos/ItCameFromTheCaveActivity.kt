package br.DroidDemos

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent

class ItCameFromTheCaveActivity : Activity() {
    var view: ItCameView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ItCameView(this)
        setContentView(view)
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

    //    @Override
//    protected void onPause() {
//
//    	view.playing = false;
//    	super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//    	view.playing = true;
//    	super.onResume();
//    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        view!!.playing = hasFocus
        super.onWindowFocusChanged(hasFocus)
    }
}