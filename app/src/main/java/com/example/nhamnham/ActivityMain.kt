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
            val bluePlayerName = binding.bluePlayerInput.text.toString()
            val orangePlayerName = binding.orangePlayerInput.text.toString()

            if (bluePlayerName.isEmpty() || orangePlayerName.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.toast_fill_player_names),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ActivityGame::class.java)
                intent.putExtra("blue_player_name", bluePlayerName)
                intent.putExtra("orange_player_name", orangePlayerName)
                intent.putExtra("orange_starts", binding.orangeStartsSwitch.isChecked)
                startActivity(intent)
            }
        }

        binding.rulesBtn.setOnClickListener {
            showGameRulesDialog()
        }
    }

    private fun showGameRulesDialog() {
        val rulesText = getString(R.string.rules_txt)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.rules_dialog_title))
            .setMessage(rulesText)
            .setPositiveButton(getString(R.string.close_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}