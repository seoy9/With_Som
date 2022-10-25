package hong.sy.withsom.viewPager2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.R

class NoticeViewPagerAdapter(noticeBannerList: ArrayList<Int>) : RecyclerView.Adapter<NoticeViewPagerAdapter.NoticeViewHolder>() {
    var item = noticeBannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoticeViewHolder((parent))

    override fun getItemCount() : Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.onBind(item[position % item.size])
    }

    inner class NoticeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.notice_banner_item, parent, false)) {
        private val notice : ImageView = itemView.findViewById(R.id.img_notice_banner)

        fun onBind(res: Int) {
            notice.setImageResource(res)
        }
    }
}