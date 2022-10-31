package hong.sy.withsom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun getUserDao() : UserDao

    companion object {
        val databaseName = "db_user"
        var userDatabase: UserDatabase? = null

        fun getInstance(context: Context) : UserDatabase? {
            if(userDatabase == null) {
                userDatabase = Room.databaseBuilder(context, UserDatabase::class.java, databaseName).build()
            }
            return userDatabase
        }
    }
}