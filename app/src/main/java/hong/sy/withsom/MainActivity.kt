package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.classList.ClassDetailActivity
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.NoticeData
import hong.sy.withsom.databinding.ActivityMainBinding
import hong.sy.withsom.notice.NoticeActivity
import hong.sy.withsom.notice.NoticeDetailActivity
import hong.sy.withsom.setting.SettingActivity
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.viewPager2.NoticeViewPagerAdapter
import java.io.Serializable
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

    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMore.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            startActivity(intent)
        }

        noticeBannerSetting()

        classBannerSetting()

        buttonSetting()

        getTopNoticeTitle()
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
                    randomClass()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun randomClass() {
        var randomClass = ArrayList<ClassData>()

        while(true) {
            if(classList.size < 3) {
                randomClass.addAll(classList)
                break
            }
            if(randomClass.size == 3) {
                break
            }

            val random = Random().nextInt(classList.size)

            if(!randomClass.contains(classList[random])) {
                randomClass.add(classList[random])
            }

        }

        classList.clear()
        classList.addAll(randomClass)

        settingClassViewPagerAdapter()
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
                intent.putExtra("where", "main")
                startActivity(intent)
            }
        })
    }

    private fun getTopNoticeTitle() {
        val notices = ArrayList<NoticeData>()
        val myRef = database.getReference("notices")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val nid = classSnapshot.child("nid").getValue(Int::class.java)
                        val title = classSnapshot.child("title").getValue(String::class.java)
                        val date = classSnapshot.child("date").getValue(String::class.java)
                        val content = classSnapshot.child("content").getValue(String::class.java)

                        val notice = NoticeData(nid!!, title!!, date!!, content!!)
                        notices.add(notice)
                    }
                    noticeTitleSetting(notices)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun noticeTitleSetting(notices: ArrayList<NoticeData>) {
        val title = notices[notices.size-1].title
        binding.tvNoticeTitle.text = title.subSequence(0, 26).toString() + "..."

        binding.tvNoticeTitle.setOnClickListener {
            val intent = Intent(this, NoticeDetailActivity::class.java)
            intent.putExtra("notice", notices[notices.size-1] as Serializable)
            startActivity(intent)
        }
    }
}