package hong.sy.withsom.createclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivityClassLeaderBinding
import hong.sy.withsom.room.ClassDao
import hong.sy.withsom.room.ClassDatabase
import hong.sy.withsom.room.ClassEntity
import java.io.Serializable

class ClassLeaderActivity : AppCompatActivity() {
    lateinit var binding: ActivityClassLeaderBinding
    private var total = ""
    lateinit var classEntity: ClassEntity

    private lateinit var db: ClassDatabase
    private lateinit var classDao: ClassDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ClassDatabase.getInstance(this)!!
        classDao = db.getClassDao()

        total += intent.getStringExtra("total")

        classEntity = intent.getSerializableExtra("data") as ClassEntity

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnClassLeaderDone.setOnClickListener {
            val leaderContent = binding.edClassLeader.text.toString()

            total += "리더 소개 : " + leaderContent
            classEntity.leaderContent = leaderContent

            Toast.makeText(this, total, Toast.LENGTH_LONG).show()

            insertClass()
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

    private fun insertClass() {
        Thread {
            classDao.insertClass(classEntity)

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object: Runnable {
                override fun run() {
                    Toast.makeText(this@ClassLeaderActivity, "insert", Toast.LENGTH_SHORT).show()
                }
            }, 0)

            val intent = Intent(this, ClassDetailActivity::class.java)
            intent.putExtra("data", classEntity as Serializable)
            startActivity(intent)
        }.start()
    }
}