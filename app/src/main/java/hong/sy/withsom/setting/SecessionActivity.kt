package hong.sy.withsom.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.*
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.databinding.ActivitySecessionBinding
import hong.sy.withsom.login.SharedPreferenceManager

class SecessionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecessionBinding
    lateinit var spinner: Spinner
    private val why = arrayOf("탈퇴 사유를 선택하세요.", "이용하기 불편해서", "이용자가 적어서", "필요없는 서비스라서", "기타")
    private var reason = why[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerSetting()

        buttonSetting()
    }

    private fun spinnerSetting() {
        spinner = binding.secessionSpinner
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter(this, android.R.layout.simple_list_item_1, why)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun buttonSetting() {
        binding.btnSecessionDone.setOnClickListener {
            reason = spinner.selectedItem.toString()

            if(reason == why[0]) {
                Toast.makeText(this, "탈퇴 사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                deleteUser()
            }
        }

        binding.btnSettingSecession.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesSecession.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHomeSecession.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchSecession.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun deleteUser() {
        val id = SharedPreferenceManager.getUserEmail(this)
        val database = Firebase.database

        database.getReference("users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val email = classSnapshot.child("email").getValue(String::class.java)
                        val stNum = classSnapshot.child("stNum").getValue(String::class.java)

                        if(id == email) {
                            database.getReference("users").child(stNum!!).removeValue()
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        database.getReference("classes").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)
                        val cid = classSnapshot.child("cid").getValue(Int::class.java)

                        if(id == leaderID) {
                            database.getReference("classes").child(cid.toString()).removeValue()
                            doneDeleteUser()
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun doneDeleteUser() {
        Toast.makeText(this, "사유 : ${reason}\n탈퇴되었습니다.", Toast.LENGTH_SHORT).show()

        SharedPreferenceManager.clearUser(this)

        finishAffinity()
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish()
    }
}