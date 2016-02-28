package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.Blog
import y2k.joyreactor.services.requests.BlogListRequest

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogService(
        private val service: BlogListRequest) {

    fun getBlogList(url: String): Observable<List<Blog>> {
        return service.requestAsync(url)
    }
}