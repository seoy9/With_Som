package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hong.sy.withsom.databinding.ActivitySignUpImageBinding
import hong.sy.withsom.room.UserDao
import hong.sy.withsom.room.UserDatabase
import hong.sy.withsom.room.UserEntity

class SignUpImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpImageBinding

    lateinit var db: UserDatabase
    lateinit var userDao: UserDao
    lateinit var user: UserEntity

    private var profile = R.drawable.foundation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabase.getInstance(this)!!
        userDao = db.getUserDao()

        user = intent.getSerializableExtra("user") as UserEntity

        buttonSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupDone.setOnClickListener {
            insertUser()

//            finishAffinity()
//            val intent = Intent(this@SignUpImageActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
        }

        binding.btnFoundation.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.foundation)
            profile = R.drawable.foundation
        }

        binding.btnVision.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.vision)
            profile = R.drawable.vision
        }

        binding.btnSince.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.since)
            profile = R.drawable.since
        }

        binding.btnSimbol.setOnClickListener {
            binding.imgSelected.setImageResource(R.drawable.simbol)
            profile = R.drawable.simbol
        }
    }

    private fun insertUser() {
        user.profile = profile

        Thread {
            userDao.insertUser(user)
            Toast.makeText(this, "가입 완료", Toast.LENGTH_SHORT).show()

            finishAffinity()
            val intent = Intent(this@SignUpImageActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }.start()
    }
}