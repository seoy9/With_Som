package hong.sy.withsom.setting

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
import hong.sy.withsom.*
import hong.sy.withsom.databinding.ActivityCorrectionBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.mail.GMailSender

class CorrectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityCorrectionBinding

    private val database = Firebase.database
    private val myRef = database.getReference("users")
    private var isHave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCorrectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSetting()

        defaultSetting()

        textChangedSetting()
    }

    private fun buttonSetting() {
        binding.btnCorrectionFoundation.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.foundation)
        }

        binding.btnCorrectionSimbol.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.simbol)
        }

        binding.btnCorrectionSince.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.since)
        }

        binding.btnCorrectionVision.setOnClickListener {
            binding.imgCorrection.setImageResource(R.drawable.vision)
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
                val postListener: ValueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.getChildren()) {
                            if (postSnapshot.child("email").getValue(String::class.java) != null) {
                                val e = postSnapshot.child("email").getValue(String::class.java)

                                if(email == e) {
                                    val uid = postSnapshot.child("uid").getValue(String::class.java)
                                    myRef.child(uid!!).child("name").setValue(name)
                                    myRef.child(uid).child("depart").setValue(depart)
                                    if(pw != "" && repw != "") {
                                        myRef.child(uid).child("pw").setValue(pw)
                                    }
                                }
                            }
                            break
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("FirebaseDatabase", "onCancelled", databaseError.toException())
                    }
                }
                val sortbyAge: Query = database.getReference().child("users").orderByChild("users")
                sortbyAge.addListenerForSingleValueEvent(postListener)

                var list = "이름: " + binding.edCorrectionName.text.toString()
                list += "\n학과: " + binding.edCorrectionDepart.text.toString()
                list += "\n비밀번호: " + binding.edCorrectionPw.text.toString()
                list += "\n비밀번호 확인: " + binding.edCorrectionRePw.text.toString()

                Toast.makeText(this, list + "\n수정 완료", Toast.LENGTH_LONG).show()
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

        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.getChildren()) {
                    if (postSnapshot.child("email").getValue(String::class.java) != null) {
                        val e = postSnapshot.child("email").getValue(String::class.java)

                        if(email == e) {
                            name = postSnapshot.child("name").getValue(String::class.java)!!
                            depart = postSnapshot.child("depart").getValue(String::class.java)!!
                            isHave = true
                        }
                    }
                    result(name, depart)
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

    private fun result(name: String, depart: String) {
        if (isHave) {
            binding.tvCorrectionEmail.text = SharedPreferenceManager.getUserEmail(this)
            binding.edCorrectionName.setText(name)
            binding.edCorrectionDepart.setText(depart)
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