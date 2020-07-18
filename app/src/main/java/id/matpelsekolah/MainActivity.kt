package id.matpelsekolah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Inisialisasi Data
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnSave: Button
    private lateinit var listGuru: ListView
    private lateinit var ref: DatabaseReference
    private lateinit var guruList: MutableList<Guru>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Untuk mrnghubungkan aplikasi dengan Firebase
        ref = FirebaseDatabase.getInstance().getReference("Guru")

        //pemanggilan activity.xml
        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        btnSave = findViewById(R.id.btn_save)
        listGuru = findViewById(R.id.lv_mhs)
        btnSave.setOnClickListener(this)

        guruList = mutableListOf()
        // Ketika ada data yang di tambah maka akan menampilkan List
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            // func ketika data berubah
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    guruList.clear()
                    for (h in p0.children) {
                        val siswa = h.getValue(Guru::class.java)
                        if (siswa != null) {
                            guruList.add(siswa)
                        }
                    }

                    val adapter = GuruAdapter(this@MainActivity, R.layout.item_mhs, guruList)
                    listGuru.adapter = adapter
                }
            }

        })

        listGuru.setOnItemClickListener { parent, view, position, id ->
            val siswa = guruList.get(position)

            val intent = Intent(this@MainActivity, AddMatpelActivity::class.java)
            intent.putExtra(AddMatpelActivity.EXTRA_ID, siswa.id)
            intent.putExtra(AddMatpelActivity.EXTRA_NAMA, siswa.nama)
            startActivity(intent)
        }
    }
    //ketika data telah di inputkan lalu user ingin menyimpannya
    override fun onClick(v: View?) {
        //func ketika user telah mengklik button save
        saveData()
    }
    // func save data
    private fun saveData() {
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = "Isi Nama!"
            return
        }

        if (alamat.isEmpty()) {
            etAlamat.error = "Isi alamat!"
            return
        }

        val siswaId = ref.push().key

        val mhs = Guru(siswaId!!, nama, alamat)

        if (siswaId != null) {
            ref.child(siswaId).setValue(mhs).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}