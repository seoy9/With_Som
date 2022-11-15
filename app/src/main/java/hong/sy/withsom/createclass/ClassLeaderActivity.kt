package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.*
import hong.sy.withsom.classList.ClassDetailActivity
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassLeaderBinding
import hong.sy.withsom.setting.SettingActivity
import java.io.Serializable

class ClassLeaderActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassLeaderBinding
    private var total = ""
    lateinit var classData: ClassData

    private val database = Firebase.database
    private val myRef = database.getReference("classes")
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        total += intent.getStringExtra("total")

        classData = intent.getSerializableExtra("data") as ClassData

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassLeaderDone.setOnClickListener {
            val leaderContent = binding.edClassLeader.text.toString()

            total += "리더 소개 : " + leaderContent
            classData.leaderContent = leaderContent

            //Toast.makeText(this, total, Toast.LENGTH_LONG).show()

            id = System.currentTimeMillis().toInt()

            Thread {
                classData.cid = id

                myRef.child(classData.cid.toString()).setValue(classData)

                finishAffinity()
                val intent = Intent(this, ClassDetailActivity::class.java)
                intent.putExtra("data", classData as Serializable)
                intent.putExtra("where", "createClass")
                startActivity(intent)
            }.start()
        }

        binding.btnHomeLeader.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesLeader.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchLeader.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingLeader.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edClassLeader.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 0) {
                    binding.btnClassLeaderDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnClassLeaderDone.setEnabled(true)
                } else {
                    binding.btnClassLeaderDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnClassLeaderDone.setEnabled(false)
                }
            }
        })
    }
}