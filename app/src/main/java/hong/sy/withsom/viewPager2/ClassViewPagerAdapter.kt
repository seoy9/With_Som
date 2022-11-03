package hong.sy.withsom.viewPager2

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
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.R
import java.util.*

class ClassViewPagerAdapter(classDataBannerList: ArrayList<ClassData>) : RecyclerView.Adapter<ClassViewPagerAdapter.PagerViewHolder>() {
    var item = classDataBannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.getClassList(item[position].leaderID)
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
            //imgLeader.setImageResource(item.imgLeader)
            classTitle.text = item.name
            classType.text = item.type
            val content = item.content
            if(content.length > 35) {
                classContent.text = content.subSequence(0, 32).toString() + "..."
            } else {
                classContent.text= content
            }

            val pos = adapterPosition

            itemView.setOnClickListener {
                listener?.onClick(itemView, item, pos)
            }
        }

        fun getClassList(item: String) {
            val database = Firebase.database
            val myRef = database.getReference("users")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(userSnapshot in snapshot.children) {
                            val email = userSnapshot.child("email").getValue(String::class.java)
                            val profile = userSnapshot.child("profile").getValue(Int::class.java)

                            if(email == item) {
                                imgLeader.setImageResource(profile!!)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
