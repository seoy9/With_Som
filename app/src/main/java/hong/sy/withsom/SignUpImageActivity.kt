package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivitySignUpImageBinding

class SignUpImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupDone.setOnClickListener {
            val intent = Intent(this@SignUpImageActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnFoundation.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.foundation)
        }

        binding.btnVision.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.vision)
        }

        binding.btnSince.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.since)
        }

        binding.btnSimbol.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.simbol)
        }
    }
}