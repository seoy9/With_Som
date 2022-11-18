package hong.sy.withsom.classList

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MultiAutoCompleteTextView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.MainActivity
import hong.sy.withsom.R
import hong.sy.withsom.SearchActivity
import hong.sy.withsom.setting.SettingActivity
import hong.sy.withsom.data.ApplicationData
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassDetailBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.mail.GMailSender
import hong.sy.withsom.recyclerView.DetailRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator
import java.io.Serializable
import java.util.*

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassDetailBinding

    lateinit var detailAdapter: DetailRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    lateinit var classData: ClassData
    lateinit var where: String

    private val database = Firebase.database

    val applicationList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classData = intent.getSerializableExtra("data") as ClassData
        where = intent.getStringExtra("where").toString()

        if(where == "createClass") {
            Toast.makeText(this, "개설되었습니다.", Toast.LENGTH_SHORT).show()
        }

        myApplicationList()

        initUserData()

        initRecycler()

        buttonSetting()

        isShowButton()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(where == "createClass") {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        MyListActivity().isBack = true
    }

    private fun initUserData() {
        val myRef = database.getReference("users")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        val profile = userSnapshot.child("profile").getValue(Int::class.java)
                        val stNum = userSnapshot.child("stNum").getValue(String::class.java)
                        val name = userSnapshot.child("name").getValue(String::class.java)

                        if(email == classData.leaderID) {
                            binding.imgLeaderDetail.setImageResource(profile!!)
                            binding.tvDetailName.text = classData.name + "\n" + stNum!! + " " + name!!
                            binding.tvLocationDetail.text = classData.location
                            binding.tvScheduleDetail.text = classData.schedule
                            binding.tvNumberDetail.text = classData.currentNum.toString() + "/" + classData.totalNum.toString() + "명"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isShowButton() {
       val id = SharedPreferenceManager.getUserEmail(this)

        if(id == classData.leaderID) {
            binding.btnClassDelete.visibility = View.VISIBLE
            binding.btnClassDelete.setEnabled(true)
            binding.btnClassCorrection.visibility = View.VISIBLE
            binding.btnClassCorrection.setEnabled(true)
            binding.btnClassAddNum.visibility = View.VISIBLE
            binding.btnClassAddNum.setEnabled(true)
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnApplication.setEnabled(false)
            binding.btnApplicationCancel.visibility = View.INVISIBLE
            binding.btnApplicationCancel.setEnabled(false)
        } else if(applicationList.contains(classData.cid)) {
            binding.btnClassDelete.visibility = View.INVISIBLE
            binding.btnClassDelete.setEnabled(false)
            binding.btnClassCorrection.visibility = View.INVISIBLE
            binding.btnClassCorrection.setEnabled(false)
            binding.btnClassAddNum.visibility = View.INVISIBLE
            binding.btnClassAddNum.setEnabled(false)
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnApplication.setEnabled(false)
            binding.btnApplicationCancel.visibility = View.VISIBLE
            binding.btnApplicationCancel.setEnabled(true)
        } else if(where == "myApplication") {
            binding.btnClassDelete.visibility = View.INVISIBLE
            binding.btnClassDelete.setEnabled(false)
            binding.btnClassCorrection.visibility = View.INVISIBLE
            binding.btnClassCorrection.setEnabled(false)
            binding.btnClassAddNum.visibility = View.INVISIBLE
            binding.btnClassAddNum.setEnabled(false)
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnApplication.setEnabled(false)
            binding.btnApplicationCancel.visibility = View.VISIBLE
            binding.btnApplicationCancel.setEnabled(true)
        } else if(where == "myClass" || where == "createClass") {
            binding.btnClassDelete.visibility = View.VISIBLE
            binding.btnClassDelete.setEnabled(true)
            binding.btnClassCorrection.visibility = View.VISIBLE
            binding.btnClassCorrection.setEnabled(true)
            binding.btnClassAddNum.visibility = View.VISIBLE
            binding.btnClassAddNum.setEnabled(true)
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnApplication.setEnabled(false)
            binding.btnApplicationCancel.visibility = View.INVISIBLE
            binding.btnApplicationCancel.setEnabled(false)
        } else {
            binding.btnClassDelete.visibility = View.INVISIBLE
            binding.btnClassDelete.setEnabled(false)
            binding.btnClassCorrection.visibility = View.INVISIBLE
            binding.btnClassCorrection.setEnabled(false)
            binding.btnClassAddNum.visibility = View.INVISIBLE
            binding.btnClassAddNum.setEnabled(false)
            binding.btnApplication.visibility = View.VISIBLE
            binding.btnApplication.setEnabled(true)
            binding.btnApplicationCancel.visibility = View.INVISIBLE
            binding.btnApplicationCancel.setEnabled(false)
        }
    }

    private fun initRecycler() {
        detailAdapter = DetailRecyclerViewAdapter(this)
        binding.rvDetail.adapter = detailAdapter

        if(classData == null) {

            datas.apply {
                add(DetailData(title = "모임 소개", content = "솜솜이를 사랑하시는 분!\n같이 덕질해요!"))
                add(DetailData(title = "모임 대상", content = "솜솜이를 사랑하는 찐팬"))
                add(DetailData(title = "모임 일정", content = "매일매시매분매초\n숨 멈추는 순간까지"))
                add(DetailData(title = "리더 소개", content = "솜솜이 1호팬"))

                detailAdapter.datas = datas
                detailAdapter.notifyDataSetChanged()
            }
        } else {
            datas.apply {
                add(DetailData(title = "모임 소개", content = classData.content))
                add(DetailData(title = "모임 대상", content = classData.member))
                add(DetailData(title = "모임 일정", content = classData.scheduleDetail))
                add(DetailData(title = "리더 소개", content = classData.leaderContent))

                detailAdapter.datas = datas
                detailAdapter.notifyDataSetChanged()
            }
        }

        binding.rvDetail.addItemDecoration(VerticalItemDecorator(20))
        binding.rvDetail.addItemDecoration(HorizontalItemDecorator(10))
    }

    private fun buttonSetting() {
        binding.btnApplication.setOnClickListener {
            val email = SharedPreferenceManager.getUserEmail(this)
            val name = SharedPreferenceManager.getUserName(this)

            val builder = AlertDialog.Builder(this@ClassDetailActivity)
            builder.setTitle("신청하시겠습니까?")

            val v1 = layoutInflater.inflate(R.layout.dialog_edit, null)
            builder.setView(v1)

            val edit: MultiAutoCompleteTextView? = v1.findViewById(R.id.edit_correction)
            edit!!.setHint("각오 혹은 신청 사유에 대해 작성해주세요.")

            val listener = DialogInterface.OnClickListener { p0, p1 ->
                val alert = p0 as AlertDialog
                val edit: MultiAutoCompleteTextView? =
                    alert.findViewById(R.id.edit_correction)


                val myRef = database.getReference("applications")
                val stNum = email.subSequence(0, 8).toString()
                val id = System.currentTimeMillis().toInt()
                val application = ApplicationData(id, stNum, classData.cid)
                myRef.child(stNum).child(id.toString()).setValue(application)

                val reason = edit!!.text.toString()

                GMailSender().sendEmail(
                    classData.leaderID,
                    "'${classData.name}'에 신청자가 있습니다.",
                    "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221114%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221114T051746Z&X-Amz-Expires=86400&X-Amz-Signature=e6face4b2f5831a7ab8051945fad5ac13309458b8231252b7740ae348e66c409&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${classData.name} 신청]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${classData.name}'에 <b>${stNum} ${name}</b>님께서 신청하셨습니다.\n\n\n<p style='background-color:#D3D3D3;'>${name} 님의 이메일 : ${email}\n\n신청 멘트 : ${reason}</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                )

                Toast.makeText(this, "신청되었습니다.", Toast.LENGTH_SHORT).show()
            }

            builder.setPositiveButton("신청", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }

        binding.btnApplicationCancel.setOnClickListener {
            val email = SharedPreferenceManager.getUserEmail(this)
            val name = SharedPreferenceManager.getUserName(this)

            val builder = AlertDialog.Builder(this@ClassDetailActivity)
            builder.setTitle("철회하시겠습니까?")
            builder.setMessage("철회하시면 리더에게 취소 메일이 보내집니다.")

            val v1 = layoutInflater.inflate(R.layout.dialog_edit, null)
            builder.setView(v1)

            val edit: MultiAutoCompleteTextView? = v1.findViewById(R.id.edit_correction)
            edit!!.setHint("취소 사유에 대해 작성해주세요.")

            val listener = DialogInterface.OnClickListener { p0, p1 ->
                val alert = p0 as AlertDialog
                val edit: MultiAutoCompleteTextView? =
                    alert.findViewById(R.id.edit_correction)

                val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
                val myRef = database.getReference("applications")
                myRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            for(applicationsSnapshot in snapshot.children) {
                                for(applicationSnapshot in applicationsSnapshot.children) {
                                    val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                                    val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                                    val stN = applicationSnapshot.child("stNum").getValue(String::class.java)

                                    if (classData.cid == cid && stNum == stN) {
                                        myRef.child(stN).child(aid.toString()).removeValue()
                                        break
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                val reason = edit!!.text.toString()

                GMailSender().sendEmail(
                    classData.leaderID,
                    "'${classData.name}'에 취소자가 있습니다.",
                    "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221114%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221114T051746Z&X-Amz-Expires=86400&X-Amz-Signature=e6face4b2f5831a7ab8051945fad5ac13309458b8231252b7740ae348e66c409&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${classData.name} 신청 취소]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${classData.name}'에 <b>${stNum} ${name}</b>님께서 취소하셨습니다.\n\n\n<p style='background-color:#D3D3D3;'>${name} 님의 이메일 : ${email}\n\n취소 사유 : ${reason}</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                )

                Toast.makeText(this, "철회되었습니다.", Toast.LENGTH_SHORT).show()

                finish()
//                val intent = Intent(this, ClassesActivity::class.java)
//                startActivity(intent)

//                if(where == "myApplication") {
//                    finishAffinity()
//                    val intent = Intent(this, MyListActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                    intent.putExtra("where", "application")
//                    startActivity(intent)
//                    finish()
//                } else {
//                    finish()
//                }
            }

            builder.setPositiveButton("철회", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }

        binding.btnClassAddNum.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("현재 모집 완료된 인원 설정")

            val v1 = layoutInflater.inflate(R.layout.dialog_current_num, null)
            builder.setView(v1)

            val numberPicker: NumberPicker? = v1.findViewById(R.id.number_picker)

            if (numberPicker != null) {
                numberPicker.minValue = 0
                numberPicker.maxValue = classData.totalNum
                numberPicker.value = classData.currentNum
            }

            val listener = DialogInterface.OnClickListener { p0, p1 ->

                val myRef = database.getReference("classes")
                myRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            for(classSnapshot in snapshot.children) {
                                val cid = classSnapshot.child("cid").getValue(Int::class.java)

                                if(classData.cid == cid) {
                                    if(numberPicker != null) {
                                        myRef.child(cid.toString()).child("currentNum").setValue(numberPicker!!.value)
                                        classData.currentNum = numberPicker.value
                                    }
                                    Toast.makeText(this@ClassDetailActivity, "인원이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                    initUserData()
                                    break
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }

        binding.btnClassCorrection.setOnClickListener {
            val intent = Intent(this, ClassCorrectionActivity::class.java)
            intent.putExtra("data", classData as Serializable)
            startActivity(intent)
        }

        binding.btnClassDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ClassDetailActivity)
            builder.setTitle("모임을 삭제하시겠습니까?")
            builder.setMessage("삭제하시면 모임 신청자들에게 삭제 메일이 보내집니다.")

            val v1 = layoutInflater.inflate(R.layout.dialog_edit, null)
            builder.setView(v1)

            val edit: MultiAutoCompleteTextView? = v1.findViewById(R.id.edit_correction)
            edit!!.setHint("삭제 사유에 대해 작성해주세요.")

            val listener = DialogInterface.OnClickListener { p0, p1 ->
                val alert = p0 as AlertDialog
                val edit: MultiAutoCompleteTextView? =
                    alert.findViewById(R.id.edit_correction)

                val reason = edit!!.text.toString()

                deleteMail(reason)

                Toast.makeText(this@ClassDetailActivity, "모임이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ClassDetailActivity, ClassesActivity::class.java)
                startActivity(intent)
                finish()
            }

            builder.setPositiveButton("신청", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        }

        binding.btnHomeDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnClassesDetail.setOnClickListener {
            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSearchDetail.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSettingDetail.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun myApplicationList() {
        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
        val myRef = database.getReference("applications")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(applicationSnapshot in snapshot.children) {
                        for (snapshot in applicationSnapshot.children) {
                            val cid = snapshot.child("cid").getValue(Int::class.java)
                            val stN = snapshot.child("stNum").getValue(String::class.java)

                            if (stNum == stN!!) {
                                applicationList.add(cid!!)
                            }
                        }
                    }
                    isShowButton()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun deleteMail(reason: String) {
        val myRef = database.getReference("applications")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(applicationsSnapshot in snapshot.children) {
                        for(applicationSnapshot in applicationsSnapshot.children) {
                            val aid = applicationSnapshot.child("aid").getValue(Int::class.java)
                            val cid = applicationSnapshot.child("cid").getValue(Int::class.java)
                            val stN = applicationSnapshot.child("stNum").getValue(String::class.java)

                            if (classData.cid == cid) {
                                myRef.child(stN!!).child(aid.toString()).removeValue()

                                val email = stN + "@dongduk.ac.kr"

                                GMailSender().sendEmail(
                                    email,
                                    "'${classData.name}' 모임이 삭제되었습니다.",
                                    "<img src='https://s3.us-west-2.amazonaws.com/secure.notion-static.com/2d69e720-0c5b-41c9-9893-54b5ab975a96/logo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221114%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221114T051746Z&X-Amz-Expires=86400&X-Amz-Signature=e6face4b2f5831a7ab8051945fad5ac13309458b8231252b7740ae348e66c409&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22logo.png%22&x-id=GetObject' alt='With Som 로고' width='20%' height='20%'><h3><b>[${classData.name} 모임 삭제]</b></h3>\n\n안녕하세요. With Som입니다.\n\n'${classData.name}' 모임이 <b>삭제</b>되었습니다.\n\n\n<p style='background-color:#D3D3D3;'>삭제 사유 : ${reason}</p>\n\n<hr/><small>With Som 개발자 dongduk.withsom@gmail.com</small>"
                                )
                            }
                        }
                    }
                    deleteClass()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun deleteClass() {
        val myRef2 = database.getReference("classes")
        myRef2.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(classSnapshot in snapshot.children) {
                        val cid = classSnapshot.child("cid").getValue(Int::class.java)

                        if(classData.cid == cid) {
                            myRef2.child(cid.toString()).removeValue()
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
