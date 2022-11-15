package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivityLoadingBinding
import hong.sy.withsom.login.LoginActivity
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.signup.SignUpActivity

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(SharedPreferenceManager.getUserId(this).isNullOrBlank() || SharedPreferenceManager.getUserPass(this).isNullOrBlank()) {
        } else {
            Toast.makeText(this, "${SharedPreferenceManager.getUserName(this)}님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonSetting()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() > backPressedTime + 2500) {
            backPressedTime = System.currentTimeMillis()

            Toast.makeText(this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()

            return
        }

        if(System.currentTimeMillis() <= backPressedTime + 2500) {
            finishAffinity()
        }
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