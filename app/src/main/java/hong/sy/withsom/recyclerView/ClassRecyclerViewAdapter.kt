package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.ClassDetailActivity
import hong.sy.withsom.viewPager2.ClassViewPagerAdapter
import hong.sy.withsom.R

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

        fun setting() {
            classes.adapter = classViewPagerAdapter
            classes.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            classViewPagerAdapter.setOnItemClickListener(object : ClassViewPagerAdapter.OnItemClickListener {
                override fun onClick(v: View, data: ClassData, pos: Int) {
                    Intent(context, ClassDetailActivity::class.java).apply {
                        putExtra("title", data.title)
                        putExtra("leader", data.leader)
                        putExtra("leader_img", data.imgLeader)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }.run { context.startActivity(this) }
                }
            })
        }

        fun bind(item: ClassData) {
            type.text = "#" + item.type
        }
    }

    private fun getClassList(): ArrayList<ClassData> {
        val classData1 = ClassData(R.drawable.foundation, "20221234 이솜솜", "솜솜덕질", "취미", "솜솜이를 덕질해보자!")
        val classData2 = ClassData(R.drawable.simbol, "20225678 김솜솜", "정보처리기사", "자격증", "컴퓨터학과 졸업요건 달성")
        val classData3 = ClassData(R.drawable.vision, "20229000 동솜솜", "만 보 걷기", "운동", "건강해지자!!")

        return arrayListOf<ClassData>(classData1, classData2, classData3)
    }
}