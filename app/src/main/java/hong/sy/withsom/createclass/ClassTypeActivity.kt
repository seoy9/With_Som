package hong.sy.withsom.createclass

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import hong.sy.withsom.ClassesActivity
import hong.sy.withsom.MainActivity
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.SettingActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassTypeBinding
import java.io.Serializable

class ClassTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassTypeBinding
    private val isChecked = BooleanArray(5){ i -> false }
    private var total = ""
    lateinit var classData : ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")
        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnTypeDialog.setOnClickListener {
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
                        checked = "선택 안 함"
                    } else {
                        checked = checked.subSequence(0, checked.length - 2).toString()
                    }

                    binding.tvTypeChecked.text = checked
                }
                .show()
        }

        binding.btnClassTypeNext.setOnClickListener {
            var type = binding.tvTypeChecked.text.toString()

            if(type == "선택 안 함") {
                type = "기타"
            }

            total += "모임 종류 : " + type + "\n"
            classData.type = type

            val intent = Intent(this, ClassContentActivity::class.java)
            intent.putExtra("total", total)
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnHomeType.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesType.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchType.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingType.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}