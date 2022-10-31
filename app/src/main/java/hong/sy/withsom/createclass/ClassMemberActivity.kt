package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassMemberBinding
import hong.sy.withsom.room.ClassEntity
import java.io.Serializable

class ClassMemberActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassMemberBinding
    private var total = ""
    lateinit var classEntity: ClassEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        classEntity = intent.getSerializableExtra("data") as ClassEntity

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassMemberNext.setOnClickListener {
            val member = binding.edClassMember.text.toString()

            total += "모집 대상 : " + member + "\n"
            classEntity.member = member

            val intent = Intent(this, ClassScheduleActivity::class.java)
            intent.putExtra("total", total)
            intent.putExtra("data", classEntity as Serializable)
            startActivity(intent)
        }

        binding.btnHomeMember.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesMember.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchMember.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingMember.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassMember.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassMemberNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassMemberNext.setEnabled(true)
                } else {
                    binding.btnClassMemberNext.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassMemberNext.setEnabled(false)
                }
            }
        })
    }
}