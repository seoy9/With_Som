package hong.sy.withsom

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class NoticeViewPagerAdapter(noticeBannerList: ArrayList<Int>) : RecyclerView.Adapter<NoticeViewPagerAdapter.PagerViewHolder>() {
    var item = noticeBannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount() : Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.notice.setImageResource(item[position])
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.notice_banner_item, parent, false)) {
        val notice : ImageView = itemView.findViewById(R.id.img_notice_banner)
    }
}