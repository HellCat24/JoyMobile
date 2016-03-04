package y2k.joyreactor.presenters

import rx.Observable
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.enteties.Tag
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.*

/**
 * Created by y2k on 9/26/15.
 */
class PostListPresenter(
        private val view: PostListPresenter.View,
        private val service: TagService,
        private val userService: ProfileService,
        private val lifeCycleService: LifeCycleService,
        private val likeDislikeService: LikeDislikeService,
        private val favoriteService: FavoriteService) {

    init {
        lifeCycleService.add(BroadcastService.TagSelected::class) { currentTagChanged(it.tag) }
        currentTagChanged(tag())
    }

    private fun tag(): Tag {
        var tag = Tag.makeFeatured()
        var serverId = view.getCurrentTag()
        if (serverId != null) {
            tag = Tag(serverId, "", false, null)
        }
        val username = view.getCurrentUserName()
        if (username != null) {
            tag = Tag.makeFavorite(username)
        }
        return tag
    }

    fun currentTagChanged(newTag: Tag) {
        service.setTag(newTag)
        service.setType(view.getPostType())
        service.requestAsync().subscribeOnMain { data ->
            view.setHasNewPosts(false)
            view.addNewPosts(data.posts)
        }

        userService
                .isAuthorized()
                .subscribeOnMain { if (it) view.setLikesDislikesEnable() }
    }

    fun applyNew() {
        service.applyNew()
                .subscribeOnMain { posts ->
                    view.setHasNewPosts(false)
                    view.reloadPosts(posts, service.divider)
                }
    }

    fun loadMore() {
        service.loadNextPage()
                .subscribeOnMain { posts ->
                    view.addNewPosts(posts)
                    view.setBusy(false)
                }
    }

    fun reloadFirstPage() {
        view.setBusy(true)
        service.reloadFirstPage()
                .subscribeOnMain { posts ->
                    view.reloadPosts(posts, posts.size)
                    view.setBusy(false)
                }
    }

    private fun getFromRepository(): Observable<List<Post>> {
        return service.queryAsync()
    }

    fun postClicked(post: Post) {
        Navigation.instance.openPost(post.serverId!!)
    }

    fun like(post: Post) {
        likeDislikeService.like(post.serverId)
                .subscribeOnMain { it ->
                    post.isLiked = true
                    post.rating = it
                    view.updatePostRating(post)
                }
    }

    fun disLike(post: Post) {
        likeDislikeService.dislike(post.serverId)
                .subscribeOnMain { it ->
                    post.isLiked = true
                    post.rating = it
                    view.updatePostRating(post)
                }
    }

    fun addToFavorite(post: Post): Unit {
        favoriteService.addToFavorite(post.serverId).subscribeOnMain {
            
        }
    }

    fun deleteFromFavorite(post: Post): Unit {
        favoriteService.deleteFromFavorite(post.serverId).subscribeOnMain { }
    }

    fun showUserPosts(username: String) {
        Navigation.instance.openUserPosts(username)
    }

    fun playClicked(post: Post) {
        if (post.image!!.isYouTube) {
            Navigation.instance.openYouTube(post.image.getYouTubeLink)
        } else Navigation.instance.openImageView(post)
    }

    interface View {

        fun setBusy(isBusy: Boolean)

        fun addNewPosts(posts: List<Post>)

        fun reloadPosts(posts: List<Post>, divider: Int?)

        fun setHasNewPosts(hasNewPosts: Boolean)

        fun getPostType(): String?

        fun getCurrentUserName(): String?

        fun getCurrentTag(): String?

        fun updatePostRating(post: Post)

        fun updatePostFavoriteStatus(post: Post)

        fun setLikesDislikesEnable()

    }
}