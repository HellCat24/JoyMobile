package joy.reactor.data.enteties

import java.io.Serializable

/**
 * Created by y2k on 07/12/15.
 */
class Attachment(val image: Image) : Serializable {

    var postId: Int = 0
}