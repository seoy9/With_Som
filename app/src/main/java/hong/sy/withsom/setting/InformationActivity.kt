package hong.sy.withsom.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hong.sy.withsom.*
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    lateinit var binding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnClassesInformation.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchInformation.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeInformation.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingInformation.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}