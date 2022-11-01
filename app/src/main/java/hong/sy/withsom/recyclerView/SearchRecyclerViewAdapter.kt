package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.R
import hong.sy.withsom.data.ClassData

class SearchRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {
    var classes = mutableListOf<ClassData>()

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img_leader : ImageView = itemView.findViewById(R.id.img_leader_search)
        private val tv_title : TextView = itemView.findViewById(R.id.tv_title_search)
        private val tv_type : TextView = itemView.findViewById(R.id.tv_type_search)
        private val tv_schedule : TextView = itemView.findViewById(R.id.tv_schedule_search)
        private val tv_num : TextView = itemView.findViewById(R.id.tv_num_search)

        fun bind(item : ClassData) {
            //img_leader.setImageResource(item.imgLeader)
            tv_title.text = item.name
            tv_type.text = item.type
            tv_schedule.text = item.schedule
            tv_num.text = "정원 " + item.totalNum.toString() + "명"

            itemView.setOnClickListener {
                val intent = Intent(context, ClassDetailActivity::class.java)
                intent.putExtra("title", item.name)
                intent.putExtra("leader", item.leaderID)
                    //putExtra("leader_img", item.imgLeader)
                intent.putExtra("location", item.location)
                intent.putExtra("schedule", item.schedule)
                intent.putExtra("num", item.totalNum)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = classes.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(classes[position])
    }
}