package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.UserData
import hong.sy.withsom.databinding.ActivitySignUpBinding
import hong.sy.withsom.mail.GMailSender
import hong.sy.withsom.random.RandomString
import java.io.Serializable

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

//    private var isHave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupNext.setOnClickListener {
            if(checkContent() && checkStNum() && checkEmail() && checkPw() && checkRePw() && checkPrivacy()) {
                isEmailExistence()
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

        binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.black))
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

    private fun isEmailExistence() {
        val id = binding.edEmail.text.toString()
        var isHave = false
        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)

                        if(id == email) {
                            isHave = true
                            isCheckExtension(isHave)
                            break
                        }
                    }
                    isCheckExtension(isHave)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isCheckExtension(isHave: Boolean) {
        if(!isHave) {
            val email = binding.edEmail.text.toString()
            val pw = binding.edPw.text.toString()
            val name = binding.edName.text.toString()
            val stNum = binding.edStNum.text.toString()
            val depart = binding.edDepart.text.toString()
            val user = UserData(stNum, email, pw, name, stNum, depart, -1)

            val certificationNum = RandomString().getRandomCertificationNum()

            GMailSender().sendEmail(
                "20181033@dongduk.ac.kr",
                "이메일 인증번호입니다.",
                "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c914e239-ba64-474b-b066-d556061030f3/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221107%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221107T004323Z&X-Amz-Expires=86400&X-Amz-Signature=3464e7a25057098bec92b232f8a20e60a6fd807d57127c2c25d3573c1dd43c47&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[이메일 인증]</b></h3>\n\n안녕하세요. With Som입니다.\n\n요청하신 아래 인증번호를 진행 중인 화면에 입력하여 이메일 인증을 완료해주세요.\n\n만약, 이메일 인증을 요청하지 않은 경우 <a href='mailto:dongduk.withsom@gmail.com' style='color:#0000FF;'>dongduk.withsom@gmail.com</a>으로 알려주시기 바랍니다.\n\n\n<p style='background-color:#D3D3D3;'>${email} 님의 인증번호 : <b>${certificationNum}</b></p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
            )
            Toast.makeText(this, "인증번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CheckEmailActivity::class.java)
            intent.putExtra("certificationNum", certificationNum)
            intent.putExtra("user", user as Serializable)
            startActivity(intent)
        } else {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
