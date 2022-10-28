package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassContentBinding
import java.io.Serializable

class ClassContentActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassContentBinding
    private var total = ""
    lateinit var title: String
    lateinit var classData: ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        title = intent.getStringExtra("title").toString()

        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassContentNext.setOnClickListener {
            total += "모임 소개 : " + binding.edClassContent.text.toString() + "\n"

            classData.content = binding.edClassContent.text.toString()

            val intent = Intent(this, ClassLocationActivity::class.java)
            intent.putExtra("total", total)
//            intent.putExtra("title", title)
//            intent.putExtra("content", binding.edClassContent.text.toString())
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnHomeContent.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesContent.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchContent.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingContent.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassContent.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassContentNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassContentNext.setEnabled(true)
                } else {
                    binding.btnClassContentNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassContentNext.setEnabled(false)
                }
            }
        })
    }
}