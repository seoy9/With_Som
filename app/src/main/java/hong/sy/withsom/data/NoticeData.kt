package hong.sy.withsom.data

import java.io.Serializable

data class NoticeData(
    val nid: Int,
    val title : String,
    val date : String,
    val content : String
) : Serializable
