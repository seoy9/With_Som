package hong.sy.withsom.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClassDao {
    @Query("SELECT * FROM ClassEntity")
    fun getAll() : List<ClassEntity>

    @Insert
    fun insertClass(clas: ClassEntity)

    @Delete
    fun deleteClass(clas: ClassEntity)

//    @Update
//    fun updateClass(clas : ClassEntity)

    @Query("SELECT * FROM ClassEntity WHERE id = :id")
    fun selectClass(id: Int) : ClassEntity

    @Query("SELECT * FROM CLASSENTITY WHERE name = :search OR type = :search")
    fun searchClass(search: String) : List<ClassEntity>

    @Query("UPDATE CLASSENTITY SET name = :name, type = :type, content = :content, location = :location, totalNum = :totalNum, member = :member, schedule = :schedule, scheduleDetail = :scheduleDetail, leaderContent = :leaderContent WHERE id = :id")
    fun updateClass(id: Int, name: String, type: String, content: String, location: String, totalNum: Int, member: String, schedule: String, scheduleDetail: String, leaderContent: String)
}