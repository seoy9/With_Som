package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassDetailBinding
import hong.sy.withsom.recyclerView.DetailRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator
import hong.sy.withsom.room.ClassEntity
import hong.sy.withsom.room.UserDao
import hong.sy.withsom.room.UserDatabase
import hong.sy.withsom.room.UserEntity

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassDetailBinding

    lateinit var detailAdapter: DetailRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    lateinit var classEntity: ClassEntity

    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabase.getInstance(this)!!
        userDao = db.getUserDao()

        classEntity = intent.getSerializableExtra("data") as ClassEntity

        if(classEntity == null) {

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
            getUser(classEntity.leaderID)

//            binding.tvDetailName.text = classEntity.name + "\n" + user.stNum + " " + user.name
//            binding.imgLeaderDetail.setImageResource(user.profile)
//            binding.tvLocationDetail.text = classEntity.location
//            binding.tvScheduleDetail.text = classEntity.schedule
//            binding.tvNumberDetail.text = classEntity.totalNum.toString() + "명"
        }

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        detailAdapter = DetailRecyclerViewAdapter(this)
        binding.rvDetail.adapter = detailAdapter

        if(classEntity == null) {

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
                add(DetailData(title = "모임 소개", content = classEntity.content))
                add(DetailData(title = "모임 대상", content = classEntity.member))
                add(DetailData(title = "모임 일정", content = classEntity.scheduleDetail))
                add(DetailData(title = "리더 소개", content = classEntity.leaderContent))

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

    private fun getUser(id: String) {
        Thread {
            val user = userDao.selectEmailUser(id)

            if(user != null) {
                binding.tvDetailName.text = classEntity.name + "\n" + user.stNum + " " + user.name
                binding.imgLeaderDetail.setImageResource(user.profile)
                binding.tvLocationDetail.text = classEntity.location
                binding.tvScheduleDetail.text = classEntity.schedule
                binding.tvNumberDetail.text = classEntity.totalNum.toString() + "명"
            }
        }.start()
    }
}
