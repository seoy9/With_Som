package hong.sy.withsom.classList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.MainActivity
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.setting.SettingActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityMyListBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.recyclerView.MyListRecyclerViewAdapter
import java.io.Serializable
import java.util.*

class MyListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyListBinding

    lateinit var myClassAdapter: MyListRecyclerViewAdapter
    lateinit var myApplicationAdapter: MyListRecyclerViewAdapter
    val classList = ArrayList<ClassData>()
    val applicationList = ArrayList<Int>()
    val applicationClassList = ArrayList<ClassData>()

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        myClassAdapter = MyListRecyclerViewAdapter(this)
//        myApplicationAdapter = MyListRecyclerViewAdapter(this)

        myClassList()
        myApplicationList()

        binding.rvMyclass.visibility = View.VISIBLE
        binding.rvMyapplication.visibility = View.INVISIBLE

        setting()

    }

    override fun onRestart() {
        super.onRestart()
        myClassList()
        myApplicationList()
    }

    override fun onStop() {
        super.onStop()
        classList.clear()
        applicationList.clear()
        applicationClassList.clear()
    }

    private fun setting() {
        binding.listTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text) {
                   "내 모임" -> {
                       binding.rvMyclass.visibility = View.VISIBLE
                       binding.rvMyapplication.visibility = View.INVISIBLE
                   }
                    else -> {
                        binding.rvMyclass.visibility = View.INVISIBLE
                        binding.rvMyapplication.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.btnHomeList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesList.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchList.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingList.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun myApplicationList() {
        applicationList.clear()
        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
        val myRef = database.getReference("applications")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(applicationSnapshot in snapshot.children) {
                        for (snapshot in applicationSnapshot.children) {
                            val cid = snapshot.child("cid").getValue(Int::class.java)
                            val stN = snapshot.child("stNum").getValue(String::class.java)

                            if (stNum == stN!!) {
                                applicationList.add(cid!!)
                            }
                        }
                    }
                    myApplicationClassList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun myApplicationClassList() {
        applicationClassList.clear()
        val myRef = database.getReference("classes")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (classSnapshot in snapshot.children) {
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

                        if(applicationList.contains(cid!!)) {
                            val c = ClassData(cid, name!!, type!!, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID!!, leaderContent!!)
                            applicationClassList.add(c)
                        }
                    }
                    myApplicationAdapterSetting()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun myApplicationAdapterSetting() {
        myApplicationAdapter = MyListRecyclerViewAdapter(this)
        binding.rvMyapplication.adapter = myApplicationAdapter
        myApplicationAdapter.datas = applicationClassList
        myApplicationAdapter.notifyDataSetChanged()

        myApplicationAdapter.setOnItemClickListener(object : MyListRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ClassData, pos: Int) {
                val intent = Intent(this@MyListActivity, ClassDetailActivity::class.java)
                intent.putExtra("data", data as Serializable)
                intent.putExtra("where", "myApplication")
                startActivity(intent)
            }
        })
    }

    private fun myClassList() {
        classList.clear()
        val email = SharedPreferenceManager.getUserEmail(this)
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

                        if(email == leaderID) {
                            val c = ClassData(cid!!, name!!, type!!, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID, leaderContent!!)
                            classList.add(c)
                        }
                    }
                    myClassAdapterSetting()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun myClassAdapterSetting() {
        myClassAdapter = MyListRecyclerViewAdapter(this)
        binding.rvMyclass.adapter = myClassAdapter
        myClassAdapter.datas = classList
        myClassAdapter.notifyDataSetChanged()

        myClassAdapter.setOnItemClickListener(object : MyListRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ClassData, pos: Int) {
                val intent = Intent(this@MyListActivity, ClassDetailActivity::class.java)
                intent.putExtra("data", data as Serializable)
                intent.putExtra("where", "myClass")
                startActivity(intent)
            }
        })
    }
}