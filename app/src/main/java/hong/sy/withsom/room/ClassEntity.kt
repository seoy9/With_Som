package hong.sy.withsom.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "location") var location: String,
    @ColumnInfo(name = "currentNum") var currentNum: Int,
    @ColumnInfo(name = "totalNum") var totalNum: Int,
    @ColumnInfo(name = "member") var member: String,
    @ColumnInfo(name = "schedule") var schedule: String,
    @ColumnInfo(name = "scheduleDetail") var scheduleDetail: String,
    @ColumnInfo(name = "leaderID") val leaderID: String,
    @ColumnInfo(name = "leaderContent") var leaderContent: String,
) : Serializable
