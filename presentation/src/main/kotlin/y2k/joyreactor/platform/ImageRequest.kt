package y2k.joyreactor.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import joy.reactor.data.images.BaseImageRequest
import java.io.File

/**
 * Created by y2k on 10/12/15.
 */
class ImageRequest : BaseImageRequest<Bitmap>() {

    override fun decode(path: File): Bitmap = BitmapFactory.decodeFile(path.absolutePath)
}