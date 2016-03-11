package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Post
import joy.reactor.data.requests.factory.OriginalImageRequestFactory
import joy.reactor.domain.services.PostService
import rx.Observable
import java.io.File

/**
 * Created by y2k on 22/10/15.
 */
class VideoPresenter(view: VideoPresenter.View, service: PostService) {

    var view = view;
    var service = service;

    init {
        view.setBusy(true)
    }

    fun loadVideo(p: Post?) {
        Observable.just(p)
                .map { it?.image!!.fullUrl("mp4") }
                .flatMap { OriginalImageRequestFactory().request(it) }
                .subscribeOnMain({ videoFile ->
                    view.showVideo(videoFile)
                    view.setBusy(false)
                }, {
                    it.printStackTrace()
                    view.setBusy(false)
                })
    }

    interface View {

        fun showVideo(videoFile: File)

        fun setBusy(isBusy: Boolean)
    }
}