package hong.sy.withsom.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.LoadingActivity
import hong.sy.withsom.MainActivity
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.databinding.ActivitySettingBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.notice.NoticeActivity

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() > backPressedTime + 2500) {
            backPressedTime = System.currentTimeMillis()

            Toast.makeText(this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()

            return
        }

        if(System.currentTimeMillis() <= backPressedTime + 2500) {
            finishAffinity()
        }
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