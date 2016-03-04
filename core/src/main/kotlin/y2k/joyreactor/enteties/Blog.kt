package y2k.joyreactor.enteties

import y2k.joyreactor.repository.DataSet
import java.io.Serializable
import java.util.*

/**
 * Created by Oleg on 28.02.2016.
 */
class Blog(
        val title: String,
        val image: Image?,
        val rating: String,
        val subscriberCount: String,
        val tagUrl: String) : Serializable, Comparable<Blog>, DataSet.Dto {

    override var id: Long = 0

    override fun compareTo(other: Blog): Int {
        return (id - other.id).toInt()
    }
}