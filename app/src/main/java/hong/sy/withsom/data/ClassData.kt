package hong.sy.withsom.data

import java.io.Serializable

data class ClassData(
    var id: String? = null,
    var name: String,
    var type: String,
    var content: String,
    var location: String,
    var currentNum: Int,
    var totalNum: Int,
    var member: String,
    var schedule: String,
    var scheduleDetail: String,
    val leaderID: String,
    var leaderContent: String
) : Serializable
