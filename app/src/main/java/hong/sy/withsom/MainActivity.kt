package hong.sy.withsom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import hong.sy.withsom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.viewpagerNotice.adapter = NoticeViewPagerAdapter(getNoticeList())
        binding.viewpagerNotice.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun getNoticeList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.notice_banner1, R.drawable.notice_banner2, R.drawable.notice_banner3)
    }
}