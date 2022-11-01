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
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.viewPager2.NoticeViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numBanner = 3

    private lateinit var classViewPagerAdapter: ClassViewPagerAdapter

    private var myHandler = MyHandler()
    private var currentPosition = Int.MAX_VALUE / 2
    private val intervalTime = 3000.toLong()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMore.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            startActivity(intent)
        }

        noticeBannerSetting()

        classBannerSetting()

        buttonSetting()
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
        val classData1 = ClassData("1", "솜솜덕질", "취미", "솜솜이를 덕질해보자!", "동덕여대", 0, 5, "솜솜이를 사랑하는 학우", "매주, 월, 수, 금", "유동적으로 활동", "1@dongduk.ac.kr", "솜덕")
        val classData2 = ClassData("2", "정보처리기사", "자격증", "정보처리기사 자격 취득", "숭인관", 0, 3, "정처기 필요한 사람", "매주, 화, 목", "화, 목 6시 이후", "2@dongduk.ac.kr", "컴과솜")
        val classData3 = ClassData("3", "만 보 걷기", "운동", "건강해지기", "백주년기념관", 0, 10, "만 보 챌린지 할 사람", "매주, 토, 일", "주말 낮", "3@dongduk.ac.kr", "체과촘")

        return arrayListOf<ClassData>(classData1, classData2, classData3)
    }

    private fun buttonSetting() {
        binding.btnClassesMain.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchMain.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingMain.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun noticeBannerSetting() {
        binding.viewpagerNotice.adapter = NoticeViewPagerAdapter(getNoticeList())
        binding.viewpagerNotice.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.tvTotalNotice.text = numBanner.toString()
        binding.viewpagerNotice.setCurrentItem(currentPosition, false)

        binding.viewpagerNotice.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
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

    private fun classBannerSetting() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pageWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pageWidth

        binding.viewpagerClass.setPageTransformer {page, position ->
            page.translationX = position * -offsetPx
        }

        binding.viewpagerClass.offscreenPageLimit = 1

        classViewPagerAdapter = ClassViewPagerAdapter(getClassList())

        binding.viewpagerClass.adapter = classViewPagerAdapter
        binding.viewpagerClass.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.tvTotalClass.text = numBanner.toString()

        binding.viewpagerClass.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvCurrentClass.text = "${position+1}"
            }
        })

        classViewPagerAdapter.setOnItemClickListener(object : ClassViewPagerAdapter.OnItemClickListener {
            override fun onClick(v: View, data: ClassData, pos: Int) {
                val intent = Intent(this@MainActivity, ClassDetailActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
        })
    }
}