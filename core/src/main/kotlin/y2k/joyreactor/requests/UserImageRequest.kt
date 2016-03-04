package y2k.joyreactor.requests

import y2k.joyreactor.repository.IconStorage

/**
 * Created by y2k on 01/10/15.
 */
class UserImageRequest {

    fun execute(name: String): String? {
        UserImageRequest.Companion.sStorage = IconStorage.get(UserImageRequest.Companion.sStorage, "user.names", "user.icons")

        val id = Companion.sStorage!!.getImageId(name)
        return if (id == null) null else "http://img0.joyreactor.cc/pics/avatar/user/" + id
    }

    companion object {

        private var sStorage: IconStorage? = null
    }
}