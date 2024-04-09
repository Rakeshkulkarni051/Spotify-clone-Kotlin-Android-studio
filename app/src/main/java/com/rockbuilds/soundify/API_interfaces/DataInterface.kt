package com.rockbuilds.soundify.API_interfaces
import com.rockbuilds.soundify.Data_class.album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query



interface DataInterface {
   @Headers("X-RapidAPI-Key: 8c0f39b7c8msh5de5f80fe9cfc63p11c578jsn58417fb53ee0","X-RapidAPI-Host: spotify23.p.rapidapi.com")
    @GET("albums")
    fun getAlbumData(@Query("ids") query: String): Call<album>
}


