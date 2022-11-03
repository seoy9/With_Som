package hong.sy.withsom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.databinding.ActivityFindPwBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.mail.GMailSender
import hong.sy.withsom.random.RandomString

class FindPwActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding

    private val database = Firebase.database
    private val myRef = database.getReference("users")
    private var isHave = false
    private val temporaryPW = RandomString().getRandomPassword()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnFindPw.setOnClickListener {
            val regex = "(\\d+)@dongduk.ac.kr".toRegex()

            if(!(binding.edEmailFindPw.text.toString().matches(regex))) {
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                Toast.makeText(this, "동덕 이메일만 가능합니다.\n이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {

                isEmailExistence()
            }
        }

        binding.btnGoLogin.setOnClickListener {
            finish()
        }
    }

    private fun textChangedSetting() {
        binding.edEmailFindPw.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)

                if (p0.toString().length == 22) {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.dongduk)
                    binding.btnFindPw.setEnabled(true)
                } else {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.nonButton)
                    binding.btnFindPw.setEnabled(false)
                }
            }
        })
    }

    private fun isEmailExistence() {
        val email = binding.edEmailFindPw.text.toString()

        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.getChildren()) {
                    if (postSnapshot.child("email").getValue(String::class.java) != null) {
                        val e = postSnapshot.child("email").getValue(String::class.java)

                        if(email == e) {
                            val uid = postSnapshot.child("uid").getValue(String::class.java)
                            myRef.child(uid!!).child("pw").setValue(temporaryPW)
                            isHave = true
                        }
                    }
                    result()
                    break
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("FirebaseDatabase", "onCancelled", databaseError.toException())
            }
        }
        val sortbyAge: Query = database.getReference().child("users").orderByChild("users")
        sortbyAge.addListenerForSingleValueEvent(postListener)
    }

    private fun result() {
        if (isHave) {
            var email = binding.edEmailFindPw.text.toString()

            GMailSender().sendEmail(
                email,
                "임시 비밀번호입니다.",
                "${email} 님의 임시 비밀번호 : ${temporaryPW}"
            )
            Toast.makeText(this, "임시 비밀번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "존재하지 않는 이메일입니다.\n다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
