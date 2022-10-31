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
import hong.sy.withsom.data.UserData
import hong.sy.withsom.databinding.ActivitySignUpBinding
import hong.sy.withsom.mail.GMailSender
import hong.sy.withsom.room.UserDao
import hong.sy.withsom.room.UserDatabase
import hong.sy.withsom.room.UserEntity
import java.io.Serializable

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabase.getInstance(this)!!
        userDao = db.getUserDao()

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupNext.setOnClickListener {
            if(checkContent() && checkStNum() && checkEmail() && checkPw() && checkRePw() && checkPrivacy()) {
                val email = binding.edEmail.text.toString()
//                val pw = binding.edPw.text.toString()
//                val name = binding.edName.text.toString()
//                val stNum = binding.edStNum.text.toString()
//                val depart = binding.edDepart.text.toString()

               checkExistence(email)
            }
        }

        binding.checkBoxPrivacy.setOnClickListener {
            if(binding.checkBoxPrivacy.isChecked) {
                binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.black))
            }
        }
    }

    private fun checkContent() : Boolean {
        val name = binding.edName.text
        val stNum = binding.edStNum.text
        val depart = binding.edDepart.text
        val email = binding.edEmail.text
        val pw = binding.edPw.text
        val repw = binding.edPwCheck.text
        var isFalse = false

        if(name.isNullOrBlank()) {
            binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(stNum.isNullOrBlank()) {
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(depart.isNullOrBlank()) {
            binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(email.isNullOrBlank()) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(pw.isNullOrBlank()) {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(repw.isNullOrBlank()) {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            isFalse = true
        } else {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        }

        if(isFalse) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)

        return true
    }

    private fun checkStNum() : Boolean {
        val stNum = binding.edStNum.text.toString()

        if(stNum.length != 8) {
            Toast.makeText(this, "학번을 확인해주세요.", Toast.LENGTH_SHORT).show()
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            return false
        }

        binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        return true
    }

    private fun checkPw() : Boolean {
        val pw = binding.edPw.text.toString()

        if(pw.length < 8) {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "비밀번호는 8자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        return true
    }

    private fun checkRePw() : Boolean {
        val pw = binding.edPw.text.toString()
        val repw = binding.edPwCheck.text.toString()

        if(pw != repw) {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        return true
    }

    private fun checkPrivacy() : Boolean {
        if(!binding.checkBoxPrivacy.isChecked) {
            binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.red))
            Toast.makeText(this, "개인정보 취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkEmail() : Boolean {
        if(!binding.edEmail.text.toString().contains("@dongduk.ac.kr")) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "동덕여대 이메일을 사용해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.edEmail.text.toString().length != 22) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        val stnNum = binding.edEmail.text.toString().subSequence(0, 8)
        val regex = "(\\d+)".toRegex()

        if(!(stnNum.matches(regex))) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
        return true
    }

    private fun textChangedSetting() {
        binding.edName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })

        binding.edStNum.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })

        binding.edDepart.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })

        binding.edEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })

        binding.edPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })

        binding.edPwCheck.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })
    }

    private fun checkExistence(email: String): Boolean {
        var ischeck = true

        Thread {
            val user = userDao.selectEmailUser(email)

            if(user != null) {
                ischeck = false
            }

            if(ischeck) {
                sign()
            } else {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(object: Runnable {
                    override fun run() {
                        Toast.makeText(this@SignUpActivity, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                }, 0)
            }
        }.start()

        return ischeck
    }

    private fun sign() {
        val email = binding.edEmail.text.toString()
        val pw = binding.edPw.text.toString()
        val name = binding.edName.text.toString()
        val stNum = binding.edStNum.text.toString()
        val depart = binding.edDepart.text.toString()
        val certificationNum = "1234"

        GMailSender().sendEmail(
            email,
            "이메일 인증번호입니다.",
            "${name} 님의 인증번호 : ${certificationNum}"
        )

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object: Runnable {
            override fun run() {
                Toast.makeText(this@SignUpActivity, "인증번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()
            }
        }, 0)
//        Toast.makeText(this, "인증번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()

        val user = UserEntity(null, email, pw, name, stNum, depart, -1)

        val intent = Intent(this, CheckEmailActivity::class.java)
        intent.putExtra("user", user as Serializable)
        startActivity(intent)
    }
}
