package app.sargis.khlopuzyan.cardreader.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import app.sargis.khlopuzyan.cardreader.R
import app.sargis.khlopuzyan.cardreader.databinding.FragmentMainBinding
import dagger.android.support.DaggerFragment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject


class MainFragment : DaggerFragment() {

    companion object {

        const val REQUEST_CODE_PERMISSION = 100
        const val REQUEST_CAPTURE_IMAGE = 101
        const val REQUEST_CAPTURE_GALLERY = 102

        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainBinding

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        setupObservers()
        checkPermissions()
    }

    private fun setupObservers() {
        viewModel.openCameraLiveData.observe(this) {
            takePictureFromCamera()
        }

        viewModel.openGalleryLiveData.observe(this) {
            pickPhotoFromGallery()
        }
    }

    private fun checkPermissions() {


        activity?.let {

            //check permission is granted or no
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (it.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || it.checkSelfPermission(
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), REQUEST_CODE_PERMISSION
                )
            }
        }
    }

    private fun takePictureFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )

        imageUri =
            activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE)
    }

    private fun pickPhotoFromGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, REQUEST_CAPTURE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {

            when (requestCode) {

                REQUEST_CAPTURE_IMAGE -> {

                    val bitmap: Bitmap

                    try {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
                        binding.imageView.setImageBitmap(bitmap)
                        viewModel.bitmap = bitmap

                    } catch (e: FileNotFoundException) {
                    } catch (e: IOException) {
                    }
                }

                REQUEST_CAPTURE_GALLERY -> {

                    data?.data?.let {

                        binding.imageView.setImageURI(it)

                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(File(it.toString()))
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            MediaStore.Images.Media.getBitmap(
                                CardReaderApp.getContext().contentResolver, it
                            )
                        }

                        viewModel.bitmap = bitmap
                    }
                }
            }
        }
    }

}