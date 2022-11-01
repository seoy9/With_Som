package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.R
import java.io.Serializable
import java.util.*

class ClassRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<ClassRecyclerViewAdapter.ViewHolder>() {
    var datas = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_classes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getClassList(datas[position])
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val type : TextView = itemView.findViewById(R.id.tv_classes_type)
        private val classes : ViewPager2 = itemView.findViewById(R.id.viewPager_classes)
        private var classList = ArrayList<ClassData>()
        private val nextButton: ImageButton = itemView.findViewById(R.id.btn_next_class)
        private val beforeButton: ImageButton = itemView.findViewById(R.id.btn_before_class)

        fun getClassList(item: String) {
            val database = Firebase.database
            val myRef = database.getReference("classes")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(classSnapshot in snapshot.children) {
                            val cid = classSnapshot.child("cid").getValue(Int::class.java)
                            val name = classSnapshot.child("name").getValue(String::class.java)
                            val type = classSnapshot.child("type").getValue(String::class.java)
                            val content = classSnapshot.child("content").getValue(String::class.java)
                            val location = classSnapshot.child("location").getValue(String::class.java)
                            val currentNum = classSnapshot.child("currentNum").getValue(Int::class.java)
                            val totalNum = classSnapshot.child("totalNum").getValue(Int::class.java)
                            val member = classSnapshot.child("member").getValue(String::class.java)
                            val schedule = classSnapshot.child("schedule").getValue(String::class.java)
                            val scheduleDetail = classSnapshot.child("scheduleDetail").getValue(String::class.java)
                            val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)
                            val leaderContent = classSnapshot.child("leaderContent").getValue(String::class.java)

                            for(t in type!!.split(", ")) {
                                if(t == item) {
                                    val c = ClassData(cid!!, name!!, type, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID!!, leaderContent!!)
                                    classList.add(c)
                                }
                            }
//                            val c = ClassData(cid!!, name!!, type!!, content!!, location!!, currentNum!!, totalNum!!, member!!, schedule!!, scheduleDetail!!, leaderID!!, leaderContent!!)
//                            classList.add(c)
                        }
                        setting()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        fun setting() {
            val classViewPagerAdapter =  ClassViewPagerAdapter(classList)
            classes.adapter = classViewPagerAdapter
            classes.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            classViewPagerAdapter.setOnItemClickListener(object : ClassViewPagerAdapter.OnItemClickListener {
                override fun onClick(v: View, data: ClassData, pos: Int) {
                    val intent = Intent(context, ClassDetailActivity::class.java)
                    intent.putExtra("data", data as Serializable)
                    context.startActivity(intent)
                }
            })

            classes.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if(classList.size == 1 || classList.size == 0) {
                            blindAllButton()
                        } else if(position == 0) {
                            blindBeforeButton()
                        } else if(position == classList.size-1) {
                            blindNextButton()
                        } else {
                            showAllButton()
                        }

                        var currentPosition = position
                        nextButton.setOnClickListener {
                            classes.setCurrentItem(++currentPosition, true)
                        }
                        beforeButton.setOnClickListener {
                            classes.setCurrentItem(--currentPosition, true)
                        }
                    }
            })
        }

        fun bind(item: String) {
            type.text = "#" + item
        }

        fun blindBeforeButton() {
            beforeButton.setVisibility(View.INVISIBLE)
            nextButton.setVisibility(View.VISIBLE)
        }

        fun blindNextButton() {
            beforeButton.setVisibility(View.VISIBLE)
            nextButton.setVisibility(View.INVISIBLE)
        }

        fun showAllButton() {
            beforeButton.setVisibility(View.VISIBLE)
            nextButton.setVisibility(View.VISIBLE)
        }

        fun blindAllButton() {
            beforeButton.setVisibility(View.INVISIBLE)
            nextButton.setVisibility(View.INVISIBLE)
        }
    }
}