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
import hong.sy.withsom.room.ClassEntity
import hong.sy.withsom.room.UserDatabase
import hong.sy.withsom.room.UserEntity
import java.io.Serializable
import java.util.*

class SearchRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {
    var classes = ArrayList<ClassEntity>()

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img_leader : ImageView = itemView.findViewById(R.id.img_leader_search)
        private val tv_title : TextView = itemView.findViewById(R.id.tv_title_search)
        private val tv_type : TextView = itemView.findViewById(R.id.tv_type_search)
        private val tv_schedule : TextView = itemView.findViewById(R.id.tv_schedule_search)
        private val tv_num : TextView = itemView.findViewById(R.id.tv_num_search)

        private val db = UserDatabase.getInstance(view.context)!!
        private val userDao = db.getUserDao()
        private lateinit var user: UserEntity

        fun bind(item : ClassEntity) {
            Thread {
                user = userDao.selectIDUser(item.leaderID)
            }.start()

            img_leader.setImageResource(user.profile)
            tv_title.text = item.name
            tv_type.text = item.type
            tv_schedule.text = item.schedule
            tv_num.text = "정원 " + item.totalNum.toString() + "명"

            itemView.setOnClickListener {
//                Intent(context, ClassDetailActivity::class.java).apply {
//                    putExtra("title", item.name)
//                    putExtra("leader", user.)
//                    putExtra("leader_img", item.imgLeader)
//                    putExtra("location", item.location)
//                    putExtra("schedule", item.schedule)
//                    putExtra("num", item.num)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }.run { context.startActivity(this) }

                val intent = Intent(context, ClassDetailActivity::class.java)
                intent.putExtra("data", item as Serializable)
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