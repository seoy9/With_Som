package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.UserData
import hong.sy.withsom.databinding.ActivitySignUpImageBinding

class SignUpImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpImageBinding
    lateinit var  user: UserData
    private var selectedImage = R.drawable.foundation

    private val database = Firebase.database
    private val myRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getSerializableExtra("user") as UserData

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupDone.setOnClickListener {
            user.profile = selectedImage

            myRef.child(user.stNum).setValue(user)

            Toast.makeText(this@SignUpImageActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@SignUpImageActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
        }

        binding.btnSomSmile.setOnClickListener {
            selectedImage = R.drawable.som_smile
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnSomKirakira.setOnClickListener {
            selectedImage = R.drawable.som_kirakira
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnSomSunglass.setOnClickListener {
            selectedImage = R.drawable.som_sunglass
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnSomLaugh.setOnClickListener {
            selectedImage = R.drawable.som_laugh
            binding.imgSelected.setImageResource(selectedImage)
        }
    }
}