package com.example.nhamnham

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nhamnham.databinding.ActivityGameBinding

class ActivityGame : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bluePlayerName = intent.getStringExtra("blue_player_name")
        val orangePlayerName = intent.getStringExtra("orange_player_name")
        val orangeStarts = intent.getBooleanExtra("orange_starts", false)

        if(bluePlayerName == null || orangePlayerName == null) {
            finish()
            return
        }

        binding.orangePlayerTxt.text = orangePlayerName
        binding.bluePlayerTxt.text = bluePlayerName

        val gameManager = GameManager(this, binding, bluePlayerName, orangePlayerName, orangeStarts)
        gameManager.setupGame()

        binding.resetBtn.setOnClickListener {
            val intent = Intent(this, ActivityGame::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("blue_player_name", bluePlayerName)
                putExtra("orange_player_name", orangePlayerName)
                putExtra("orange_starts", orangeStarts)
            }
            startActivity(intent)
            finish()
        }
    }
}