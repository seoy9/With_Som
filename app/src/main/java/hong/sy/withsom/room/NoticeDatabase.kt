package hong.sy.withsom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(NoticeEntity::class), version = 1)
abstract class NoticeDatabase: RoomDatabase() {
    abstract fun getNoticeDao() : NoticeDao

    companion object {
        val databaseName = "db_notice"
        var noticeDatabase: NoticeDatabase? = null

        fun getInstance(context: Context) :NoticeDatabase? {
            if(noticeDatabase == null) {
                noticeDatabase = Room.databaseBuilder(context, NoticeDatabase::class.java, databaseName).build()
            }
            return noticeDatabase
        }
    }
}