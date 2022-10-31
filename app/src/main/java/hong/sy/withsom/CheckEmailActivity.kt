package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import hong.sy.withsom.databinding.ActivityCheckEmailBinding
import hong.sy.withsom.room.UserEntity
import java.io.Serializable

class CheckEmailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckEmailBinding

    private lateinit var user: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getSerializableExtra("user") as UserEntity

        textChangedSetting()

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnCheckEmail.setOnClickListener {
            val certificationNum = "1234"

            if(binding.edCheckCertification.text.toString() == certificationNum) {
                Toast.makeText(this, "메일 인증 완료", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, SignUpImageActivity::class.java)
                intent.putExtra("user", user as Serializable)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "인증번호가 다릅니다.\n다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun textChangedSetting() {
        binding.edCheckCertification.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if(p0.length >= 4) {
                        binding.btnCheckEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                        binding.btnCheckEmail.setEnabled(true)
                    } else {
                        binding.btnCheckEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                        binding.btnCheckEmail.setEnabled(false)
                    }
                }
            }
        })
    }
}