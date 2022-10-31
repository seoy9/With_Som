package hong.sy.withsom.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "pw") val pw: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "stNum") val stNum: String,
    @ColumnInfo(name = "depart") val depart: String,
    @ColumnInfo(name = "profile") var profile: Int
) : Serializable
