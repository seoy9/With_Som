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
import hong.sy.withsom.mail.GMailSender
import java.util.*

class SecessionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecessionBinding
    lateinit var spinner: Spinner
    private val why = arrayOf("탈퇴 사유를 선택하세요.", "이용하기 불편해서", "이용자가 적어서", "필요없는 서비스라서", "기타")
    private var reason = why[0]
    private val classList = ArrayList<Int>()
    private val className = ArrayList<String>()
    private val applicationList = ArrayList<Int>()
    private val applicationName = ArrayList<String>()
    private val database = Firebase.database

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
        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
        val id = SharedPreferenceManager.getUserEmail(this)
        val stName = SharedPreferenceManager.getUserName(this)
        val stEmail = SharedPreferenceManager.getUserEmail(this)

        database.getReference("applications").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (applicationsSnapshot in snapshot.children) {
                        for (applicationSnapshot in applicationsSnapshot.children) {
                            val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                            val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                            val stN =
                                applicationSnapshot.child("stNum").getValue(String::class.java)

                            if (stNum == stN) {
                                database.getReference("applications").child(stN).child(aid.toString()).removeValue()
                                applicationList.add(cid!!)
                            }
                        }
                    }
                    sendCancleApplicationMail()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        for(index in 0..applicationList.size-1) {
            database.getReference("classes").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (classSnapshot in snapshot.children) {
                            val cid = classSnapshot.child("cid").getValue(Int::class.java)
                            val name = classSnapshot.child("name").getValue(String::class.java)
                            val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)

                            if (applicationList[index] == cid) {

                                GMailSender().sendEmail(
                                    leaderID!!,
                                    "'${name}'에 취소자가 있습니다.",
                                    "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221109T042603Z&X-Amz-Expires=86400&X-Amz-Signature=f8df2991060c06b0a194a5a7e254add92dd239cf8188dc7c0041e37a070903a9&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${name} 신청 취소]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${name}'에 <b>${stNum} ${stName}</b>님께서 취소하셨습니다.\n\n\n<p style='background-color:#D3D3D3;'>${stName} 님의 이메일 : ${stEmail}\n\n취소 사유 : 회원 탈퇴</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                                )

                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        database.getReference("users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val email = classSnapshot.child("email").getValue(String::class.java)
                        val stN = classSnapshot.child("stNum").getValue(String::class.java)

                        if(id == email) {
                            database.getReference("users").child(stN!!).removeValue()
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
                        val name = classSnapshot.child("name").getValue(String::class.java)

                        if(id == leaderID) {
                            database.getReference("classes").child(cid.toString()).removeValue()
                            classList.add(cid!!)
                            className.add(name!!)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        for(index in 0..classList.size-1) {
            database.getReference("applications").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (applicationsSnapshot in snapshot.children) {
                            for (applicationSnapshot in applicationsSnapshot.children) {
                                val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                                val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                                val stN =
                                    applicationSnapshot.child("stNum").getValue(String::class.java)

                                if (classList[index] == cid) {
                                    database.getReference("applications").child(stN!!).child(aid.toString()).removeValue()

                                    val email = stN + "@dongduk.ac.kr"

                                    GMailSender().sendEmail(
                                        email,
                                        "'${className[index]}' 모임이 삭제되었습니다.",
                                        "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221109T042603Z&X-Amz-Expires=86400&X-Amz-Signature=f8df2991060c06b0a194a5a7e254add92dd239cf8188dc7c0041e37a070903a9&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${className[index]} 모임 삭제]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${className[index]}' 모임이 <b>삭제</b>되었습니다.\n\n\n<p style='background-color:#D3D3D3;'>삭제 사유 : 리더 회원 탈퇴</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                                    )
                                }
                            }
                        }
                        doneDeleteUser()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

    private fun sendDeleteClassMail() {
        val myRef = database.getReference("applications")

        for(index in 0..classList.size-1) {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (applicationsSnapshot in snapshot.children) {
                            for (applicationSnapshot in applicationsSnapshot.children) {
                                val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                                val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                                val stN =
                                    applicationSnapshot.child("stNum").getValue(String::class.java)

                                if (classList[index] == cid) {
                                    myRef.child(stN!!).child(aid.toString()).removeValue()

                                    val email = stN + "@dongduk.ac.kr"

                                    GMailSender().sendEmail(
                                        email,
                                        "'${className[index]}' 모임이 삭제되었습니다.",
                                        "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221109T042603Z&X-Amz-Expires=86400&X-Amz-Signature=f8df2991060c06b0a194a5a7e254add92dd239cf8188dc7c0041e37a070903a9&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${className[index]} 모임 삭제]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${className[index]}' 모임이 <b>삭제</b>되었습니다.\n\n\n<p style='background-color:#D3D3D3;'>삭제 사유 : 리더 회원 탈퇴</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                                    )
                                }
                            }
                        }
                        deleteUser()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun findApplicationList() {
        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
        val myRef = database.getReference("applications")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (applicationsSnapshot in snapshot.children) {
                        for (applicationSnapshot in applicationsSnapshot.children) {
                            val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                            val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                            val stN =
                                applicationSnapshot.child("stNum").getValue(String::class.java)

                            if (stNum == stN) {
                                myRef.child(stN).child(aid.toString()).removeValue()
                                applicationList.add(cid!!)
                            }
                        }
                    }
                    sendCancleApplicationMail()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun sendCancleApplicationMail() {
        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
        val stName = SharedPreferenceManager.getUserName(this)
        val stEmail = SharedPreferenceManager.getUserEmail(this)
        val myRef = database.getReference("classes")

        for(index in 0..applicationList.size-1) {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (classSnapshot in snapshot.children) {
                            val cid = classSnapshot.child("cid").getValue(Int::class.java)
                            val name = classSnapshot.child("name").getValue(String::class.java)
                            val leaderID = classSnapshot.child("leaderID").getValue(String::class.java)

                            if (applicationList[index] == cid) {

                                GMailSender().sendEmail(
                                    leaderID!!,
                                    "'${name}'에 취소자가 있습니다.",
                                    "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221109%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221109T042603Z&X-Amz-Expires=86400&X-Amz-Signature=f8df2991060c06b0a194a5a7e254add92dd239cf8188dc7c0041e37a070903a9&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${name} 신청 취소]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${name}'에 <b>${stNum} ${stName}</b>님께서 취소하셨습니다.\n\n\n<p style='background-color:#D3D3D3;'>${stName} 님의 이메일 : ${stEmail}\n\n취소 사유 : 회원 탈퇴</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                                )

                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        sendDeleteClassMail()
    }

    private fun doneDeleteUser() {
        val email = "dongduk.withsom@gmail.com"
        val id = SharedPreferenceManager.getUserEmail(this)
        val name = SharedPreferenceManager.getUserName(this)

        Toast.makeText(this, "사유 : ${reason}\n탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
        GMailSender().sendEmail(email, "${id} ${name} 님의 탈퇴", "${id} ${name} 님의 탈퇴\n\n사유 : ${reason}")

        SharedPreferenceManager.clearUser(this)

        finishAffinity()
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish()
    }
}