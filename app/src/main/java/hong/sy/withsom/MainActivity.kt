package hong.sy.withsom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import hong.sy.withsom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numBanner = 3

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
                    binding.tvCurrentNotice.text = "${position+1}"
                }
            })
        }

        binding.noticeSeeAll.setOnClickListener {
            Toast.makeText(this, "모두 보기 클릭했음", Toast.LENGTH_SHORT).show()
        }

        binding.viewpagerClass.adapter = ClassViewPagerAdapter(getClassList())
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
    }

    private fun getNoticeList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.notice_banner1, R.drawable.notice_banner2, R.drawable.notice_banner3)
    }

    private fun getClassList(): ArrayList<Class> {
        val class1 = Class(R.drawable.foundation, "솜솜덕질", "취미", "솜솜이를 덕질해보자!")
        val class2 = Class(R.drawable.simbol, "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성")
        val class3 = Class(R.drawable.vision, "만 보 걷기", "운동", "건강해지자!!")

        return arrayListOf<Class>(class1, class2, class3)
    }
}