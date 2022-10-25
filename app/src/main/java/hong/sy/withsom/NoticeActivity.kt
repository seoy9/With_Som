package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.data.NoticeData
import hong.sy.withsom.databinding.ActivityNoticeBinding
import hong.sy.withsom.recyclerView.NoticeRecyclerViewAdapter

class NoticeActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoticeBinding

    lateinit var noticeAdapter: NoticeRecyclerViewAdapter
    val notices = mutableListOf<NoticeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        noticeAdapter = NoticeRecyclerViewAdapter(this)
        binding.rvNotice.adapter = noticeAdapter

        notices.apply {
            add(NoticeData("[첫 공지] With Som 서비스가 시작되었습니다!", "2022-10-21", "안녕하세요. :) 개발자 홍서연입니다.\nWith Som 서비스가 드디어 시작되었습니다!\n많은 이용 부탁드립니다."))
            add(NoticeData("[업데이트 공지] 10/24 With Som 업데이트", "2022-10-24", "안녕하세요. With Som 개발자입니다.\n모임 상세 정보, 설정 페이지가 생성되었습니다."))
            add(NoticeData("[업데이트 공지] 10/25 With Som 업데이트", "2022-10-25", "안녕하세요. With Som 개발자입니다.\n메인 공지 자동스크롤, 검색, 모임 상세 정보, 공지 페이지가 생성되었습니다."))

            noticeAdapter.notices = notices
            noticeAdapter.notifyDataSetChanged()
        }
    }

    private fun buttonSetting() {
        binding.btnHomeNotice.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnClassesNotice.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchNotice.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingNotice.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}