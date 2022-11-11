package hong.sy.withsom.createclass

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassScheduleBinding
import java.io.Serializable

class ClassScheduleActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassScheduleBinding
    private var total = ""
    private val isChecked = BooleanArray(10){ i -> false }
    lateinit var classData: ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnScheduleDialog.setOnClickListener {
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

                    binding.tvScheduleChecked.text = checked
                }
                .show()
        }

        binding.btnClassScheduleNext.setOnClickListener {
            var schedule = binding.tvScheduleChecked.text.toString()

            if(schedule == "선택 안 함") {
                schedule = "미정"
            }

            val scheduleDetail = binding.edClassScheduleDetail.text.toString()

            total += "모임 일정 : " + schedule + "\n"
            total += "모임 일정 상세 소개 : " + scheduleDetail + "\n"
            classData.schedule = schedule
            classData.scheduleDetail = scheduleDetail


            val intent = Intent(this, ClassLeaderActivity::class.java)
            intent.putExtra("total", total)
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnHomeSchedule.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesSchedule.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchSchedule.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingSchedule.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassScheduleDetail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassScheduleNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, hong.sy.withsom.R.color.dongduk)
                    binding.btnClassScheduleNext.setEnabled(true)
                } else {
                    binding.btnClassScheduleNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, hong.sy.withsom.R.color.nonButton)
                    binding.btnClassScheduleNext.setEnabled(false)
                }
            }
        })
    }
}