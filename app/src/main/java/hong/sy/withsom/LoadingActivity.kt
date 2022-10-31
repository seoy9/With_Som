package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivityLoadingBinding
import hong.sy.withsom.login.SharedPreferenceManager

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(SharedPreferenceManager.getUserEmail(this).isNullOrBlank() || SharedPreferenceManager.getUserPass(this).isNullOrBlank()) {
        } else {
            Toast.makeText(this, "${SharedPreferenceManager.getUserEmail(this)}님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}