package hong.sy.withsom.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.MainActivity
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.setting.SettingActivity
import hong.sy.withsom.data.NoticeData
import hong.sy.withsom.databinding.ActivityNoticeDetailBinding

class NoticeDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoticeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notice = intent.getSerializableExtra("notice") as NoticeData

        setting(notice)

        buttonSetting()
    }

    private fun setting(notice : NoticeData) {
        binding.tvTitleNoticeDetail.text = notice.title
        binding.tvDateNoticeDetail.text = notice.date
        binding.tvContentNoticeDetail.text = notice.content
    }

    private fun buttonSetting() {
        binding.btnHomeNoticeDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesNoticeDetail.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchNoticeDetail.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingNoticeDetail.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}