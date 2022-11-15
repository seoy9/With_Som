package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassNumberBinding
import hong.sy.withsom.setting.SettingActivity
import java.io.Serializable

class ClassNumberActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassNumberBinding
    private var total = ""
    lateinit var classData: ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassNumberNext.setOnClickListener {
            val totalNum = binding.edClassNumber.text.toString()

            total += "모집 인원 : " + totalNum + "\n"
            classData.totalNum = totalNum.toInt()

            val intent = Intent(this, ClassMemberActivity::class.java)
            intent.putExtra("total", total)
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnHomeNumber.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesNumber.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchNumber.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingNumber.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassNumber.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassNumberNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassNumberNext.setEnabled(true)
                } else {
                    binding.btnClassNumberNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassNumberNext.setEnabled(false)
                }
            }
        })
    }
}