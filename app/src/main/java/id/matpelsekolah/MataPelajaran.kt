package id.matpelsekolah

data class MataPelajaran(
    val id : String,
    val nama : String,
    val sks : Int
){
    constructor(): this("","",0){

    }
}