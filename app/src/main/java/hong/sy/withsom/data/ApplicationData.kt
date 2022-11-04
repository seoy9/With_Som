package hong.sy.withsom.data

import java.io.Serializable

data class ApplicationData(
    var aid: Int,
    var stNum: String,
    var cid: Int
) : Serializable
