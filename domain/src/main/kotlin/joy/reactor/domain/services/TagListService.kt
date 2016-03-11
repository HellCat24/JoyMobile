package joy.reactor.domain.services

import joy.reactor.data.common.concatAndRepeat
import joy.reactor.data.enteties.Tag
import joy.reactor.data.repository.DataContext
import joy.reactor.data.requests.AddTagRequest
import joy.reactor.data.requests.UserNameRequest
import joy.reactor.domain.services.synchronizers.MyTagFetcher
import rx.Observable

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