package hong.sy.withsom.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoticeDao {
    @Update
    fun updateNotice(notice: NoticeEntity)

    @Query("SELECT * FROM NOTICEENTITY")
    fun getAll() : List<NoticeEntity>

    @Query("SELECT * FROM NOTICEENTITY WHERE id = :id")
    fun selectNotice(id: Int) : NoticeEntity
}