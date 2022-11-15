package hong.sy.withsom.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.MainActivity
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.setting.SettingActivity
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
//        noticeAdapter = NoticeRecyclerViewAdapter(this)
//        binding.rvNotice.adapter = noticeAdapter
//
//        notices.apply {
//            add(NoticeData("[첫 공지] With Som 서비스가 시작되었습니다!", "2022-10-21", "안녕하세요. :) 개발자 홍서연입니다.\nWith Som 서비스가 드디어 시작되었습니다!\n많은 이용 부탁드립니다."))
//            add(NoticeData("[업데이트 공지] 10/24 With Som 업데이트", "2022-10-24", "안녕하세요. With Som 개발자입니다.\n모임 상세 정보, 설정 페이지가 생성되었습니다."))
//            add(NoticeData("[업데이트 공지] 10/25 With Som 업데이트", "2022-10-25", "안녕하세요. With Som 개발자입니다.\n메인 공지 자동스크롤, 검색, 모임 상세 정보, 공지 페이지가 생성되었습니다."))
//
//            noticeAdapter.notices = notices
//            noticeAdapter.notifyDataSetChanged()
//        }

        val database = Firebase.database
        val myRef = database.getReference("notices")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val nid = classSnapshot.child("nid").getValue(Int::class.java)
                        val name = classSnapshot.child("title").getValue(String::class.java)
                        val date = classSnapshot.child("date").getValue(String::class.java)
                        val content = classSnapshot.child("content").getValue(String::class.java)

                        val notice = NoticeData(nid!!, name!!, date!!, content!!)
                        notices.add(notice)
                    }
                    noticeAdapterSetting()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun noticeAdapterSetting() {
        noticeAdapter = NoticeRecyclerViewAdapter(this)
        binding.rvNotice.adapter = noticeAdapter

        noticeAdapter.notices = notices
        noticeAdapter.notifyDataSetChanged()
    }

    private fun buttonSetting() {
        binding.btnHomeNotice.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesNotice.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchNotice.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingNotice.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}