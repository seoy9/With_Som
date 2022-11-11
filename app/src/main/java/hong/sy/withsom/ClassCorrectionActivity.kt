package hong.sy.withsom

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassCorrectionBinding
import hong.sy.withsom.recyclerView.CorrectionRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator
import java.util.*

class ClassCorrectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassCorrectionBinding

    lateinit var correctionAdapter: CorrectionRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    lateinit var classData: ClassData

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassCorrectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classData = intent.getSerializableExtra("data") as ClassData

        initRecycler()

        buttonSetting()
    }

    private fun initRecycler() {
        correctionAdapter = CorrectionRecyclerViewAdapter(this)
        binding.rvClassCorrection.adapter = correctionAdapter

        datas.clear()

        datas.apply {
            add(DetailData(title = "모임 이름", content = classData.name))
            add(DetailData(title = "모임 종류", content = classData.type))
            add(DetailData(title = "모임 소개", content = classData.content))
            add(DetailData(title = "모임 위치", content = classData.location))
            add(DetailData(title = "모집 인원", content = classData.totalNum.toString()))
            add(DetailData(title = "모집 대상", content = classData.member))
            add(DetailData(title = "모임 일정", content = classData.schedule))
            add(DetailData(title = "모임 일정 상세", content = classData.scheduleDetail))
            add(DetailData(title = "리더 소개", content = classData.leaderContent))

            correctionAdapter.datas = datas
            correctionAdapter.notifyDataSetChanged()
        }

        binding.rvClassCorrection.addItemDecoration(VerticalItemDecorator(15))
        binding.rvClassCorrection.addItemDecoration(HorizontalItemDecorator(10))

        correctionAdapter.setOnItemClickListener(object: CorrectionRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: DetailData, pos: Int) {
                if (pos == 1) {
                    updateType(data)
                } else if (pos == 6) {
                    updateSchedule(data)
                } else {
                    val builder = AlertDialog.Builder(this@ClassCorrectionActivity)
                    builder.setTitle(data.title + " 수정")

                    val v1 = layoutInflater.inflate(R.layout.dialog_edit, null)
                    builder.setView(v1)

                    val edit: MultiAutoCompleteTextView? = v1.findViewById(R.id.edit_correction)
                    edit!!.setText(data.content)

                    val listener = DialogInterface.OnClickListener { p0, p1 ->
                        val alert = p0 as AlertDialog
                        var edit: MultiAutoCompleteTextView? =
                            alert.findViewById(R.id.edit_correction)

                        data.content = edit!!.text.toString()

                        when (pos) {
                            0 -> classData.name = edit.text.toString()
                            2 -> classData.content = edit.text.toString()
                            3 -> classData.location = edit.text.toString()
                            4 -> classData.totalNum = edit.text.toString().toInt()
                            5 -> classData.member = edit.text.toString()
                            7 -> classData.scheduleDetail = edit.text.toString()
                            8 -> classData.leaderContent = edit.text.toString()
                        }

                        initRecycler()
                    }

                    builder.setPositiveButton("확인", listener)
                    builder.setNegativeButton("취소", null)

                    builder.show()
                }
            }
        })
    }

    private fun buttonSetting() {
        binding.btnClassCorrectionDone.setOnClickListener {
            val myRef = database.getReference("classes")
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(classSnapshot in snapshot.children) {
                            val cid = classSnapshot.child("cid").getValue(Int::class.java)

                            if(classData.cid == cid) {
                                myRef.child(cid.toString()).setValue(classData)

                                Toast.makeText(this@ClassCorrectionActivity, "수정 완료", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@ClassCorrectionActivity, ClassDetailActivity::class.java)
                                intent.putExtra("data", classData)
                                startActivity(intent)
                                finish()
                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        binding.btnHomeClassCorrection.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesClassCorrection.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchClassCorrection.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingClassCorrection.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateType(item: DetailData) {
        val isChecked = BooleanArray(5){ i -> false }
        val type = item.content.split(", ")

        for(t in type) {
            when(t) {
                "취미" -> isChecked[0] = true
                "공부" -> isChecked[1] = true
                "운동" -> isChecked[2] = true
                "자격증" -> isChecked[3] = true
                "대회" -> isChecked[4] = true
            }
        }

        val schedules = arrayOf("취미", "공부", "운동", "자격증", "대회")

        val builder = AlertDialog.Builder(this)
            .setTitle("종류 선택")
            .setMultiChoiceItems(schedules, isChecked) { dialogInterface: DialogInterface, i: Int, b: Boolean ->
                if(b) {
                    isChecked[i] = true
                } else {
                    isChecked[i] = false
                }
            }
            .setPositiveButton("완료") { dialogInterface: DialogInterface, i: Int ->
                var checked = ""

                for(j in 0..4) {
                    if(isChecked[j]) {
                        checked += schedules[j] + ", "
                    }
                }

                if(checked == "") {
                    checked = "미정"
                } else {
                    checked = checked.subSequence(0, checked.length - 2).toString()
                }

                classData.type = checked

                initRecycler()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun updateSchedule(item: DetailData) {
        val isChecked = BooleanArray(10){ i -> false }
        val schedule = item.content.split(", ")

        for(s in schedule) {
            when(s) {
                "미정" -> isChecked[0] = true
                "매주" -> isChecked[1] = true
                "격주" -> isChecked[2] = true
                "월" -> isChecked[3] = true
                "화" -> isChecked[4] = true
                "수" -> isChecked[5] = true
                "목" -> isChecked[6] = true
                "금" -> isChecked[7] = true
                "토" -> isChecked[8] = true
                "일" -> isChecked[9] = true
            }
        }

        val schedules = arrayOf("미정", "매주", "격주", "월", "화", "수", "목", "금", "토", "일")

        val builder = AlertDialog.Builder(this)
            .setTitle("요일 선택")
            .setMultiChoiceItems(schedules, isChecked) {dialogInterface: DialogInterface, i: Int, b: Boolean ->
                if(b) {
                    isChecked[i] = true
                } else {
                    isChecked[i] = false
                }
            }
            .setPositiveButton("완료") {dialogInterface: DialogInterface, i: Int ->
                var checked = ""

                for(j in 0..9) {
                    if(isChecked[j]) {
                        checked += schedules[j] + ", "
                    }
                }

                if(checked == "") {
                    checked = "선택 안 함"
                } else {
                    checked = checked.subSequence(0, checked.length - 2).toString()
                }

                classData.schedule = checked

                initRecycler()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}