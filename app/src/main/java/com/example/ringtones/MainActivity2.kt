package com.example.ringtones

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class MainActivity2 : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Usa aquí el mismo ID del layout raíz de activity_main2.xml (ej. R.id.main2 o R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listaRingtones = listOf(
            RingtoneItem("Aaah Bob Esponja", R.raw.aaah_bob_esponja, "aaah_bob_esponja.mp3"),
            RingtoneItem("Cancion con gemidos", R.raw.cancion_con_gemidos, "cancion_con_gemidos.mp3"),
            RingtoneItem("Detras de ti imbecil ganado", R.raw.detras_de_ti_imbecil_ganado, "detras_de_ti_imbecil_ganado.mp3"),
            RingtoneItem("El chavo del ocho", R.raw.el_chavo_del_ocho, "el_chavo_del_ocho.mp3"),
            RingtoneItem("Elite", R.raw.elite, "elite.mp3"),
            RingtoneItem("Elite pozole", R.raw.elite_pozole, "elite_pozole.mp3"),
            RingtoneItem("Expedientes secretos X", R.raw.expedientes_secretos_x, "expedientes_secretos_x.mp3"),
            RingtoneItem("Gemidos fuertes", R.raw.gemidos_fuertes, "gemidos_fuertes.mp3"),
            RingtoneItem("Halo elite sounds", R.raw.halo_elite_sounds, "halo_elite_sounds.mp3"),
            RingtoneItem("Lo que se dano fue el software jasguer", R.raw.lo_que_se_dano_fue_el_software_jasguer, "lo_que_se_dano_fue_el_software_jasguer.mp3"),
            RingtoneItem("Me pican los cocos", R.raw.me_pican_los_cocos_oooooooooooooo, "me_pican_los_cocos_oooooooooooooo.mp3"),
            RingtoneItem("Oh pobrecito violin triste", R.raw.oh_pobrecito_violin_triste, "oh_pobrecito_violin_triste.mp3"),
            RingtoneItem("Patricio que le pasa es estupido", R.raw.patricio_que_le_pasa_es_estupido, "patricio_que_le_pasa_es_estupido.mp3"),
            RingtoneItem("Que paso wey", R.raw.que_paso_wey, "que_paso_wey.mp3"),
            RingtoneItem("Quiero muerto a ese", R.raw.quiero_muerto_a_ese, "quiero_muerto_a_ese.mp3"),
            RingtoneItem("R2D2 grito", R.raw.r2d2_grito, "r2d2_grito.mp3"),
            RingtoneItem("Tal vez sea estupido", R.raw.telves_sea_estupido, "telves_sea_estupido.mp3"),
            RingtoneItem("Tiburon banda sonora", R.raw.tiburon_banda_sonora, "tiburon_banda_sonora.mp3"),
            RingtoneItem("Waagh", R.raw.waagh, "waagh.mp3"),
            RingtoneItem("Woboba", R.raw.woboba, "woboba.mp3"),
            RingtoneItem("spider_man", R.raw.spider_man, "spider_man.mp3"),
            RingtoneItem("spiderman_meme_song", R.raw.spiderman_meme_song, "spiderman_meme_song.mp3")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rvRingtones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RingtoneAdapter(
            items = listaRingtones,
            onPlayClick = { item -> reproducirAudio(item) },
            onDownloadClick = { item -> descargarAudio(item) }
        )
    }

    private fun reproducirAudio(item: RingtoneItem) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, item.resId)?.apply {
            setOnCompletionListener {
                it.release()
                if (mediaPlayer === it) mediaPlayer = null
            }
            start()
        }
        Toast.makeText(this, "Reproduciendo ${item.titulo}", Toast.LENGTH_SHORT).show()
    }

    private fun descargarAudio(item: RingtoneItem) {
        try {
            val inputStream = resources.openRawResource(item.resId)
            val bytes = inputStream.readBytes()
            inputStream.close()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, item.nombreArchivo)
                    put(MediaStore.Downloads.MIME_TYPE, "audio/mpeg")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    contentResolver.openOutputStream(it)?.use { out -> out.write(bytes) }
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val outFile = File(downloadsDir, item.nombreArchivo)
                FileOutputStream(outFile).use { it.write(bytes) }
            }

            Toast.makeText(this, "Descargado: ${item.nombreArchivo}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al descargar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}