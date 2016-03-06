package y2k.joyreactor.requests.const

/**
 * Created by Oleg on 04.03.2016.
 */
class URLConst {

    companion object {
        val BASE_URL = "http://joyreactor.cc"

        val MESSAGE_URL = BASE_URL + "/private/list"
        val USERNAME_URL = BASE_URL + "/user/"
        val CREATE_MESSAGE_URL = BASE_URL + "/private/create"


        val TAG_REQUEST = BASE_URL + "/tag/"
        val BLOG_REQUEST = ""
        val ADD_REQUEST = ""
        val LIKE_REQUEST = BASE_URL + "/post_vote/add/"
        val DISLIKE_REQUEST = ""
        val ADD_FAVORITE_REQUEST = BASE_URL + "/favorite/create/"
        val DELETE_FAVORITE_REQUEST = BASE_URL + "/favorite/delete/"
        val POST_REQUEST = ""




    }

}