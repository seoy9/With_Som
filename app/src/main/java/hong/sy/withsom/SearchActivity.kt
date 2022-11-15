package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivitySearchBinding
import hong.sy.withsom.recyclerView.SearchRecyclerViewAdapter
import hong.sy.withsom.setting.SettingActivity
import java.util.*

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding

    lateinit var searchAdapter: SearchRecyclerViewAdapter
    val classList = ArrayList<ClassData>()

    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() > backPressedTime + 2500) {
            backPressedTime = System.currentTimeMillis()

            Toast.makeText(this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()

            return
        }

        if(System.currentTimeMillis() <= backPressedTime + 2500) {
            finishAffinity()
        }
    }

    private fun initRecycler() {
        val database = Firebase.database
        val myRef = database.getReference("classes")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val cid = classSnapshot.child("cid").getValue(Int::class.java)
                        val name = classSnapshot.child("name").getValue(String::class.java)
                        val type = classSnapshot.child("type").getValue(String::class.java)
                        val content = classSnapshot.child("content").getValue(String::class.java)
                        val location = classSnapshot.child("location").getValue(String::class.java)
                        val currentNum = classSnapshot.child("currentNum").getValue(Int::class.java)
                        val totalNum = classSnapshot.child("totalNum").getValue(Int::class.java)
                        val member = classSnapshot.child("member").getValue(String::class.java)
                        val schedule = classSnapshot.child("schedule").getValue(String::class.java)
                        val scheduleDetail = classSnapshot.child("scheduleDetail").getValue(String::class.java)
                        val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)
                        val leaderContent = classSnapshot.child("leaderContent").getValue(String::class.java)

                        if(totalNum != currentNum) {
                            val c = ClassData(
                                cid!!,
                                name!!,
                                type!!,
                                content!!,
                                location!!,
                                currentNum!!,
                                totalNum!!,
                                member!!,
                                schedule!!,
                                scheduleDetail!!,
                                leaderID!!,
                                leaderContent!!
                            )
                            classList.add(c)
                        }
                    }
                    searchAdapterSetting()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun searchAdapterSetting() {
        searchAdapter = SearchRecyclerViewAdapter(classList, this)
        binding.rvSearch.adapter = searchAdapter
        searchAdapter.classes = classList
        searchAdapter.notifyDataSetChanged()

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.length == 0) {
                    searchAdapter.classes.clear()
                    initRecycler()
                }
                return false
            }

        })

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