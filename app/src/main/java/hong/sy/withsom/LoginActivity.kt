package hong.sy.withsom

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.databinding.ActivityLoginBinding
import hong.sy.withsom.login.SharedPreferenceManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isIDdone = false
    private var isPWdone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textchangedSetting()

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
            // 디비에 이메일이 있는지 확인
            // 이메일과 비밀번호가 맞는지 확인
            val regex = "(\\d+)@dongduk.ac.kr".toRegex()

            if(!(binding.edLoginEmail.text.toString().matches(regex))) {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)

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

    private fun textchangedSetting() {
        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)

                if (p0 != null) {
                    if(p0.length >= 22) {
                        isIDdone = true
                    } else {
                        isIDdone = false
                    }
                }

                if(isIDdone && isPWdone) {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnLoginDone.setEnabled(true)
                } else {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnLoginDone.setEnabled(false)
                }
            }
        })

        binding.edLoginPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if(p0.length >= 8) {
                        isPWdone = true
                    } else {
                        isPWdone = false
                    }
                }

                if(isIDdone && isPWdone) {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnLoginDone.setEnabled(true)
                } else {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnLoginDone.setEnabled(false)
                }
            }
        })
    }
}