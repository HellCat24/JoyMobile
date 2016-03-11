package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Post
import joy.reactor.data.enteties.Tag
import joy.reactor.domain.services.PostListService
import joy.reactor.domain.services.ProfileService
import joy.reactor.domain.services.synchronizers.PostMerger
import y2k.joyreactor.platform.Navigation
import java.util.*

/**
 * Created by y2k on 9/26/15.
 */
class PostListPresenter(
        private val view: PostListPresenter.View,
        private val service: PostListService,
        private val userService: ProfileService,
        private val merger: PostMerger) {

    private lateinit var currentPostList: MutableList<Post>

    init {
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
        service.requestAsync()
                .subscribeOnMain { data ->
                    currentPostList = data.posts.toMutableList()
                    view.addNewPosts(data.posts)
                }
        userService
                .isAuthorized()
                .subscribeOnMain { if (it) view.setLikesDislikesEnable() }
    }

    fun loadMore() {
        service.loadNextPage()
                .map { data -> merger.mergePosts(currentPostList, data as ArrayList<Post>) }
                .subscribeOnMain { posts ->

                    (currentPostList).addAll(posts)

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

    fun postClicked(post: Post) {
        Navigation.instance.openPost(post.serverId!!)
    }

    fun like(post: Post) {
        service.like(post.serverId)
                .subscribeOnMain { it ->
                    post.isLiked = true
                    post.rating = it
                    view.updatePostRating(post)
                }
    }

    fun disLike(post: Post) {
        service.dislike(post.serverId)
                .subscribeOnMain { it ->
                    post.isLiked = true
                    post.rating = it
                    view.updatePostRating(post)
                }
    }

    fun addToFavorite(post: Post): Unit {
        service.addToFavorite(post.serverId).subscribeOnMain {

        }
    }

    fun showLongPost(post: Post): Unit {
        Navigation.instance.openLongPost(post)

    }

    fun deleteFromFavorite(post: Post): Unit {
        service.deleteFromFavorite(post.serverId).subscribeOnMain { }
    }

    fun showUserPosts(username: String) {
        Navigation.instance.openUserPosts(username)
    }

    fun playClicked(post: Post) {
        if (post.image!!.isYouTube) {
            Navigation.instance.openYouTube(post.image!!.getYouTubeLink)
        } else if (post.image!!.isCoub || post.image!!.isGif) {
            Navigation.instance.openVideo(post)
        } else Navigation.instance.openImageView(post)
    }

    interface View {

        fun setBusy(isBusy: Boolean)

        fun addNewPosts(posts: List<Post>)

        fun reloadPosts(posts: List<Post>, divider: Int?)

        fun getPostType(): String?

        fun getCurrentUserName(): String?

        fun getCurrentTag(): String?

        fun updatePostRating(post: Post)

        fun updatePostFavoriteStatus(post: Post)

        fun setLikesDislikesEnable()

    }
}