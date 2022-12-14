package hong.sy.withsom.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.R
import hong.sy.withsom.data.UserData
import hong.sy.withsom.databinding.ActivitySignUpBinding
import hong.sy.withsom.mail.GMailSender
import hong.sy.withsom.random.RandomString
import java.io.Serializable

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private var isHave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnSignupNext.setOnClickListener {
            if(checkContent() && checkStNum() && checkEmail() && checkPw() && checkRePw() && checkPrivacy()) {
                isEmailExistence()
            }
        }

        binding.checkBoxPrivacy.setOnClickListener {
            if(binding.checkBoxPrivacy.isChecked) {
                binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.black))
            }
        }
    }

    private fun checkContent() : Boolean {
        val name = binding.edName.text
        val stNum = binding.edStNum.text
        val depart = binding.edDepart.text
        val email = binding.edEmail.text
        val pw = binding.edPw.text
        val repw = binding.edPwCheck.text
        var isFalse = false

        if(name.isNullOrBlank()) {
            binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(stNum.isNullOrBlank()) {
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(depart.isNullOrBlank()) {
            binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(email.isNullOrBlank()) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(pw.isNullOrBlank()) {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(repw.isNullOrBlank()) {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            isFalse = true
        } else {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.black
            )
        }

        if(isFalse) {
            Toast.makeText(this, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )

        return true
    }

    private fun checkStNum() : Boolean {
        val stNum = binding.edStNum.text.toString()

        if(stNum.length != 8) {
            Toast.makeText(this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            return false
        }

        binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        return true
    }

    private fun checkPw() : Boolean {
        val pw = binding.edPw.text.toString()

        if(pw.length < 8) {
            binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "??????????????? 8?????? ??????????????? ?????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        return true
    }

    private fun checkRePw() : Boolean {
        val pw = binding.edPw.text.toString()
        val repw = binding.edPwCheck.text.toString()

        if(pw != repw) {
            binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        return true
    }

    private fun checkPrivacy() : Boolean {
        if(!binding.checkBoxPrivacy.isChecked) {
            binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.red))
            Toast.makeText(this, "???????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.checkBoxPrivacy.setTextColor(getResources().getColor(R.color.black))
        return true
    }

    private fun checkEmail() : Boolean {
        if(!binding.edEmail.text.toString().contains("@dongduk.ac.kr")) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.edEmail.text.toString().length != 22) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        val stnNum = binding.edEmail.text.toString().subSequence(0, 8)
        val regex = "(\\d+)".toRegex()

        if(!(stnNum.matches(regex))) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
            R.color.black
        )
        return true
    }

    private fun textChangedSetting() {
        binding.edName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edName.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })

        binding.edStNum.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edStNum.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })

        binding.edDepart.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edDepart.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })

        binding.edEmail.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })

        binding.edPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })

        binding.edPwCheck.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edPwCheck.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
            }
        })
    }

    private fun isEmailExistence() {
        val id = binding.edEmail.text.toString()
        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)

                        if (id == email) {
                            isHave = true
                        }
                    }
                }
                isCheckExtension()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isCheckExtension() {
        if(isHave) {
            binding.edEmail.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                R.color.red
            )
            Toast.makeText(this, "?????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
            isHave = false
        } else {
            val email = binding.edEmail.text.toString()
            val pw = binding.edPw.text.toString()
            val name = binding.edName.text.toString()
            val stNum = binding.edStNum.text.toString()
            val depart = binding.edDepart.text.toString()
            val user = UserData(stNum, email, pw, name, stNum, depart, -1)

            val certificationNum = RandomString().getRandomCertificationNum()

            GMailSender().sendEmail(
                email,
                "????????? ?????????????????????.",
                "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221114%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221114T051746Z&X-Amz-Expires=86400&X-Amz-Signature=e6face4b2f5831a7ab8051945fad5ac13309458b8231252b7740ae348e66c409&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som ??????' width='20%' height='20%'><h3><b>[????????? ??????]</b></h3>\n\n???????????????. With Som?????????.\n\n???????????? ?????? ??????????????? ?????? ?????? ????????? ???????????? ????????? ????????? ??????????????????.\n\n??????, ????????? ????????? ???????????? ?????? ?????? <a href='mailto:dongduk.withsom@gmail.com' style='color:#0000FF;'>dongduk.withsom@gmail.com</a>?????? ??????????????? ????????????.\n\n\n<p style='background-color:#D3D3D3;'>${email} ?????? ???????????? : <b>${certificationNum}</b></p>\n\n<hr/><small>With Som ????????? dongduk.withsom@gmail.com</small>"
            )
            Toast.makeText(this, "??????????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CheckEmailActivity::class.java)
            intent.putExtra("certificationNum", certificationNum)
            intent.putExtra("user", user as Serializable)
            startActivity(intent)
        }
    }
}
