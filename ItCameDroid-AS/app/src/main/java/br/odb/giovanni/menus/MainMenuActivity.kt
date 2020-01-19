package br.odb.giovanni.menus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import br.odb.giovanni.R
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        findViewById<View>(R.id.btnStartGame).setOnClickListener(this)
        findViewById<View>(R.id.btnHelp).setOnClickListener(this)
        (findViewById<View>(R.id.btnStartGame) as Button).requestFocus()
        findViewById<Switch>(R.id.swEnableSound).isChecked = (application as GiovanniApplication)
                .mayEnableSound()
    }

    override fun onClick(v: View) {
        var intent: Intent? = null
        when (v.id) {
            R.id.btnStartGame -> {
                needsReset = true
                intent = Intent(this, ItCameFromTheCaveActivity::class.java)
                var bundle = Bundle()
                bundle.putBoolean("hasSound", swEnableSound.isChecked)
                intent.putExtras(bundle)
            }
            R.id.btnHelp -> intent = Intent(this, HowToPlayActivity::class.java)
        }
        this.startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    companion object {
        @JvmField
        var needsReset = false
    }
}