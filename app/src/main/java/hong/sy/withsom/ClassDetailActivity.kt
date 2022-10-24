package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassDetailBinding
import hong.sy.withsom.recyclerView.DetailRecyclerViewAdapter

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassDetailBinding

    lateinit var detailAdapter: DetailRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val leader = intent.getStringExtra("leader")
        val leader_img = intent.getIntExtra("leader_img", 0)

        binding.tvDetailName.text = title + "\n" + leader

        if(leader_img == null) {
            Toast.makeText(this, "img null!", Toast.LENGTH_SHORT).show()
        }

        if(leader_img != null) {
            binding.imgLeaderDetail.setImageResource(leader_img)
        }

        initRecycler()

        binding.btnClassesDetail.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchDetail.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnHomeDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingDetail.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycler() {
        detailAdapter = DetailRecyclerViewAdapter(this)
        binding.rvDetail.adapter = detailAdapter

        datas.apply {
            add(DetailData(title = "모임 소개", content = "솜솜이를 사랑하시는 분!\n같이 덕질해요!"))
            add(DetailData(title = "모임 대상", content = "솜솜이를 사랑하는 찐팬"))
            add(DetailData(title = "모임 일정", content = "매일매시매분매초\n숨 멈추는 순간까지"))
            add(DetailData(title = "리더 소개", content = "솜솜이 1호팬"))
        }
    }
}
