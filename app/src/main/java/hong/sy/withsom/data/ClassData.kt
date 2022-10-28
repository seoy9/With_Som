package hong.sy.withsom.data

import java.io.Serializable

data class ClassData(
    val imgLeader : Int,
    val leader : String,
    val title : String,
    val type : String,
    var content : String,
    var num : Int,
    var schedule : String,
    var location : String
) : Serializable
