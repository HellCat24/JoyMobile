package y2k.joyreactor.presenters

import rx.Observable
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.PostService
import y2k.joyreactor.requests.factory.OriginalImageRequestFactory

import java.io.File

/**
 * Created by y2k on 22/10/15.
 */
class VideoPresenter(view: VideoPresenter.View, service: PostService) {

    var view = view;
    var service = service;

    init {
        view.setBusy(true)
        loadVideo()
    }

    private fun loadVideo() {
        service.getFromCache(Navigation.instance.argumentPostId)
                .map { it.image!!.fullUrl("mp4") }
                .flatMap { OriginalImageRequestFactory().request(it) }
                .subscribeOnMain({ videoFile ->
                    view.showVideo(videoFile)
                    view.setBusy(false)
                }, {
                    it.printStackTrace()
                    view.setBusy(false)
                })
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