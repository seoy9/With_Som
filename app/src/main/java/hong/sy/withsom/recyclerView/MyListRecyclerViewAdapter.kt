package hong.sy.withsom.recyclerView

import android.content.Context
import android.graphics.Color
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
import hong.sy.withsom.R
import hong.sy.withsom.data.ClassData
import java.util.*

class MyListRecyclerViewAdapter(private val context: Context, private var datas: ArrayList<ClassData>) : RecyclerView.Adapter<MyListRecyclerViewAdapter.MyListViewHolder>() {
    var list = datas
    var where = ""

    inner class MyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img_leader : ImageView = itemView.findViewById(R.id.img_leader_search)
        private val tv_title : TextView = itemView.findViewById(R.id.tv_title_search)
        private val tv_type : TextView = itemView.findViewById(R.id.tv_type_search)
        private val tv_schedule : TextView = itemView.findViewById(R.id.tv_schedule_search)
        private val tv_num : TextView = itemView.findViewById(R.id.tv_num_search)

        fun bind(item : ClassData) {
            tv_title.text = item.name
            tv_type.text = item.type
            tv_schedule.text = item.schedule

            if(item.currentNum == item.totalNum) {
                tv_num.text = "정원 마감"
                tv_num.setTextColor(Color.parseColor("#CC0000"))
            } else {
                tv_num.text =
                    "정원 " + item.currentNum.toString() + "/" + item.totalNum.toString() + "명"
            }

//            itemView.setOnClickListener {
//                val intent = Intent(context, ClassDetailActivity::class.java)
//                intent.putExtra("data", item as Serializable)
//                intent.putExtra("where", where)
//                context.startActivity(intent)
//            }
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
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

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.bindLeaderImg(list[position])
        holder.bind(list[position])
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: ClassData, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
}