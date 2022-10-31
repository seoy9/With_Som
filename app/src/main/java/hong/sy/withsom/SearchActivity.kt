package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivitySearchBinding
import hong.sy.withsom.recyclerView.SearchRecyclerViewAdapter
import hong.sy.withsom.room.ClassDao
import hong.sy.withsom.room.ClassDatabase
import hong.sy.withsom.room.ClassEntity
import java.util.*

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding

    lateinit var searchAdapter: SearchRecyclerViewAdapter

    private lateinit var db: ClassDatabase
    private lateinit var classDao: ClassDao
    private lateinit var classList: ArrayList<ClassEntity>

    private lateinit var search: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        search = binding.search.toString()

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        searchAdapter = SearchRecyclerViewAdapter(this)
        binding.rvSearch.adapter = searchAdapter

        Thread {
            classList = ArrayList(classDao.searchClass(search))

            searchAdapter.classes = classList
            searchAdapter.notifyDataSetChanged()
        }.start()
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