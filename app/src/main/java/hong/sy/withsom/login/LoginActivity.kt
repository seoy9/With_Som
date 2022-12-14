package hong.sy.withsom.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.LoadingActivity
import hong.sy.withsom.MainActivity
import hong.sy.withsom.R
import hong.sy.withsom.classList.ClassesActivity
import hong.sy.withsom.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isIDdone = false
    private var isPWdone = false

    private val database = Firebase.database

    private var isHave = false
    private var isCorrect = false
    lateinit var name: String

    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textchangedSetting()

        buttonSetting()

        if(SharedPreferenceManager.getUserId(this).isNullOrBlank() || SharedPreferenceManager.getUserPass(this).isNullOrBlank()) {
            SharedPreferenceManager.clearUser(this)
            login()
        } else {
            Toast.makeText(this, "${SharedPreferenceManager.getUserId(this)}님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login() {
        binding.btnLoginDone.setOnClickListener {
            val regex = "(\\d+)@dongduk.ac.kr".toRegex()

            if(!(binding.edLoginEmail.text.toString().matches(regex))) {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.red
                )
                binding.edLoginPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                isEmailExistence()
            }
        }
    }

    private fun buttonSetting() {
        binding.tvFindPw.setOnClickListener {
            val intent = Intent(this, FindPwActivity::class.java)
            startActivity(intent)
        }
    }

    private fun textchangedSetting() {
        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edLoginEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )

                if (p0 != null) {
                    if(p0.length >= 22) {
                        isIDdone = true
                    } else {
                        isIDdone = false
                    }
                }

                if(isIDdone && isPWdone) {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.dongduk
                    )
                    binding.btnLoginDone.setEnabled(true)
                } else {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.nonButton
                    )
                    binding.btnLoginDone.setEnabled(false)
                }
            }
        })

        binding.edLoginPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if(p0.length >= 8) {
                        isPWdone = true
                    } else {
                        isPWdone = false
                    }
                }

                if(isIDdone && isPWdone) {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.dongduk
                    )
                    binding.btnLoginDone.setEnabled(true)
                } else {
                    binding.btnLoginDone.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.nonButton
                    )
                    binding.btnLoginDone.setEnabled(false)
                }
            }
        })
    }

    private fun isEmailExistence() {
        val id = binding.edLoginEmail.text.toString()
        val pw = binding.edLoginPw.text.toString()
        val myRef = database.getReference("users")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        val password = userSnapshot.child("pw").getValue(String::class.java)
                        name = userSnapshot.child("name").getValue(String::class.java)!!

                        if(id == email) {
                            isHave = true
                            if(pw == password) {
                                isCorrect = true
                            }
                            break
                        }
                    }
                }
                result()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun result() {
        if(isHave) {
            if(isCorrect) {
                SharedPreferenceManager.setUserEmail(this, binding.edLoginEmail.text.toString())
                SharedPreferenceManager.setUserName(this, name)

                if (binding.checkBoxAutologin.isChecked) {
                    SharedPreferenceManager.setUserId(this, binding.edLoginEmail.text.toString())
                    SharedPreferenceManager.setUserPass(this, binding.edLoginPw.text.toString())
                }

                Toast.makeText(this, "${name}님 로그인되었습니다.", Toast.LENGTH_SHORT).show()

                finishAffinity()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}