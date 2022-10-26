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
            add(ClassData(R.drawable.foundation, "20221234 이솜솜", "솜솜덕질", "취미", "솜솜이를 덕질해보자!", 5, "매일", "동덕여대"))
            add(ClassData(R.drawable.simbol, "20225678 김솜솜", "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성", 6, "월 4~5시", "동덕여대 대학원"))
            add(ClassData(R.drawable.vision, "20229000 박솜솜", "만 보 걷기", "운동", "건강해지자!!", 7, "미정", "동덕여대 백주년"))

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