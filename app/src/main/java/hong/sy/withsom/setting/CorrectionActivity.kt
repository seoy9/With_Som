package hong.sy.withsom.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.*
import hong.sy.withsom.databinding.ActivityCorrectionBinding

class CorrectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityCorrectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCorrectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCorrectionFoundation.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.foundation)
        }

        binding.btnCorrectionSimbol.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.simbol)
        }

        binding.btnCorrectionSince.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.since)
        }

        binding.btnCorrectionVision.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.vision)
        }

        binding.btnCorrectionDone.setOnClickListener {
            var list = "학번: " + binding.tvCorrectionStNum.text.toString()
            list += "\n학과: " + binding.edCorrectionDepart.text.toString()
            list += "\n비밀번호: " + binding.edCorrectionPw.text.toString()
            list += "\n비밀번호 확인: " + binding.edCorrectionRePw.text.toString()

            Toast.makeText(this, list + "\n수정 완료", Toast.LENGTH_LONG).show()
        }

        binding.btnClassesCorrection.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchCorrection.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnHomeCorrection.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingCorrection.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}