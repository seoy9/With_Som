package hong.sy.withsom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private fun initRecycler() {
        classRecyclerViewAdapter = ClassRecyclerViewAdapter(this)
        binding.rvClasses.adapter = classRecyclerViewAdapter

        datas.apply {
            add(ClassData(R.drawable.foundation, "솜솜덕질", "취미", "솜솜이를 덕질해보자!"))
            add(ClassData(R.drawable.simbol, "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성"))
            add(ClassData(R.drawable.vision, "만 보 걷기", "운동", "건강해지자!!"))

            classRecyclerViewAdapter.datas = datas
            classRecyclerViewAdapter.notifyDataSetChanged()
        }

        binding.rvClasses.addItemDecoration(VerticalItemDecorator(20))
        binding.rvClasses.addItemDecoration(HorizontalItemDecorator(10))
    }
}