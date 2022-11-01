package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityMainBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.viewPager2.NoticeViewPagerAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numBanner = 3

    private lateinit var classViewPagerAdapter: ClassViewPagerAdapter

    private var myHandler = MyHandler()
    private var currentPosition = Int.MAX_VALUE / 2
    private val intervalTime = 3000.toLong()

    private val database = Firebase.database

    private var classList = ArrayList<ClassData>()

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

    private fun getClassList() {
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

                        val c = ClassData(cid!!, name!!, type!!, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID!!, leaderContent!!)
                        classList.add(c)
                    }
                    settingClassViewPagerAdapter()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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

        getClassList()
    }

    private fun settingClassViewPagerAdapter() {
        classViewPagerAdapter = ClassViewPagerAdapter(classList)

        binding.viewpagerClass.adapter = classViewPagerAdapter
        binding.viewpagerClass.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //binding.tvTotalClass.text = numBanner.toString()
        binding.tvTotalClass.text = classList.size.toString()

        binding.viewpagerClass.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvCurrentClass.text = "${position + 1}"
            }
        })

        classViewPagerAdapter.setOnItemClickListener(object :
            ClassViewPagerAdapter.OnItemClickListener {
            override fun onClick(v: View, data: ClassData, pos: Int) {
                val intent = Intent(this@MainActivity, ClassDetailActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
        })
    }
}