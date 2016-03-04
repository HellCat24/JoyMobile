package y2k.joyreactor.repository

import rx.Observable
import rx.schedulers.Schedulers
import y2k.joyreactor.common.ForegroundScheduler
import y2k.joyreactor.enteties.Message
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.enteties.Tag
import y2k.joyreactor.enteties.TagPost
import y2k.joyreactor.platform.Platform
import java.io.EOFException
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import java.util.concurrent.Executors

/**
 * Created by y2k on 12/22/15.
 */
class DataContext {

    private val tables = ArrayList<DataSet<*>>()

    val Posts = register<Post>("posts")

    val AllPosts = register<Post>("all_posts")

    val BestPosts = register<Post>("best_posts")

    val Tags = register<Tag>("tags")

    val TagPosts = register<TagPost>("tag_posts")

    val Messages = register<Message>("messages")

    private fun <T : DataSet.Dto> register(name: String): DataSet<T> {
        return DataSet<T>(name).apply { tables.add(this) }
    }

    fun saveChanges() {
        // TODO: при forEach падает
        for (it in tables) DataContext.Serializer.saveToDisk(it)
    }

    class Factory {

        fun <T> applyUse(callback: DataContext.() -> T): Observable<T> {
            return Observable
                    .fromCallable { innerMakeDataContext().callback(); }
                    .subscribeOn(Schedulers.from(DataContext.Factory.Companion.executor))
                    .observeOn(ForegroundScheduler.instance);
        }

        fun <T> use(callback: (DataContext) -> T): Observable<T> {
            return Observable
                    .fromCallable { callback(innerMakeDataContext()) }
                    .subscribeOn(Schedulers.from(DataContext.Factory.Companion.executor))
                    .observeOn(ForegroundScheduler.instance);
        }

        private fun innerMakeDataContext(): DataContext {
            val entities = DataContext()
            // TODO: при forEach падает
            for (it in entities.tables) DataContext.Serializer.loadFromDisk(it)
            return entities
        }

        companion object {

            private val executor = Executors.newSingleThreadExecutor()
        }
    }

    private object Serializer {

        fun <T : DataSet.Dto> loadFromDisk(dataSet: DataSet<T>) {
            DataContext.Serializer.getFile(dataSet)
                    .let { if (it.exists()) it else null }
                    ?.let { file ->
                        file.inputStream()
                                .let { ObjectInputStream(it) }
                                .use { stream ->
                                    while (true) {
                                        try {
                                            @Suppress("UNCHECKED_CAST")
                                            dataSet.add(stream.readObject() as T)
                                        } catch(e: EOFException) {
                                            break
                                        }
                                    }
                                }
                    }
        }

        fun saveToDisk(dataSet: DataSet<*>) {
            DataContext.Serializer.getFile(dataSet)
                    .outputStream()
                    .let { ObjectOutputStream(it) }
                    .use { stream -> dataSet.forEach { stream.writeObject(it) } }
        }

        private fun getFile(datasSet: DataSet<*>): File {
            return File(Platform.Companion.instance.currentDirectory, "${datasSet.name}.db")
        }
    }
}
