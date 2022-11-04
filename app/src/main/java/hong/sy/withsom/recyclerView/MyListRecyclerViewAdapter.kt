package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.R
import hong.sy.withsom.data.ClassData
import java.util.*

class MyListRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<MyListRecyclerViewAdapter.MyListViewHolder>() {
    var datas = ArrayList<ClassData>()

    inner class MyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
            tv_num.text = "정원 " + item.currentNum.toString() + "/" + item.totalNum.toString() + "명"

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

        fun bindLeaderImg(item : ClassData) {
            val database = Firebase.database
            val myRef = database.getReference("users")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(userSnapshot in snapshot.children) {
                            val email = userSnapshot.child("email").getValue(String::class.java)
                            val profile = userSnapshot.child("profile").getValue(Int::class.java)

                            if(email == item.leaderID) {
                                img_leader.setImageResource(profile!!)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_search, parent, false)
        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int  = datas.size

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.bindLeaderImg(datas[position])
        holder.bind(datas[position])
    }
}