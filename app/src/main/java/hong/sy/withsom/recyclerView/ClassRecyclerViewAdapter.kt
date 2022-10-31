package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.R
import hong.sy.withsom.room.ClassDatabase
import hong.sy.withsom.room.ClassEntity
import java.io.Serializable
import java.util.*

class ClassRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<ClassRecyclerViewAdapter.ViewHolder>() {
    var datas = ArrayList<ClassEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_classes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setting()
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val type : TextView = itemView.findViewById(R.id.tv_classes_type)
        private val classes : ViewPager2 = itemView.findViewById(R.id.viewPager_classes)
        private lateinit var classDetail : ArrayList<ClassEntity>
        private lateinit var classViewPagerAdapter : ClassViewPagerAdapter
        private val nextButton: ImageButton = itemView.findViewById(R.id.btn_next_class)
        private val beforeButton: ImageButton = itemView.findViewById(R.id.btn_before_class)

        private val db = ClassDatabase.getInstance(view.context)!!
        private val classDao = db.getClassDao()


        fun setting() {
            Thread {
                classDetail = ArrayList(classDao.getAll())

                if(classDetail != null) {

                    classViewPagerAdapter = ClassViewPagerAdapter(classDetail)

                    classes.adapter = classViewPagerAdapter
                    classes.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                    classViewPagerAdapter.setOnItemClickListener(object :
                        ClassViewPagerAdapter.OnItemClickListener {
                        override fun onClick(v: View, data: ClassEntity, pos: Int) {
                            val intent = Intent(context, ClassDetailActivity::class.java)
                            intent.putExtra("data", data as Serializable)
                            context.startActivity(intent)

                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed(object: Runnable {
                                override fun run() {
                                    Toast.makeText(v.context, "Detail.", Toast.LENGTH_SHORT).show()
                                }
                            }, 0)
                        }
                    })

                    classes.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            if (classDetail.size == 1) {
                                blindAllButton()
                            } else if (position == 0) {
                                blindBeforeButton()
                            } else if (position == itemCount - 1) {
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
            }.start()


        }

        fun bind(item: ClassEntity) {
            type.text = "#" + item.type
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