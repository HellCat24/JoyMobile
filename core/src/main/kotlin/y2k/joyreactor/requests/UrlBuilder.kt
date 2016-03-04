package y2k.joyreactor.requests

import y2k.joyreactor.enteties.Tag
import y2k.joyreactor.requests.const.URLConst

/**
 * Created by y2k on 11/8/15.
 */
internal class UrlBuilder {

    fun build(tag: Tag, pageId: String?, type: String): String {
        var url = URLConst.BASE_URL
        if(type.length > 0)
            url += type
        if (tag.isFavorite)
            url += "user/" + tag.username
        else if (tag.serverId != null)
            url += "tag/" + tag.serverId
        if (pageId != null) url += "/" + pageId
        return url
    }
}