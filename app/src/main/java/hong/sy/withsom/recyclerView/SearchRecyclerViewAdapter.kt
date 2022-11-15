package hong.sy.withsom.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.classList.ClassDetailActivity
import hong.sy.withsom.R
import hong.sy.withsom.data.ClassData
import java.io.Serializable
import java.util.*

class SearchRecyclerViewAdapter(private val datas: ArrayList<ClassData>, private val context: Context) : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>(), Filterable {
    var classes = datas
    var searchFilter = SearchFilter()

    inner class SearchFilter: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString()
            val results = FilterResults()

            val filterList : ArrayList<ClassData> = ArrayList<ClassData>()

            if(filterString.length == 0) {
                results.values = datas
                results.count = datas.size
                return results
            } else {
                for(search in datas) {
                    if(search.name.contains(filterString) || search.content.contains(filterString) || search.type.contains(filterString) || search.member.contains(filterString) || search.leaderID.contains(filterString) || search.schedule.contains(filterString) || search.scheduleDetail.contains(filterString) || search.location.contains(filterString)) {
                        filterList.add(search)
                    }
                }
            }
            results.values = filterList
            results.count = filterList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            classes.clear()
            classes.addAll(results!!.values as ArrayList<ClassData>)
            notifyDataSetChanged()
        }
    }

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
            tv_num.text = "정원 " + item.currentNum.toString() + "/" + item.totalNum.toString() + "명"

            itemView.setOnClickListener {
                val intent = Intent(context, ClassDetailActivity::class.java)
                intent.putExtra("data", item as Serializable)
                intent.putExtra("where", "search")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = classes.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindLeaderImg(classes[position])
        holder.bind(classes[position])
    }

    override fun getFilter(): Filter {
        return searchFilter
    }
}