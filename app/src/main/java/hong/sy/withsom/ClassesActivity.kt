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
import hong.sy.withsom.room.ClassDao
import hong.sy.withsom.room.ClassDatabase
import hong.sy.withsom.room.ClassEntity
import java.util.*

class ClassesActivity : AppCompatActivity() {
    lateinit var classRecyclerViewAdapter: ClassRecyclerViewAdapter
//    val datas = mutableListOf<ClassData>()
    private lateinit var binding: ActivityClassesBinding

    private lateinit var db: ClassDatabase
    private lateinit var classDao: ClassDao
    private lateinit var classList: ArrayList<ClassEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ClassDatabase.getInstance(this)!!
        classDao = db.getClassDao()

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        classRecyclerViewAdapter = ClassRecyclerViewAdapter(this)
        binding.rvClasses.adapter = classRecyclerViewAdapter

        Thread {
            classList = ArrayList(classDao.getAll())

            classRecyclerViewAdapter.datas = classList
            classRecyclerViewAdapter.notifyDataSetChanged()
        }.start()

//        datas.apply {
//            add(ClassData(R.drawable.foundation, "20221234 이솜솜", "솜솜덕질", "취미", "솜솜이를 덕질해보자!", 5, "매일", "동덕여대"))
//            add(ClassData(R.drawable.simbol, "20225678 김솜솜", "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성", 6, "월 4~5시", "동덕여대 대학원"))
//            add(ClassData(R.drawable.vision, "20229000 박솜솜", "만 보 걷기", "운동", "건강해지자!!", 7, "미정", "동덕여대 백주년"))
//
//            classRecyclerViewAdapter.datas = datas
//            classRecyclerViewAdapter.notifyDataSetChanged()
//        }

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