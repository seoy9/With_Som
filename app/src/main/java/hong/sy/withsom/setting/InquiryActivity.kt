package hong.sy.withsom.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.*
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.databinding.ActivityInquiryBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.mail.GMailSender

class InquiryActivity : AppCompatActivity() {
    lateinit var binding: ActivityInquiryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInquiryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnSendInquiry.setOnClickListener {
            val email = "dongduk.withsom@gmail.com"
            val id = SharedPreferenceManager.getUserEmail(this)
            val name = SharedPreferenceManager.getUserName(this)
            val inquiryContent = binding.edInquiryContent.text.toString()

            GMailSender().sendEmail(email, "${id} ${name} 님의 문의", "${id} ${name} 님의 문의\n\n${inquiryContent}")
            Toast.makeText(this, "문의 완료!\n감사합니다 :)", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnSettingInquiry.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesInquiry.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeInquiry.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchInquiry.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}