package hong.sy.withsom.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun getAll() : List<UserEntity>

    @Insert
    fun insertUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM USEREntity WHERE id = :id")
    fun selectIDUser(id: Int): UserEntity

    @Query("SELECT * FROM USEREntity WHERE email = :email")
    fun selectEmailUser(email: String): UserEntity

    @Query("UPDATE USERENTITY SET pw = :pw, depart = :depart, profile = :profile WHERE id = :id")
    fun updateUser(id: String, pw: String, depart: String, profile: String)
}