package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityMainBinding
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.viewPager2.NoticeViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numBanner = 3

    private lateinit var classViewPagerAdapter: ClassViewPagerAdapter

    private var myHandler = MyHandler()
    private var currentPosition = Int.MAX_VALUE / 2
    private val intervalTime = 5000.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpagerNotice.adapter = NoticeViewPagerAdapter(getNoticeList())
        binding.viewpagerNotice.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.tvTotalNotice.text = numBanner.toString()

        binding.viewpagerNotice.apply {
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvCurrentNotice.text = "${(position % 3) + 1}"
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when(state) {
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                    }
                }
            })
        }

        binding.noticeSeeAll.setOnClickListener {
            Toast.makeText(this, "모두 보기 클릭했음", Toast.LENGTH_SHORT).show()
        }

        classViewPagerAdapter = ClassViewPagerAdapter(getClassList())

        binding.viewpagerClass.adapter = classViewPagerAdapter
        binding.viewpagerClass.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.tvTotalClass.text = numBanner.toString()

        binding.viewpagerClass.apply {
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvCurrentClass.text = "${position+1}"
                }
            })
        }

        classViewPagerAdapter.setOnItemClickListener(object : ClassViewPagerAdapter.OnItemClickListener {
            override fun onClick(v: View, data: ClassData, pos: Int) {
                Intent(this@MainActivity, ClassDetailActivity::class.java).apply {
                    putExtra("title", data.title)
                    putExtra("leader", data.leader)
                    putExtra("leader_img", data.imgLeader)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })

        binding.btnSearchMain.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnClassesMain.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingMain.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun autoScrollStart(intervalTime: Long) {
        myHandler.removeMessages(0)
        myHandler.sendEmptyMessageDelayed(0, intervalTime)
    }

    private fun autoScrollStop() {
        myHandler.removeMessages(0)
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if(msg.what == 0) {
                binding.viewpagerNotice.setCurrentItem(++currentPosition, true)
                autoScrollStart(intervalTime)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }

    private fun getNoticeList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.notice_banner1, R.drawable.notice_banner2, R.drawable.notice_banner3)
    }

    private fun getClassList(): ArrayList<ClassData> {
        val classData1 = ClassData(R.drawable.foundation, "20221234 이솜솜", "솜솜덕질", "취미", "솜솜이를 덕질해보자!")
        val classData2 = ClassData(R.drawable.simbol, "20225678 김솜솜", "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성")
        val classData3 = ClassData(R.drawable.vision, "20229000 박솜솜", "만 보 걷기", "운동", "건강해지자!!")

        return arrayListOf<ClassData>(classData1, classData2, classData3)
    }
}