package hong.sy.withsom.setting

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
import hong.sy.withsom.*
import hong.sy.withsom.databinding.ActivityCorrectionBinding
import hong.sy.withsom.login.SharedPreferenceManager

class CorrectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityCorrectionBinding

    private val database = Firebase.database
    private val myRef = database.getReference("users")
    private var isHave = false
    private var profile = R.drawable.som_default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCorrectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        defaultSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {

        binding.btnCorrectionSomSmile.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.som_smile)
            profile = R.drawable.som_smile
        }

        binding.btnCorrectionSomKirakira.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.som_kirakira)
            profile = R.drawable.som_kirakira
        }

        binding.btnCorrectionSomSumglass.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.som_sunglass)
            profile = R.drawable.som_sunglass
        }

        binding.btnCorrectionSomLaugh.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.som_laugh)
            profile = R.drawable.som_laugh
        }

        binding.btnCorrectionDone.setOnClickListener {
            val name = binding.edCorrectionName.text.toString()
            val depart = binding.edCorrectionDepart.text.toString()
            val pw = binding.edCorrectionPw.text.toString()
            val repw = binding.edCorrectionRePw.text.toString()
            val email = SharedPreferenceManager.getUserEmail(this)

            if(pw != repw) {
                Toast.makeText(this, "비밀번호가 다릅니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                binding.edCorrectionRePw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
            } else {
                myRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            for(userSnapshot in snapshot.children) {
                                val e = userSnapshot.child("email").getValue(String::class.java)

                                if (email == e) {
                                    val uid = userSnapshot.child("uid").getValue(String::class.java)
                                    myRef.child(uid!!).child("name").setValue(name)
                                    myRef.child(uid).child("depart").setValue(depart)
                                    myRef.child(uid).child("profile").setValue(profile)
                                    if (pw != "" && repw != "") {
                                        myRef.child(uid).child("pw").setValue(pw)
                                    }
                                    break
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                Toast.makeText(this, "수정 완료", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        binding.btnClassesCorrection.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchCorrection.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeCorrection.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingCorrection.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun defaultSetting() {
        val email = SharedPreferenceManager.getUserEmail(this)
        var name = ""
        var depart = ""

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapshot in snapshot.children) {
                        val e = userSnapshot.child("email").getValue(String::class.java)

                        if(email == e) {
                            name = userSnapshot.child("name").getValue(String::class.java)!!
                            depart = userSnapshot.child("depart").getValue(String::class.java)!!
                            profile = userSnapshot.child("profile").getValue(Int::class.java)!!
                            isHave = true
                            result(name, depart, profile)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun result(name: String, depart: String, profile: Int) {
        if (isHave) {
            binding.tvCorrectionEmail.text = SharedPreferenceManager.getUserEmail(this)
            binding.edCorrectionName.setText(name)
            binding.edCorrectionDepart.setText(depart)
            binding.imgCorrection.setImageResource(profile)
        }
    }

    private fun textChangedSetting() {
        binding.edCorrectionRePw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edCorrectionRePw.backgroundTintList =
                    ContextCompat.getColorStateList(applicationContext, R.color.black)
            }
        })
    }
}