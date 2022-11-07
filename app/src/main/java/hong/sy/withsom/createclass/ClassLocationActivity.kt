package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassLocationBinding
import java.io.Serializable

class ClassLocationActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassLocationBinding
    private var total = ""
    lateinit var title: String
    lateinit var content: String
    lateinit var classData: ClassData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        title = intent.getStringExtra("title").toString()
        content = intent.getStringExtra("content").toString()

        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassLocationNext.setOnClickListener {
            val location = binding.edClassLocation.text.toString()

            total += "모임 위치 : " + location + "\n"
            classData.location = binding.edClassLocation.text.toString()

            val intent = Intent(this, ClassNumberActivity::class.java)
            intent.putExtra("total", total)
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnHomeLocation.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesLocation.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchLocation.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingLocation.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassLocation.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassLocationNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassLocationNext.setEnabled(true)
                } else {
                    binding.btnClassLocationNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassLocationNext.setEnabled(false)
                }
            }
        })
    }
}