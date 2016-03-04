package y2k.joyreactor.repository

import y2k.joyreactor.platform.Platform
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * Created by y2k on 11/23/15.
 */
internal class IconStorage private constructor(names: String, icons: String) {

    private val names: IntArray
    private val icons: IntArray

    init {
        this.names = loadIndexes(names)
        this.icons = loadIndexes(icons)
    }

    private fun loadIndexes(name: String): IntArray {
        val tmp = Platform.instance.loadFromBundle(name, "dat")
        val intBuffer = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer()

        val intArray = IntArray(intBuffer.remaining())
        intBuffer.get(intArray)
        return intArray
    }

    fun getImageId(name: String): Int? {
        val position = Arrays.binarySearch(names, name.toLowerCase().hashCode())
        return if (position < 0) null else icons[position]
    }

    companion object {

        operator fun get(cached: IconStorage?, names: String, icons: String): IconStorage {
            synchronized (IconStorage::class.java) {
                return cached ?: IconStorage(names, icons)
            }
        }
    }
}