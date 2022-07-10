package com.rival.my_packet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.ActivityCreateBinding
import com.rival.my_packet.model.respon
import com.rival.my_packet.ui.paket.SatpamPaketFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Create_Activity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding

    private lateinit var currentPhotoPath: String
    lateinit var statusList: Spinner

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Create"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.imageView.setOnClickListener { startCameraX() }
//        binding.imageView.setOnClickListener { takePicture() }
        binding.btnKirim.setOnClickListener { kirim() }

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            // FIle -> Bitmap
            val bitmap = BitmapFactory.decodeFile(myFile.absolutePath)
            binding.imageView.setImageBitmap(bitmap)

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

        }
    }


    private fun kirim() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val namaPenerima =
                binding.namaPenerima.text.toString().toRequestBody("text/plain".toMediaType())
            val ekspedisi =
                binding.ekspedisi.text.toString().toRequestBody("text/plain".toMediaType())
            val status =
                binding.statusPaket.selectedItem.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "img",
                file.name,
                requestImageFile
            )


            val service = ApiConfig.instanceRetrofit.uploadImage(
                imageMultipart,
                namaPenerima,
                ekspedisi,
                status
            )

            service.enqueue(object : Callback<respon> {
                override fun onResponse(
                    call: Call<respon>,
                    response: Response<respon>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.status == 1) {
                            Toast.makeText(
                                this@Create_Activity,
                                responseBody.pesan,
                                Toast.LENGTH_SHORT
                            ).show()
                            //back to landing page
                            val intent = Intent(this@Create_Activity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@Create_Activity, response.message(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<respon>, t: Throwable) {
                    Toast.makeText(
                        this@Create_Activity,
                        "Gagal instance Retrofit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
               startActivity(Intent(this, MainActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}