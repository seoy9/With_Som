package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassNameBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.room.*
import java.io.Serializable

class ClassNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassNameBinding
    private var total = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassNameNext.setOnClickListener {

            val name = binding.edClassName.text.toString()
            val leaderID = SharedPreferenceManager.getUserId(this)
            total += "모임 이름 : " + name + "\n"

            val classEntity = ClassEntity(null, name, "", "", "", 0, 0, "", "", "", leaderID, "")

            val intent = Intent(this, ClassTypeActivity::class.java)
            intent.putExtra("total", total)
//            intent.putExtra("title", binding.edClassName.text.toString())
            intent.putExtra("data", classEntity as Serializable)
            startActivity(intent)
        }

        binding.btnHomeName.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesName.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchName.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingName.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassNameNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassNameNext.setEnabled(true)
                } else {
                    binding.btnClassNameNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassNameNext.setEnabled(false)
                }
            }
        })
    }
}