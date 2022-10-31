package hong.sy.withsom.data

data class UserData(
    val id: String,
    val email: String,
    var pw: String,
    val name: String,
    val stNum: String,
    var depart: String,
    var profile: Int
)
