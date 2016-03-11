package joy.reactor.domain.services

import joy.reactor.data.enteties.Blog
import joy.reactor.data.requests.BlogListRequest
import rx.Observable

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogService(private val service: BlogListRequest) {

    fun getBlogList(url: String): Observable<List<Blog>> {
        return service.requestAsync(url)
    }
}