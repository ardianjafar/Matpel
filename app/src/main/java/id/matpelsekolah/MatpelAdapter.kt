package id.matpelsekolah

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MatpelAdapter(val mCtx : Context, val layoutResId : Int, val matpelList :List<MataPelajaran> ) : ArrayAdapter<MataPelajaran>(mCtx, layoutResId, matpelList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInflater.inflate(layoutResId, null)

        val tvNamaMatpel = view.findViewById<TextView>(R.id.tv_matkul)
        val tvNilai = view.findViewById<TextView>(R.id.tv_sks)

        val matpel = matpelList[position]

        tvNamaMatpel.text = matpel.nama
        tvNilai.text = matpel.sks.toString()

        return view

    }
}