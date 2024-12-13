package com.shawonshagor0.catchthebirds

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoadingScreen : AppCompatActivity() {
    lateinit var btnNewGame: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)
        btnNewGame = findViewById(R.id.btnNewGame)
        btnNewGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }


    }
}