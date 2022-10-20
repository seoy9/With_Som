package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.databinding.ActivitySignUpImageBinding

class SignUpImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignupDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}