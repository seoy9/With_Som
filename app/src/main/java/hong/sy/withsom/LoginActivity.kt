package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivityLoginBinding
import hong.sy.withsom.login.SharedPreferenceManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        if(SharedPreferenceManager.getUserId(this).isNullOrBlank() || SharedPreferenceManager.getUserPass(this).isNullOrBlank()) {
            login()
        } else {
            Toast.makeText(this, "${SharedPreferenceManager.getUserId(this)}님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        binding.btnLoginDone.setOnClickListener {

            if(binding.edLoginEmail.text.isNullOrBlank() || binding.edLoginPw.text.isNullOrBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
            } else {
                if(binding.checkBoxAutologin.isChecked) {
                    SharedPreferenceManager.setUserId(this, binding.edLoginEmail.text.toString())
                    SharedPreferenceManager.setUserPass(this, binding.edLoginPw.text.toString())
                }

                Toast.makeText(this, "${SharedPreferenceManager.getUserId(this)}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                finishAffinity()
            }
        }
    }

    private fun buttonSetting() {
        binding.tvFindPw.setOnClickListener {
            val intent = Intent(this, FindPwActivity::class.java)
            startActivity(intent)
        }
    }
}