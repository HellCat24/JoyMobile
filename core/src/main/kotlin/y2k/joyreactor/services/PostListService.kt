package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.common.peek
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.enteties.Tag
import y2k.joyreactor.repository.DataContext
import y2k.joyreactor.requests.FavoriteRequest
import y2k.joyreactor.requests.LikeDislikeRequest
import y2k.joyreactor.requests.PostsListRequest
import y2k.joyreactor.services.synchronizers.PostMerger

/**
 * Created by y2k on 11/24/15.
 */
class PostListService(private val dataContext: DataContext.Factory,
                 private val postsRequest: PostsListRequest,
                 private val likeDislikeRequest: LikeDislikeRequest,
                 private val requestFactory: FavoriteRequest,
                 private val merger: PostMerger) {

    private lateinit var tag: Tag
    private lateinit var type: String
    private @Volatile lateinit var lastPage: PostsListRequest.Data

    val divider: Int?
        get() = merger.divider

    fun setTag(tag: Tag) {
        this.tag = tag
    }

    fun setType(type: String?) {
        if (type != null)
            this.type = type
        else this.type = ""
    }

    fun preloadNewPosts(): Observable<Boolean> {
        return requestAsync().flatMap { merger.isUnsafeUpdate(tag, it.posts) }
    }

    fun applyNew(): Observable<List<Post>> {
        return merger
                .mergeFirstPage(tag, lastPage.posts)
                .flatMap { getFromRepository() }
    }

    fun loadNextPage(): Observable<List<Post>> {
        return requestAsync(lastPage.nextPage).map { it -> it.posts }
    }

    fun reloadFirstPage(): Observable<List<Post>> {
        return requestAsync()
                .flatMap { data ->
                    dataContext
                            .use { entities ->
                                entities.TagPosts
                                        .filter { it.tagId == tag.id }
                                        .forEach { entities.TagPosts.remove(it) }
                                entities.saveChanges()
                            }
                            .map { data }
                }
                .flatMap { merger.mergeFirstPage(tag, it.posts) }
                .flatMap { getFromRepository() }
    }

    fun requestAsync(page: String? = null): Observable<PostsListRequest.Data> {
        return postsRequest
                .requestAsync(tag, page, type)
                .peek { lastPage = it }
    }

    fun queryAsync(): Observable<List<Post>> {
        return getFromRepository()
    }

    private fun getFromRepository(): Observable<List<Post>> {
        return dataContext.use { entities ->
            entities.TagPosts
                    .filter { it.tagId == tag.id }
                    .map { link -> entities.Posts.first { it.id == link.postId } }
        }
    }

    fun like(postId: String): Observable<Float> {
        return likeDislikeRequest
                .like(postId)
                .map { it -> it.toFloat() }
    }

    fun dislike(postId: String): Observable<Float> {
        return likeDislikeRequest
                .disLike(postId)
                .map { it -> it.toFloat() }
    }

    fun addToFavorite(postId: String): Observable<Boolean> {
        return requestFactory.addToFavorite(postId)
    }

    fun deleteFromFavorite(postId: String): Observable<Boolean> {
        return requestFactory.deleteFromFavorite(postId)
    }
}