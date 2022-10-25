package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.databinding.ActivityNoticeDetailBinding

class NoticeDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoticeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title").toString()
        val date = intent.getStringExtra("date").toString()
        val content = intent.getStringExtra("content").toString()

        setting(title, date, content)


    }

    private fun setting(title: String, date: String, content: String) {
        binding.tvTitleNoticeDetail.text = title
        binding.tvDateNoticeDetail.text = date
        binding.tvContentNoticeDetail.text = content
    }

    private fun buttonSetting() {
        binding.btnHomeNoticeDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnClassesNoticeDetail.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchNoticeDetail.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingNoticeDetail.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}