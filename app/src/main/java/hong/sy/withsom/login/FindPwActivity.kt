package hong.sy.withsom.login

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
import hong.sy.withsom.databinding.ActivityFindPwBinding
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
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.red
                )
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )
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
                binding.edEmailFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.black
                )

                if (p0.toString().length == 22) {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.dongduk
                    )
                    binding.btnFindPw.setEnabled(true)
                } else {
                    binding.btnFindPw.backgroundTintList = ContextCompat.getColorStateList(applicationContext,
                        R.color.nonButton
                    )
                    binding.btnFindPw.setEnabled(false)
                }
            }
        })
    }

    private fun isEmailExistence() {
        val email = binding.edEmailFindPw.text.toString()

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapshot in snapshot.children) {
                        val e = userSnapshot.child("email").getValue(String::class.java)

                        if(email == e) {
                            val uid = userSnapshot.child("uid").getValue(String::class.java)
                            myRef.child(uid!!).child("pw").setValue(temporaryPW)
                            isHave = true
                            result()
                            isHave = false
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun result() {
        if (isHave) {
            val email = binding.edEmailFindPw.text.toString()

            GMailSender().sendEmail(
                email,
                "임시 비밀번호입니다.",
                "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221109T042603Z&X-Amz-Expires=86400&X-Amz-Signature=f8df2991060c06b0a194a5a7e254add92dd239cf8188dc7c0041e37a070903a9&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[로그인 비밀번호 초기화]</b></h3>\n\n현재 보드리는 비밀번호는 임시로 제공되는 비밀번호입니다.\n\n로그인 후 '개인정보 수정'에서 비밀번호를 변경하고 사용하시길 바랍니다.\n\n\n<p style='background-color:#D3D3D3;'>${email} 님의 임시 비밀번호 : <b>${temporaryPW}</b></p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
            )

            Toast.makeText(this, "임시 비밀번호를 메일로 전송했습니다.", Toast.LENGTH_SHORT).show()

            isHave = false
        } else {
            Toast.makeText(this, "존재하지 않는 이메일입니다.\n다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
