package hong.sy.withsom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import hong.sy.withsom.databinding.ActivityMyListBinding

class MyListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listTabLayout.getTabAt(0)!!.text = "내 모임"
        binding.listTabLayout.getTabAt(1)!!.text = "신청한 모임"

    }
}