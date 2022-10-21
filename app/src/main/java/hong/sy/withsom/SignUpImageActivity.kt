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

        binding.btnSignupDone.setOnClickListener {
            val intent = Intent(this@SignUpImageActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.imgFoundation.setOnClickListener {
            binding.imgSelected.setImageResource(0)
            binding.imgSelected.setImageResource(R.drawable.foundation)
            Toast.makeText(this, "F Click!", Toast.LENGTH_SHORT).show()
        }

        binding.imgVision.setOnClickListener {
            binding.imgSelected.setImageResource(0)
            binding.imgSelected.setImageResource(R.drawable.foundation)
            Toast.makeText(this, "V Click!", Toast.LENGTH_SHORT).show()
        }

        binding.imgSince.setOnClickListener {
            binding.imgSelected.setImageResource(0)
            binding.imgSelected.setImageResource(R.drawable.foundation)
            Toast.makeText(this, "S Click!", Toast.LENGTH_SHORT).show()
        }

        binding.imgSimbol.setOnClickListener {
            binding.imgSelected.setImageResource(0)
            binding.imgSelected.setImageResource(R.drawable.foundation)
            Toast.makeText(this, "B Click!", Toast.LENGTH_SHORT).show()
        }
    }
}