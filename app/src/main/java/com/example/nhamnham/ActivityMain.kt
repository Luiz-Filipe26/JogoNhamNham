package com.example.nhamnham

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nhamnham.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.startBtn.setOnClickListener {
            val intent = Intent(this, ActivityGame::class.java)
            startActivity(intent)
        }

        binding.rulesBtn.setOnClickListener {
            showGameRulesDialog()
        }
    }

    private fun showGameRulesDialog() {
        val rulesText = getText(R.string.rules_txt)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Regras do Jogo")
            .setMessage(rulesText)
            .setPositiveButton("Fechar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
