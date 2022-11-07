package hong.sy.withsom

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hong.sy.withsom.data.ApplicationData
import hong.sy.withsom.data.ClassData
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.databinding.ActivityClassDetailBinding
import hong.sy.withsom.login.SharedPreferenceManager
import hong.sy.withsom.recyclerView.DetailRecyclerViewAdapter
import hong.sy.withsom.recyclerView.HorizontalItemDecorator
import hong.sy.withsom.recyclerView.VerticalItemDecorator
import java.util.*

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClassDetailBinding

    lateinit var detailAdapter: DetailRecyclerViewAdapter
    val datas = mutableListOf<DetailData>()

    lateinit var classData: ClassData
    lateinit var where: String

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classData = intent.getSerializableExtra("data") as ClassData
        where = intent.getStringExtra("where").toString()

        isShowButton()

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

        initRecycler()

        buttonSetting()
    }

    private fun isShowButton() {
        if(where == "myApplication") {
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnApplicationCancel.visibility = View.VISIBLE
        } else if(where == "myClass" || where == "createClass") {
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnClassAddNum.visibility = View.VISIBLE
            binding.btnClassCorrection.visibility = View.VISIBLE
            binding.btnClassDelete.visibility = View.VISIBLE
        }

        val id = SharedPreferenceManager.getUserEmail(this)

        if(id == classData.leaderID) {
            binding.btnApplication.visibility = View.INVISIBLE
            binding.btnClassAddNum.visibility = View.VISIBLE
            binding.btnClassCorrection.visibility = View.VISIBLE
            binding.btnClassDelete.visibility = View.VISIBLE
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
            val builder = AlertDialog.Builder(this)
            builder.setTitle("신청하시겠습니까?")
                .setMessage("신청하시면 리더에게 신청 메일이 보내집니다.")
                .setPositiveButton("신청",
                    DialogInterface.OnClickListener{ dialog, id ->
                        val myRef = database.getReference("applications")
                        val stNum = SharedPreferenceManager.getUserEmail(this).subSequence(0, 8).toString()
                        val id = System.currentTimeMillis().toInt()
                        val application = ApplicationData(id, stNum, classData.cid)
                        myRef.child(stNum).child(id.toString()).setValue(application)

                        Toast.makeText(this, "신청되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", null)
            builder.show()
        }

        binding.btnApplicationCancel.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("철회하시겠습니까?")
                .setMessage("철회하시면 리더에게 취소 메일이 보내집니다.")
                .setPositiveButton("철회",
                    DialogInterface.OnClickListener{ dialog, id ->
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

                        Toast.makeText(this, "철회되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    })
                .setNegativeButton("취소", null)
            builder.show()
        }

        binding.btnClassAddNum.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("현재 인원을 증가시키겠습니까?")
                .setPositiveButton("증가",
                    DialogInterface.OnClickListener{ dialog, id ->
                        val myRef = database.getReference("classes")
                        myRef.addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()) {
                                    for(classSnapshot in snapshot.children) {
                                        val cid = classSnapshot.child("cid").getValue(Int::class.java)
                                        var currentNum = classSnapshot.child("currentNum").getValue(Int::class.java)

                                        if(classData.cid == cid) {
                                            currentNum = currentNum!! + 1
                                            myRef.child(cid.toString()).child("currentNum").setValue(currentNum)
                                            break
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                        Toast.makeText(this@ClassDetailActivity, "현재 인원이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", null)
            builder.show()
        }

        binding.btnClassCorrection.setOnClickListener {

        }

        binding.btnClassDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("모임을 삭제하시겠습니까?")
                .setMessage("삭제하시면 모임 신청자들에게 삭제 메일이 보내집니다.")
                .setPositiveButton("삭제",
                    DialogInterface.OnClickListener{ dialog, id ->
                        val myRef = database.getReference("classes")
                        myRef.addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()) {
                                    for(classSnapshot in snapshot.children) {
                                        val cid = classSnapshot.child("cid").getValue(Int::class.java)

                                        if(classData.cid == cid) {
                                            myRef.child(cid.toString()).removeValue()
                                            break
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                        Toast.makeText(this@ClassDetailActivity, "모임이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("취소", null)
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

    private fun refresh() {
        val intent = this.intent
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}
