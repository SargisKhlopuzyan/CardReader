package app.sargis.khlopuzyan.cardreader.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.sargis.khlopuzyan.cardreader.helper.SingleLiveEvent
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class MainViewModel : ViewModel() {

    var cardInfo = MutableLiveData<String>()

    val openCameraLiveData: SingleLiveEvent<View> = SingleLiveEvent()
    val openGalleryLiveData: SingleLiveEvent<View> = SingleLiveEvent()

    var bitmap: Bitmap? = null

    /**
     * Handles caching icon click
     * */
    fun onScanClick() {

        if (bitmap == null) {
            Toast.makeText(CardReaderApp.getContext(), "Image not found!!", Toast.LENGTH_LONG)
                .show()
            return
        }

        val textRecognizer = TextRecognizer.Builder(CardReaderApp.getContext()).build()

        if (!textRecognizer.isOperational) {
            Toast.makeText(CardReaderApp.getContext(), "Could not get the Text", Toast.LENGTH_LONG)
                .show()
        } else {

            val frame = Frame.Builder().setBitmap(bitmap).build()

            val items: SparseArray<TextBlock> = textRecognizer.detect(frame)

            val stringBuilder = StringBuilder()

            for (i in 0 until items.size()) {

                val item = items[i]
                stringBuilder.append(item.value)
                stringBuilder.append("\n")
            }

            cardInfo.value = stringBuilder.toString()

        }

    }

    /**
     * Handles caching icon click
     * */
    fun onOpenCameraClick(v: View) {
        openCameraLiveData.value = v
    }

    /**
     * Handles caching icon click
     * */
    fun onOpenGalleryClick(v: View) {
        openGalleryLiveData.value = v
    }

    fun loadBitmap(url: String): Bitmap? {

        var bm: Bitmap? = null
        var inputStream: InputStream? = null
        var bufferedInputStream: BufferedInputStream? = null

        try {

            val urlConnection: URLConnection = URL(url).openConnection()
            urlConnection.connect()
            inputStream = urlConnection.getInputStream()
            bufferedInputStream = BufferedInputStream(inputStream, 8192)
            bm = BitmapFactory.decodeStream(bufferedInputStream)

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
        return bm
    }

}
