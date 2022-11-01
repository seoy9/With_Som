package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivitySearchBinding
import hong.sy.withsom.recyclerView.SearchRecyclerViewAdapter

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding

    lateinit var searchAdapter: SearchRecyclerViewAdapter
    val classes = mutableListOf<ClassData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        searchAdapter = SearchRecyclerViewAdapter(this)
        binding.rvSearch.adapter = searchAdapter

        classes.apply {
            add(ClassData(1, "솜솜덕질", "취미", "솜솜이를 덕질해보자!", "동덕여대", 0, 5, "솜솜이를 사랑하는 학우", "매주, 월, 수, 금", "유동적으로 활동", "1@dongduk.ac.kr", "솜덕"))
            add(ClassData(2, "정보처리기사", "자격증", "정보처리기사 자격 취득", "숭인관", 0, 3, "정처기 필요한 사람", "매주, 화, 목", "화, 목 6시 이후", "2@dongduk.ac.kr", "컴과솜"))
            add(ClassData(3, "만 보 걷기", "운동", "건강해지기", "백주년기념관", 0, 10, "만 보 챌린지 할 사람", "매주, 토, 일", "주말 낮", "3@dongduk.ac.kr", "체과촘"))

            searchAdapter.classes = classes
            searchAdapter.notifyDataSetChanged()
        }
    }
    private fun buttonSetting() {
        binding.btnClassesSearch.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeSearch.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingSearch.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}