package id.matpelsekolah

data class Guru(
    val id : String,
    val nama: String,
    val alamat : String
){
    constructor(): this("","",""){

    }
}