package id.matpelsekolah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*


class AddMatpelActivity : AppCompatActivity() {
    private lateinit var tvNama : TextView
    private lateinit var etMatpel : EditText
    private lateinit var etNilai : EditText
    private lateinit var btnMatpel : Button
    private lateinit var lvMatpel : ListView
    private lateinit var matpelList : MutableList<MataPelajaran>
    private lateinit var ref : DatabaseReference

    companion object{
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_matpel)

        val id = intent.getStringExtra(EXTRA_ID)
        val nama = intent.getStringExtra(EXTRA_NAMA)
        matpelList = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("mata pelajaran").child(id!!)

        tvNama = findViewById(R.id.tv_nama)
        etMatpel = findViewById(R.id.et_matkul)
        etNilai = findViewById(R.id.et_sks)
        btnMatpel = findViewById(R.id.btn_matkul)
        lvMatpel = findViewById(R.id.lv_matkul)

        btnMatpel.setOnClickListener{
            saveMatpel()
        }
    }

    fun saveMatpel(){
        val namaMatpel = etMatpel.text.toString().trim()
        val nilaiText = etNilai.text.toString().trim()
        val nilai = nilaiText.toInt()

        if(namaMatpel.isEmpty()){
            etMatpel.error = "Matkul harus diisi"
            return
        }
        if(nilaiText.isEmpty()){
            etNilai.error = "SKS harus diisi"
            return
        }

        val matpelId = ref.push().key

        val matpel = MataPelajaran(matpelId!!,namaMatpel,nilai)

        if (matpelId != null) {
            ref.child(matpelId).setValue(matpel).addOnCompleteListener{
                Toast.makeText(applicationContext, "Matakuliah berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    matpelList.clear()
                    for(h in snapshot.children){
                        val matakuliah = h.getValue(MataPelajaran::class.java)
                        if (matakuliah != null) {
                            matpelList.add(matakuliah)
                        }
                    }
                    val adapter = MatpelAdapter(this@AddMatpelActivity, R.layout.item_matkul, matpelList)
                    lvMatpel.adapter = adapter
                }
            }
        })
    }
}