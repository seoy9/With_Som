package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.createclass.ClassNameActivity
import hong.sy.withsom.createclass.ClassScheduleActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassesBinding
import hong.sy.withsom.recyclerView.ClassRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator
import java.util.*

class ClassesActivity : AppCompatActivity() {
    lateinit var classRecyclerViewAdapter: ClassRecyclerViewAdapter
    private var datas = ArrayList<String>()
    private lateinit var binding: ActivityClassesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        classRecyclerViewAdapter = ClassRecyclerViewAdapter(this)
        binding.rvClasses.adapter = classRecyclerViewAdapter

//        datas.apply {
//            add(ClassData(1, "솜솜덕질", "취미", "솜솜이를 덕질해보자!", "동덕여대", 0, 5, "솜솜이를 사랑하는 학우", "매주, 월, 수, 금", "유동적으로 활동", "1@dongduk.ac.kr", "솜덕"))
//            add(ClassData(2, "정보처리기사", "자격증", "정보처리기사 자격 취득", "숭인관", 0, 3, "정처기 필요한 사람", "매주, 화, 목", "화, 목 6시 이후", "2@dongduk.ac.kr", "컴과솜"))
//            add(ClassData(3, "만 보 걷기", "운동", "건강해지기", "백주년기념관", 0, 10, "만 보 챌린지 할 사람", "매주, 토, 일", "주말 낮", "3@dongduk.ac.kr", "체과촘"))
//
//            classRecyclerViewAdapter.datas = datas
//            classRecyclerViewAdapter.notifyDataSetChanged()
//        }

//        val database = Firebase.database
//        val myRef = database.getReference("classes")
//        myRef.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    for(classSnapshot in snapshot.children) {
//                        val cid = classSnapshot.child("cid").getValue(Int::class.java)
//                        val name = classSnapshot.child("name").getValue(String::class.java)
//                        val type = classSnapshot.child("type").getValue(String::class.java)
//                        val content = classSnapshot.child("content").getValue(String::class.java)
//                        val location = classSnapshot.child("location").getValue(String::class.java)
//                        val currentNum = classSnapshot.child("currentNum").getValue(Int::class.java)
//                        val totalNum = classSnapshot.child("totalNum").getValue(Int::class.java)
//                        val member = classSnapshot.child("member").getValue(String::class.java)
//                        val schedule = classSnapshot.child("schedule").getValue(String::class.java)
//                        val scheduleDetail = classSnapshot.child("scheduleDetail").getValue(String::class.java)
//                        val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)
//                        val leaderContent = classSnapshot.child("leaderContent").getValue(String::class.java)
//
//                        val c = ClassData(cid!!, name!!, type!!, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID!!, leaderContent!!)
//                        datas.add(c)
//                    }
//                    classRecyclerViewAdapter.datas = datas
//                    classRecyclerViewAdapter.notifyDataSetChanged()
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })

        datas.add("취미")
        datas.add("공부")
        datas.add("운동")
        datas.add("자격증")
        datas.add("대회")
        classRecyclerViewAdapter.datas = datas
        classRecyclerViewAdapter.notifyDataSetChanged()

        binding.rvClasses.addItemDecoration(VerticalItemDecorator(20))
        binding.rvClasses.addItemDecoration(HorizontalItemDecorator(10))
    }

    private fun buttonSetting() {
        binding.fabCreate.setOnClickListener {
            val intent = Intent(this, ClassNameActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeClasses.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchClasses.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingClasses.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
