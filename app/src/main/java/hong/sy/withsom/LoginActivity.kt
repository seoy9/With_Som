package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.databinding.ActivityLoginBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.room.UserDao
import hong.sy.withsom.room.UserDatabase
import hong.sy.withsom.room.UserEntity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isIDdone = false
    private var isPWdone = false

    lateinit var db: UserDatabase
    lateinit var userDao: UserDao
    private lateinit var user: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabase.getInstance(this)!!
        userDao = db.getUserDao()

        textchangedSetting()

        buttonSetting()

        if(SharedPreferenceManager.getUserEmail(this).isNullOrBlank() || SharedPreferenceManager.getUserPass(this).isNullOrBlank()) {
            startLogin()
        } else {
            isExistence(SharedPreferenceManager.getUserEmail(this))

            Toast.makeText(this, "${user.name}님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startLogin() {
        binding.btnLoginDone.setOnClickListener {
            // 디비에 이메일이 있는지 확인
            val email = binding.edLoginEmail.text.toString()
//            val pw = binding.edLoginPw.text.toString()

            isExistence(email)

//            if(!isExistence(email)) {
//                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
//                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
//                Toast.makeText(this, "사용자가 없습니다.\n이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
//            } else {
//                if(user.pw == pw) {
//                    SharedPreferenceManager.setUserEmail(this, user.id.toString())
//
//                    if(binding.checkBoxAutologin.isChecked) {
//                        SharedPreferenceManager.setUserEmail(this, binding.edLoginEmail.text.toString())
//                        SharedPreferenceManager.setUserPass(this, binding.edLoginPw.text.toString())
//                    }
//
//                    Toast.makeText(this, "${user.name}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                    finishAffinity()
//                } else {
//                    binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
//                    binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
//                    Toast.makeText(this, "비밀번호가 틀렸습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
//                }
//            }


            // 이메일과 비밀번호가 맞는지 확인
//            val regex = "(\\d+)@dongduk.ac.kr".toRegex()
//
//            if(!(email.matches(regex))) {
//                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
//                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
//                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
//                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
//
//                if(binding.checkBoxAutologin.isChecked) {
//                    SharedPreferenceManager.setUserId(this, binding.edLoginEmail.text.toString())
//                    SharedPreferenceManager.setUserPass(this, binding.edLoginPw.text.toString())
//                }
//
//                Toast.makeText(this, "${SharedPreferenceManager.getUserId(this)}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                finishAffinity()
//            }
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

    private fun isExistence(email: String) {
        var isCheck = false

        Thread {
            val user = userDao.selectEmailUser(email)

            if(user != null) {
                isCheck = true
            }

            if(isCheck) {
                login(user)
            } else {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(object: Runnable {
                    override fun run() {
                        Toast.makeText(this@LoginActivity, "사용자가 없습니다.\n이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }, 0)
//                Toast.makeText(this, "사용자가 없습니다.\n이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun login(user: UserEntity) {
        val pw = binding.edLoginPw.text.toString()

        if(user.pw == pw) {
            SharedPreferenceManager.setUserEmail(this, user.id.toString())

            if(binding.checkBoxAutologin.isChecked) {
                SharedPreferenceManager.setUserEmail(this, binding.edLoginEmail.text.toString())
                SharedPreferenceManager.setUserPass(this, binding.edLoginPw.text.toString())
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object: Runnable {
                override fun run() {
                    Toast.makeText(this@LoginActivity, "${user.name}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, 0)

//            Toast.makeText(this, "${user.name}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            finishAffinity()
        } else {
            binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
//            Toast.makeText(this, "비밀번호가 틀렸습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object: Runnable {
                override fun run() {
                    Toast.makeText(this@LoginActivity, "비밀번호가 틀렸습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }, 0)
        }
    }
}