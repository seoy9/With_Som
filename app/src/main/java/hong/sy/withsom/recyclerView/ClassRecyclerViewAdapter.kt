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
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.R
import java.io.Serializable
import java.util.*

class ClassRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<ClassRecyclerViewAdapter.ViewHolder>() {
    var datas = mutableListOf<ClassData>()

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
        private val classDetail : ArrayList<ClassData> = getClassList()
        private val classViewPagerAdapter =  ClassViewPagerAdapter(classDetail)
        private val nextButton: ImageButton = itemView.findViewById(R.id.btn_next_class)
        private val beforeButton: ImageButton = itemView.findViewById(R.id.btn_before_class)

        fun setting() {
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
                        if(classDetail.size == 1) {
                            blindAllButton()
                        } else if(position == 0) {
                            blindBeforeButton()
                        } else if(position == itemCount-1) {
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

        fun bind(item: ClassData) {
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

    private fun getClassList(): ArrayList<ClassData> {
        val classData1 = ClassData("1", "솜솜덕질", "취미", "솜솜이를 덕질해보자!", "동덕여대", 0, 5, "솜솜이를 사랑하는 학우", "매주, 월, 수, 금", "유동적으로 활동", "1@dongduk.ac.kr", "솜덕")
        val classData2 = ClassData("2", "정보처리기사", "자격증", "정보처리기사 자격 취득", "숭인관", 0, 3, "정처기 필요한 사람", "매주, 화, 목", "화, 목 6시 이후", "2@dongduk.ac.kr", "컴과솜")
        val classData3 = ClassData("3", "만 보 걷기", "운동", "건강해지기", "백주년기념관", 0, 10, "만 보 챌린지 할 사람", "매주, 토, 일", "주말 낮", "3@dongduk.ac.kr", "체과촘")

        return arrayListOf<ClassData>(classData1, classData2, classData3)
    }
}