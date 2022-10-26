package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.databinding.ActivitySettingBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.setting.CorrectionActivity
import hong.sy.withsom.setting.InformationActivity
import hong.sy.withsom.setting.InquiryActivity
import hong.sy.withsom.setting.SecessionActivity

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnLogout.setOnClickListener {
            SharedPreferenceManager.clearUser(this)

            finishAffinity()
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCorrection.setOnClickListener {
            val intent = Intent(this, CorrectionActivity::class.java)
            startActivity(intent)
        }

        binding.btnNotice.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            startActivity(intent)
        }

        binding.btnInquiry.setOnClickListener {
            val intent = Intent(this, InquiryActivity::class.java)
            startActivity(intent)
        }

        binding.btnInformation.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        binding.btnSecession.setOnClickListener {
            val intent = Intent(this, SecessionActivity::class.java)
            startActivity(intent)
        }

        binding.btnHomeSetting.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnClassesSetting.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchSetting.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}