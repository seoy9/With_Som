package hong.sy.withsom.viewPager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.R

class ClassViewPagerAdapter(classDataBannerList: ArrayList<ClassData>) : RecyclerView.Adapter<ClassViewPagerAdapter.PagerViewHolder>() {
    var item = classDataBannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(item[position])
    }

    interface OnItemClickListener {
        fun onClick(v: View, data: ClassData, pos: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.class_banner_item, parent, false)) {
        private val imgLeader : ImageView = itemView.findViewById(R.id.img_leader)
        private val classTitle : TextView = itemView.findViewById(R.id.tv_class_title)
        private val classType : TextView = itemView.findViewById(R.id.tv_class_type)
        private val classContent : TextView = itemView.findViewById(R.id.tv_class_content)

        fun bind(item: ClassData) {
            imgLeader.setImageResource(item.imgLeader)
            classTitle.text = item.title
            classType.text = item.type
            classContent.text = item.content

            val pos = adapterPosition

            itemView.setOnClickListener {
                listener?.onClick(itemView, item, pos)
            }
        }
    }
}
