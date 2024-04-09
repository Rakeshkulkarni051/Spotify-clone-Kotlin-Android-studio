import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockbuilds.soundify.API_interfaces.DataInterface
import com.rockbuilds.soundify.Data_class.album
import com.rockbuilds.soundify.Fragments.history
import com.rockbuilds.soundify.Fragments.notifications
import com.rockbuilds.soundify.Fragments.setting
import com.rockbuilds.soundify.R
import com.rockbuilds.soundify.adapters.AlbAdapter
import com.rockbuilds.soundify.adapters.Albs_adapter
import com.rockbuilds.soundify.adapters.PLadapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class Home()  : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var myadapter: AlbAdapter
    lateinit var pl_rcv:RecyclerView
    lateinit var pl_adapter:PLadapter
    lateinit var hits_rcy:RecyclerView
    lateinit var albumsadp:Albs_adapter
    lateinit var mash_rcy:RecyclerView
    lateinit var hiph:RecyclerView
    lateinit var reco:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val greeting = view.findViewById<TextView>(R.id.greeting)
        val notification=view.findViewById<ImageView>(R.id.notifications)
        val history=view.findViewById<ImageView>(R.id.history)
        val gear=view.findViewById<ImageView>(R.id.setting)

        recyclerView=view.findViewById(R.id.rcy_albums)
        pl_rcv=view.findViewById(R.id.rcy_playlists)
        hits_rcy=view.findViewById(R.id.hits24)
        mash_rcy=view.findViewById(R.id.mashups20)
        hiph=view.findViewById(R.id.hiphop)
        reco=view.findViewById(R.id.rfy)

        //Albums Titles
        val txt_artist=view.findViewById<TextView>(R.id.artists)
        val txt_hits=view.findViewById<TextView>(R.id.hits)
        val txt_mashup=view.findViewById<TextView>(R.id.mashup)
        val txt_hip=view.findViewById<TextView>(R.id.hh)
        val txt_rcy=view.findViewById<TextView>(R.id.rcy)


        // Initialize RecyclerViews
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        pl_rcv.layoutManager = GridLayoutManager(requireContext(), 2)

        hits_rcy.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mash_rcy.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
       hiph.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        reco.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Get the current time
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        // Update greeting text based on the current time
        val greetingText = when (hourOfDay) {
            in 0..3 -> "Good morning" // 12 AM to 3 AM
            in 4..11 -> "Good morning"
            in 12..15 -> "Good afternoon"
            in 16..23 -> "Good evening"
            else -> "NULL"
        }
        greeting.text = greetingText


        notification.setOnClickListener {
            val fragment =notifications()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_frame, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        history.setOnClickListener {
            val fragment =history()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_frame, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        gear.setOnClickListener {
            val fragment =setting()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_frame, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //Populating the Data
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://spotify23.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataInterface::class.java)


        //fetch Grid Playlists
        val playlistid= listOf("5APZ2ECPRfe0cddST1jOOF",
                "7sHrPadM39466NY7DW3s4r",
                "7pQTHwx9EU9Jni7Z1CWkT5",
                "6NvpMMAPklMO4SvieenEKT",
                "60V986cjpv3YVoUEIKdKvy",
                "6al2VdKbb6FIz9d7lU7WRB"
        )
        val playlistidStr=playlistid.joinToString(",")

                val PlaylistData = retrofitBuilder.getAlbumData(playlistidStr)


        PlaylistData.enqueue(object : Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dataList = response.body()?.albums ?: emptyList()
                pl_adapter = PLadapter(requireActivity(), dataList)
                pl_rcv.adapter =pl_adapter
            }

            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })

        //Fetch Top artist Albums Data
        val albumIds = listOf(
            "4xeWDuTiI7eEyMiQAtVBp3",
            "6UtgXbhq3Y2HISrGamfnro",
            "7g7aT1QRPm6eQzTFnkxqtl",
            "6bpBiAg8JiNLreVexDpFPC",
            "4dYNdtPc5EUSmKtyZ9x0l0",
            "3HlLWECHDAx5FuFFktQWNj",
            "1dNAXHTY5Ezn0hcH7N5BsH",
            "3q6uhwTdA7ygXyZpYfNpVh",
            "4PMVBKIe8o2iixsVkGDMBG"
        )
        val albumIdsString = albumIds.joinToString(",")
        val retrofitDataAlbum = retrofitBuilder.getAlbumData(albumIdsString)


        retrofitDataAlbum.enqueue(object : Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dataList = response.body()?.albums ?: emptyList()
                myadapter = AlbAdapter(requireActivity(), dataList)
                recyclerView.adapter = myadapter
                txt_artist.visibility=View.VISIBLE
            }

            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })

        //2024 hits
        val hits_id = listOf("0KvWbswDD0ksvFayBl6gUz",
                "0a183xiCHiC1GQd8ou7WXO",
                "1YlugBItLrdu7CEZwRGskr",
                "3WLJmMZUeDOuERFAk1Mxs6",
                "6mOSDGo8ZREISAbaiTMW5J",
                "6AgKY9DZqh6C2lkMyCC8pn")
        val hitsid_string=hits_id.joinToString(",")

        val retrofithits=retrofitBuilder.getAlbumData(hitsid_string)

        retrofithits.enqueue(object :Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dL = response.body()?.albums ?: emptyList()
                albumsadp = Albs_adapter(requireActivity(), dL)
                hits_rcy.adapter = albumsadp
                txt_hits.visibility=View.VISIBLE
            }
            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })

        //20's Mashups
        val mashup20=listOf("4OYdTHNgjhXzgVjbqsb0tO",
            "5xjaz957o6YGSXmlfd2tex",
            "2Lxoc72vRTGdQfMvj7Ovi1",
            "2KZs4INik6X4KeZEsEWEm4",
            "2cUzlmLfL5LUTSEk7qG09k",
            "7pbKuvQHUTwbq4FbevvEca",
            "0Rkv5iqjF2uenfL0OVB8hg",
            "3EkmvTqyKrnMw1WiVpsSwF",
            "2CUXo26JAWIbQx0EVMnjpA",
        )
        val mash_string=mashup20.joinToString(",")
        val retrofit_mash=retrofitBuilder.getAlbumData(mash_string)

        retrofit_mash.enqueue(object :Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dL = response.body()?.albums ?: emptyList()
                albumsadp = Albs_adapter(requireActivity(), dL)
                mash_rcy.adapter = albumsadp
                txt_mashup.visibility=View.VISIBLE
            }
            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })

        //Hip Hop
        val rap_id=listOf("1xXv2wox60JAkIKUl8QwWp",
            "6CiopvBPnSPd7G6l3qbbHt",
            "0XDtCjokUgSrL527Ftk0xE",
            "3TWJqzaophqIi6ZIm5wkux",
            "16PSZwABl4VFJvfDFOPOoB",
            "0LcyzKKw3RjFKL6ygISTeU",
        )
        val rap_string=rap_id.joinToString(",")

        val retorfit_hip = retrofitBuilder.getAlbumData(rap_string)


        retorfit_hip.enqueue(object : Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dataList = response.body()?.albums ?: emptyList()
                myadapter = AlbAdapter(requireActivity(), dataList)
                hiph.adapter = myadapter
                txt_hip.visibility=View.VISIBLE
            }

            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })


       // recommended
        val rec_uid=listOf("2xfAmgKBQroCYbDnl18me3",
            "0MPHvcqKbQQl6z7mtrmTAo",
            "4UldFWVMJgHPsgBF1jIzuU",
            "2jYkg3tbnUceBK1Gt0Hb7k",
            "1fQd1LKKDndPgcdHKs6WQM",
            "1n4k7qcOWcnphiLVajCLMk",
            "0pc4JFQ72mz6k7BtY7FawU",
            "4RqyXIo6nHMpUtjFYQBdPs"
        )
        val rec_string=rec_uid.joinToString(",")
       val retro_recu=retrofitBuilder.getAlbumData(rec_string)
        retro_recu.enqueue(object :Callback<album?> {
            override fun onResponse(call: Call<album?>, response: Response<album?>) {
                val dL = response.body()?.albums ?: emptyList()
                albumsadp = Albs_adapter(requireActivity(), dL)
                reco.adapter = albumsadp
                txt_rcy.visibility=View.VISIBLE
            }

            override fun onFailure(call: Call<album?>, t: Throwable) {
                //On  API failure
                Log.e("ERROR", "Failure " + t.message)
            }
        })

        return view
    }
}
