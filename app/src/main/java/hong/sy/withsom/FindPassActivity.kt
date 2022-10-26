package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivityFindPassBinding
import hong.sy.withsom.mail.GMailSender

class FindPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnFindPw.setOnClickListener {
            var email = binding.edEmailFindPw.text.toString()
            val temporaryPW = "3333"

            Toast.makeText(this, email, Toast.LENGTH_SHORT).show()

            GMailSender().sendEmail(email, "임시 비밀번호입니다." , "${email} 님의 임시 비밀번호 : ${temporaryPW}")
            Toast.makeText(this, "임시 비밀번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnGoLogin.setOnClickListener {
            finish()
        }
    }
}
