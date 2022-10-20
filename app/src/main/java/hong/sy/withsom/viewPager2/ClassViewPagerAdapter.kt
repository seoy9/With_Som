package hong.sy.withsom.viewPager2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.ClassData
import hong.sy.withsom.R

class ClassViewPagerAdapter(classDataBannerList: ArrayList<ClassData>) : RecyclerView.Adapter<ClassViewPagerAdapter.PagerViewHolder>() {
    var item = classDataBannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.imgLeader.setImageResource (item[position].imgLeader)
        holder.classTitle.text = item[position].title
        holder.classType.text = item[position].type
        holder.classContent.text = item[position].content
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.class_banner_item, parent, false)) {
        val imgLeader : ImageView = itemView.findViewById(R.id.img_leader)
        val classTitle : TextView = itemView.findViewById(R.id.tv_class_title)
        val classType : TextView = itemView.findViewById(R.id.tv_class_type)
        val classContent : TextView = itemView.findViewById(R.id.tv_class_content)
    }
}
