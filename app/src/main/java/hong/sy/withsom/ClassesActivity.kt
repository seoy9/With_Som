package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.createclass.ClassNameActivity
import hong.sy.withsom.createclass.ClassScheduleActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassesBinding
import hong.sy.withsom.recyclerView.ClassRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator

class ClassesActivity : AppCompatActivity() {
    lateinit var classRecyclerViewAdapter: ClassRecyclerViewAdapter
    val datas = mutableListOf<ClassData>()
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

        datas.apply {
            add(ClassData(1, "솜솜덕질", "취미", "솜솜이를 덕질해보자!", "동덕여대", 0, 5, "솜솜이를 사랑하는 학우", "매주, 월, 수, 금", "유동적으로 활동", "1@dongduk.ac.kr", "솜덕"))
            add(ClassData(2, "정보처리기사", "자격증", "정보처리기사 자격 취득", "숭인관", 0, 3, "정처기 필요한 사람", "매주, 화, 목", "화, 목 6시 이후", "2@dongduk.ac.kr", "컴과솜"))
            add(ClassData(3, "만 보 걷기", "운동", "건강해지기", "백주년기념관", 0, 10, "만 보 챌린지 할 사람", "매주, 토, 일", "주말 낮", "3@dongduk.ac.kr", "체과촘"))

            classRecyclerViewAdapter.datas = datas
            classRecyclerViewAdapter.notifyDataSetChanged()
        }

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