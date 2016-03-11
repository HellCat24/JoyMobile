package joy.reactor.data.images

import joy.reactor.data.common.ForegroundScheduler
import joy.reactor.data.enteties.Image
import rx.Observable
import rx.Subscription
import java.io.File
import java.util.*

/**
 * Created by y2k on 12/10/15.
 */
abstract class BaseImageRequest<T> {

    private var subscription: Subscription? = null

    private var image: Image? = null
    private var width: Int? = null
    private var height: Int? = null

    open fun setSize(width: Int, height: Int): BaseImageRequest<T> {
        this.width = width
        this.height = height
        return this
    }

    fun setUrl(image: Image?): BaseImageRequest<T> {
        this.image = image
        return this
    }

    fun to(target: Any, callback: (T?) -> Unit) {
        if (image == null) {
            sLinks.remove(target)
            callback(null)
            return
        }

        subscription = getFromCache()
                .flatMap({ image ->
                    if (image != null) Observable.just<T>(image)
                    else putToCache().flatMap { getFromCache() }
                })
                .observeOn(ForegroundScheduler.instance)
                .filter { sLinks[target] === subscription }
                .subscribe({
                    callback(it);
                    sLinks.remove(target)
                }, {
                    it.printStackTrace();
                    sLinks.remove(target)
                })

        callback(null)
        sLinks.put(target, subscription!!)
    }

    private fun getFromCache(): Observable<T?> {
        return sDiskCache[toURLString()].map({ it?.let { decode(it) } })
    }

    private fun putToCache(): Observable<Any> {
        val dir = DiskCache.cacheDirectory
        return MultiTryDownloader(dir, toURLString())
                .downloadAsync()
                .flatMap({ s -> sDiskCache.put(s, toURLString()) })
    }

    private fun toURLString(): String {
        return image!!.thumbnailUrl(width, height)
    }

    protected abstract fun decode(path: File): T

    companion object {

        private val sDiskCache = DiskCache()
        private val sLinks = HashMap<Any, Subscription>()
    }
}