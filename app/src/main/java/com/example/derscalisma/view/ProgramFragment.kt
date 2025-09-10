package com.example.derscalisma.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.derscalisma.databinding.FragmentProgramBinding
import com.example.derscalisma.model.Program
import com.example.derscalisma.roomdb.ProgramDAO
import com.example.derscalisma.roomdb.ProgramDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.observers.DeferredScalarDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream


class ProgramFragment : Fragment() {
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private var secilenProgram :Program? = null
    // izinler için
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var secilenGorsel: Uri? = null // yer belitir
    private var secilenBitmap: Bitmap? = null// kayıtlı adresteki veriyi görsele çevirir
    private lateinit var db: ProgramDatabase
    private lateinit var programDao: ProgramDAO
    private lateinit var mDisposable: CompositeDisposable //istekleri hafızada tutmama için rxjava özelliği
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLancher()

        db = Room.databaseBuilder(requireContext(), ProgramDatabase::class.java, "Programlar")
            .build()
        programDao = db.programDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener { gorselSec(it) }
        binding.silButton.setOnClickListener { sil(it) }
        binding.kaydetButton.setOnClickListener { kaydet(it) }
        mDisposable = CompositeDisposable()

        arguments?.let {
            val bilgi = ProgramFragmentArgs.fromBundle(it).bilgi

            if (bilgi == "yeni") {
                //yeni program eklenecek
                binding.silButton.isEnabled = false
                binding.kaydetButton.isEnabled = true
                binding.isimText.setText("")
            } else {
                binding.silButton.isEnabled = true
                binding.kaydetButton.isEnabled = false
                //eski program gösteriliyor
                val id = ProgramFragmentArgs.fromBundle(it).id

                mDisposable.add(
                    programDao.findById(id).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )

            }

        }
    }

    private fun handleResponse(program: Program) {
        binding.isimText.setText(program.isim)
        val bitmap = BitmapFactory.decodeByteArray(program.gorsel, 0, program.gorsel!!.size)
        binding.imageView.setImageBitmap(bitmap)
        secilenProgram = program
    }

    fun kaydet(view: View) {
        val isim = binding.isimText.text.toString()

        if (secilenBitmap != null) {
            val kucukBitmap = kucukBitmapOlustur(secilenBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteDizisi = outputStream.toByteArray()

            val program = Program(isim, byteDizisi)
            programDao.insert(program)

            mDisposable.add(
                programDao.insert(program)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert)
            )


        }


    }

    private fun handleResponseForInsert() {
        //bir önceki fragmente dön
        val action = ProgramFragmentDirections.actionProgramFragmentToListeFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun sil(view: View) {
        if (secilenProgram!= null) {
            mDisposable.add(programDao.delete(program = secilenProgram!!)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForInsert)
                )
        }
    }

    fun gorselSec(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission
                        .READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //izin verilmemiş ,izin istenmesi gerekiyor

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    // snack bar göstermeliyiz,kulanıcıdan neden izin istediğimizi söyleyerek izin isteyeceğiz
                    Snackbar.make(
                        view,
                        "Galeriye Ulaşıp Fotoğraf Seçmeliyiz!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "izin ver",
                        View.OnClickListener {
                            // izin isteyeceğiz
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                        }
                    )


                } else {
                    //izin isteyeceğiz
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                }


            } else {
                // izin verilmiş galeriye gidilecek
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)


            }

        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission
                        .READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //izin verilmemiş ,izin istenmesi gerekiyor

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    // snack bar göstermeliyiz,kulanıcıdan neden izin istediğimizi söyleyerek izin isteyeceğiz
                    Snackbar.make(
                        view,
                        "Galeriye Ulaşıp Fotoğraf Seçmeliyiz!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "izin ver",
                        View.OnClickListener {
                            // izin isteyeceğiz
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }
                    )


                } else {
                    //izin isteyeceğiz
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }


            } else {
                // izin verilmiş galeriye gidilecek
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)


            }
        }


    }

    private fun registerLancher() {

        //galeriye gitmak için
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // galeriden geleni kontrol ediyor
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data

                        try {


                            if (Build.VERSION.SDK_INT >= 28) {
                                //yeni yöntem
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(secilenBitmap)
                            } else {

                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel
                                )
                                binding.imageView.setImageBitmap(secilenBitmap)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }
                    }

                }

            }


        // izin isteme lancher'i
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //izin verildi
                    // galeriye gidebiliriz
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    //izin verilmedi
                    Toast.makeText(requireContext(), "İzin verilmedi", Toast.LENGTH_LONG).show()


                }

            }

    }

    private fun kucukBitmapOlustur(kullanicininSectigiBitmap: Bitmap, maximumBoyut: Int): Bitmap {
        var width = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        val bitmapOrani: Double = width.toDouble() / height.toDouble()

        if (bitmapOrani >= 1) {//yatay görsel
            width = maximumBoyut
            val kisaltilmisYukseklik = width / bitmapOrani
            height = kisaltilmisYukseklik.toInt()
        } else {              // dikey görsel
            height = maximumBoyut
            val kisaltilmisGenislik = height * bitmapOrani
            width = kisaltilmisGenislik.toInt()

        }


        return Bitmap.createScaledBitmap(kullanicininSectigiBitmap, width, height, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}