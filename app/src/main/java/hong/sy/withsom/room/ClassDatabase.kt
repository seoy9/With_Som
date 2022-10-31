package hong.sy.withsom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ClassEntity::class), version = 1)
abstract class ClassDatabase: RoomDatabase() {
    abstract fun getClassDao() : ClassDao

    companion object {
        val databaseName = "db_class"
        var classDatabase: ClassDatabase? = null

        fun getInstance(context: Context) :ClassDatabase? {
            if(classDatabase == null) {
                classDatabase = Room.databaseBuilder(context, ClassDatabase::class.java, databaseName).build()
            }
            return classDatabase
        }
    }
}