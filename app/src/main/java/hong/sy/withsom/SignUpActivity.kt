package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivitySignUpBinding
import hong.sy.withsom.mail.GMailSender

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupNext.setOnClickListener {
            if(checkContent() && checkPw() && checkPrivacy()) {
                val email = binding.edEmail.text.toString()
                val certificationNum = "1234"
                
                GMailSender().sendEmail(email, "이메일 인증번호입니다.", "${email} 님의 인증번호 : ${certificationNum}")
                Toast.makeText(this, "인증번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, CheckEmailActivity::class.java)
                startActivity(intent)
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

        if(name.isNullOrBlank() || stNum.isNullOrBlank() || depart.isNullOrBlank() || email.isNullOrBlank() || pw.isNullOrBlank() || repw.isNullOrBlank()) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkPw() : Boolean {
        val pw = binding.edPw.text.toString()
        val repw = binding.edPwCheck.text.toString()

        if(pw != repw) {
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkPrivacy() : Boolean {
        if(!binding.checkBoxPrivacy.isChecked) {
            Toast.makeText(this, "개인정보 취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}