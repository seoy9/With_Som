package hong.sy.withsom.data

import java.io.Serializable

data class UserData(
    var id: String,
    val email: String,
    var pw: String,
    val name: String,
    val stNum: String,
    var depart: String,
    var profile: Int
) : Serializable
