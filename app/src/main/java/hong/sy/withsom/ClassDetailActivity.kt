package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassDetailBinding
import hong.sy.withsom.recyclerView.DetailRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassDetailBinding

    lateinit var detailAdapter: DetailRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    lateinit var classData: ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classData = intent.getSerializableExtra("data") as ClassData

        if(classData == null) {

            val title = intent.getStringExtra("title")
            val leader = intent.getStringExtra("leader")
            val leader_img = intent.getIntExtra("leader_img", 0)
            val location = intent.getStringExtra("location")
            val schedule = intent.getStringExtra("schedule")
            val num = intent.getIntExtra("num", 0).toString()

            binding.tvDetailName.text = title + "\n" + leader

            if (leader_img != null) {
                binding.imgLeaderDetail.setImageResource(leader_img)
            }

            binding.tvLocationDetail.text = location
            binding.tvScheduleDetail.text = schedule
            binding.tvNumberDetail.text = num + "명"
        } else {
            val database = Firebase.database
            val myRef = database.getReference("users")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(userSnapshot in snapshot.children) {
                            val email = userSnapshot.child("email").getValue(String::class.java)
                            val profile = userSnapshot.child("profile").getValue(Int::class.java)
                            val stNum = userSnapshot.child("stNum").getValue(String::class.java)
                            val name = userSnapshot.child("name").getValue(String::class.java)

                            if(email == classData.leaderID) {
                                binding.imgLeaderDetail.setImageResource(profile!!)
                                binding.tvDetailName.text = classData.name + "\n" + stNum!! + " " + name!!
                                binding.tvLocationDetail.text = classData.location
                                binding.tvScheduleDetail.text = classData.schedule
                                binding.tvNumberDetail.text = classData.currentNum.toString() + "/" + classData.totalNum.toString() + "명"
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        detailAdapter = DetailRecyclerViewAdapter(this)
        binding.rvDetail.adapter = detailAdapter

        if(classData == null) {

            datas.apply {
                add(DetailData(title = "모임 소개", content = "솜솜이를 사랑하시는 분!\n같이 덕질해요!"))
                add(DetailData(title = "모임 대상", content = "솜솜이를 사랑하는 찐팬"))
                add(DetailData(title = "모임 일정", content = "매일매시매분매초\n숨 멈추는 순간까지"))
                add(DetailData(title = "리더 소개", content = "솜솜이 1호팬"))

                detailAdapter.datas = datas
                detailAdapter.notifyDataSetChanged()
            }
        } else {
            datas.apply {
                add(DetailData(title = "모임 소개", content = classData.content))
                add(DetailData(title = "모임 대상", content = classData.member))
                add(DetailData(title = "모임 일정", content = classData.scheduleDetail))
                add(DetailData(title = "리더 소개", content = classData.leaderContent))

                detailAdapter.datas = datas
                detailAdapter.notifyDataSetChanged()
            }
        }

        binding.rvDetail.addItemDecoration(VerticalItemDecorator(20))
        binding.rvDetail.addItemDecoration(HorizontalItemDecorator(10))
    }

    private fun buttonSetting() {
        binding.btnApplication.setOnClickListener {
            Toast.makeText(this, "신청되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnHomeDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesDetail.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchDetail.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingDetail.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
