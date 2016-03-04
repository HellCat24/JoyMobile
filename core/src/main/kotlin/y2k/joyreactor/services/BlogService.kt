package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.enteties.Blog
import y2k.joyreactor.requests.BlogListRequest

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogService(private val service: BlogListRequest) {

    fun getBlogList(url: String): Observable<List<Blog>> {
        return service.requestAsync(url)
    }
}