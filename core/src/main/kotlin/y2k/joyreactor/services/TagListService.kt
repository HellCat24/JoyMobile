package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.common.concatAndRepeat
import y2k.joyreactor.enteties.Tag
import y2k.joyreactor.repository.DataContext
import y2k.joyreactor.requests.AddTagRequest
import y2k.joyreactor.requests.UserNameRequest
import y2k.joyreactor.services.synchronizers.MyTagFetcher

/**
 * Created by y2k on 11/24/15.
 */
class TagListService(
    private val dataContext: DataContext.Factory,
    private val userNameRequest: UserNameRequest,
    private val synchronizer: MyTagFetcher) {

    fun getMyTags(): Observable<List<Tag>> {
        return dataContext
            .applyUse {
                Tags.filter { it.isVisible }.sortedBy { it.title?.toLowerCase() }
            }
            .concatAndRepeat(synchronizer.synchronize())
    }

    fun addTag(tag: String): Observable<Unit> {
        return AddTagRequest(tag).request()
    }

    fun getTagForFavorite(): Observable<Tag> {
        return userNameRequest
            .request()
            .map { Tag.makeFavorite(it!!) }
    }
}