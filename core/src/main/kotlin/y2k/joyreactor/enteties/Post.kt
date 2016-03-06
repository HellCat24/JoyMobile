package y2k.joyreactor.enteties

import y2k.joyreactor.repository.DataSet
import java.io.Serializable

/**
 * Created by y2k on 9/27/15.
 */
class Post(
        val title: String,
        val image: Image?,
        val images: List<Image>,
        val userImage: String,
        val userName: String,
        val serverId: String,
        val commentCount: Int,
        var rating: Float) : Serializable, Comparable<Post>, DataSet.Dto {

    override var id: Long = 0
    var isLiked: Boolean = rating > 0


    override fun compareTo(other: Post): Int {
        return (id - other.id).toInt()
    }

    override fun equals(other: Any?): Boolean {
        return this.serverId == (other as Post).serverId
    }
}