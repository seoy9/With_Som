package hong.sy.withsom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.databinding.ActivityFindPwBinding
import hong.sy.withsom.mail.GMailSender

class FindPwActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnFindPw.setOnClickListener {
            val regex = "(\\d+)@dongduk.ac.kr".toRegex()

            if(!(binding.edEmailFindPw.text.toString().matches(regex))) {
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {

                var email = binding.edEmailFindPw.text.toString()
                val temporaryPW = "3333"

                GMailSender().sendEmail(
                    email,
                    "임시 비밀번호입니다.",
                    "${email} 님의 임시 비밀번호 : ${temporaryPW}"
                )
                Toast.makeText(this, "임시 비밀번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoLogin.setOnClickListener {
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edEmailFindPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)

                if (p0.toString().length == 22) {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnFindPw.setEnabled(true)
                } else {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnFindPw.setEnabled(false)
                }
            }
        })
    }
}
