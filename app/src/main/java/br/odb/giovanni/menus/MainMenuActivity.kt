package br.odb.giovanni.menus

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import br.odb.giovanni.R
import com.google.android.material.switchmaterial.SwitchMaterial

class MainMenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var swEnableSound: SwitchMaterial

    companion object {
        var needsReset = false
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
    }

    override fun onClick(v: View) {
        var intent: Intent? = null
        when (v.id) {
            R.id.btnStartGame -> {
                needsReset = true
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