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

        binding.fabMyList.setOnClickListener {
            val intent = Intent(this, MyListActivity::class.java)
            startActivity(intent)
            //finish()
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
