package id.matpelsekolah

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class GuruAdapter(val mCtx : Context, val layoutResId : Int, val guruList :List<Guru> ) :ArrayAdapter<Guru> (mCtx, layoutResId, guruList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val tvNama : TextView = view.findViewById(R.id.tv_nama)
        val tvAlamat : TextView = view.findViewById(R.id.tv_alamat)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)
        val guru = guruList[position]

        tvEdit.setOnClickListener{
            showUpdateDialog(guru)
        }
        tvNama.text = guru.nama
        tvAlamat.text = guru.alamat
        return view
    }

    fun showUpdateDialog(guru: Guru) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Data")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)
        val etNama = view.findViewById<EditText>(R.id.et_nama)
        val etAlamat = view.findViewById<EditText>(R.id.et_alamat)

        etNama.setText(guru.nama)
        etAlamat.setText(guru.alamat)

        builder.setView(view)
        builder.setPositiveButton("Update"){p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance().getReference("Guru")
            val nama = etNama.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            if(nama.isEmpty()){
                etNama.error = "Mohon nama di iisi"
                etNama.requestFocus()
                return@setPositiveButton
            }
            if(alamat.isEmpty()){
                etAlamat.error = "Mohon alamat diisi"
                etAlamat.requestFocus()
                return@setPositiveButton
            }
            val guru = Guru(guru.id, nama, alamat)
            dbMhs.child(guru.id).setValue(guru)
            Toast.makeText(mCtx, "Data berhasil di update", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("No"){p0,p1 ->

        }

        builder.setNegativeButton("Delete"){p0,p1 ->
            val dbGuru =  FirebaseDatabase.getInstance().getReference("Guru").child(guru.id)
            val dbMatpel = FirebaseDatabase.getInstance().getReference("mata pelajaran").child(guru.id)

            dbGuru.removeValue()
            dbMatpel.removeValue()

            Toast.makeText(mCtx, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
        val alert = builder.create()
        alert.show()
    }
}
