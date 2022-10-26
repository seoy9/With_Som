package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivityCheckEmailBinding

class CheckEmailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnCheckEmail.setOnClickListener {
            val certificationNum = "1234"

            if(binding.edCheckCertification.text.toString() == certificationNum) {
                Toast.makeText(this, "메일 인증 완료", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, SignUpImageActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "인증번호가 다릅니다.\n다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}