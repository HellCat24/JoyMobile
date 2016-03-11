package joy.reactor.data.images

import rx.Observable
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.Executors

/**
 * Created by y2k on 9/27/15.
 */
class DiskCache {

    init {
        cacheDirectory.mkdirs()
    }

    operator fun get(url: String): Observable<File?> {
        return Observable.create<File?> { subscriber ->
            val file = urlToFile(url)
            subscriber.onNext(if (file.exists()) file else null)
            subscriber.onCompleted()
        }.subscribeOn(Schedulers.from(DISK_EXECUTOR))
    }

    fun put(newImageFile: File, url: String): Observable<*> {
        return Observable.create<Any> { subscriber ->
            newImageFile.renameTo(urlToFile(url))
            subscriber.onNext(null)
            subscriber.onCompleted()
        }.subscribeOn(Schedulers.from(DISK_EXECUTOR))
    }

    private fun urlToFile(url: String): File {
        return File(cacheDirectory, "" + url.hashCode())
    }


    companion object {

        private val DISK_EXECUTOR = Executors.newSingleThreadExecutor()

        lateinit var cacheDirectory: File

        fun init(file: File) {
            cacheDirectory = File(file, "images");
        }

        val instance: DiskCache
            get() = DiskCache()
    }
}