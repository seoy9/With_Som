package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.UserData
import hong.sy.withsom.databinding.ActivitySignUpImageBinding
import hong.sy.withsom.login.SharedPreferenceManager

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
            user.id = user.stNum

            Thread {
                myRef.child(user.stNum).setValue(user)

                this.runOnUiThread {
                    Toast.makeText(this@SignUpImageActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()
                }

                //finishAffinity()
                val intent = Intent(this@SignUpImageActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }.start()
        }

        binding.btnFoundation.setOnClickListener {
            selectedImage = R.drawable.foundation
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnVision.setOnClickListener {
            selectedImage = R.drawable.vision
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnSince.setOnClickListener {
            selectedImage = R.drawable.since
            binding.imgSelected.setImageResource(selectedImage)
        }

        binding.btnSimbol.setOnClickListener {
            selectedImage = R.drawable.simbol
            binding.imgSelected.setImageResource(selectedImage)
        }
    }
}