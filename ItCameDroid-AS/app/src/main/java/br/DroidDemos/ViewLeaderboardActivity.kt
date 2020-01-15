package br.DroidDemos

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView

class ViewLeaderboardActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_leaderboard)
        findViewById<View>(R.id.btnBackToMenu).setOnClickListener(this)
        val wvScores = findViewById<View>(R.id.wvScores) as WebView
        wvScores.loadUrl("http://www.montyprojects.com/scoreboard/leaderboard.php?game=3&link=1")
    }

    override fun onClick(v: View) {
        finish()
    }
}