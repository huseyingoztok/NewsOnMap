package com.example.husgo.newsonmap

import android.content.Intent
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var newsList:ArrayList<News> =ArrayList<News>()
    val MAX_ELEMENT = 15
    val API_KEY = "e58be05518d14dd09a601044caf3b5e9"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //newsList=ArrayList<News>()




        var mySelect = "Description,Title,CreatedDate"

        val url = "https://api.hurriyet.com.tr/v1/articles?%24select=$mySelect&%24top=$MAX_ELEMENT&apikey=$API_KEY"
        var title: String? = null
        var jsonobjReq2 = JsonArrayRequest(Request.Method.GET, url, null, object : Response.Listener<JSONArray> {
            override fun onResponse(response: JSONArray?) {
                for (i in 0..response!!.length()-1) {
                    var oneNew = response.getJSONObject(i)
                    var rnd: Random = Random()
                    var lat = 36.5 + (rnd.nextDouble() * 6) //Türkiye Latitude sınırı 35.9025 to 42.02683 a>b olmak üzere a ile b  arasında sayı üretmek için a+(rnd*(b-a)) a ile b arasında sayı üretilir
                    var longitude = 26 + (rnd.nextDouble() * 17)                     //25.90902 to 44.5742.
                    var n = News(oneNew.getString("Title"), oneNew.getString("Description"), lat, longitude,oneNew.getString("CreatedDate"))
                    mMap.setOnInfoWindowClickListener(markerClick);
                    var loc = LatLng(n.lat, n.longitude)
                    mMap.addMarker(MarkerOptions().position(loc).title(n.newTitle))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                    newsList.add(n)
                }
            }


        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.e("HATA", " " + error!!.message)
            }

        })

        MySingleton.getInstance(this@MapsActivity).addToRequestQueue(jsonobjReq2)





        //mMap.setOnMapLongClickListener(uzunBasma)



        /*for (h in newsList) {
            Log.e("Deneme",""+h.newTitle)

            //h = googleMap

            // Add a marker in Sydney and move the camera
            var loc = LatLng(h.lat, h.longitude)
            mMap.addMarker(MarkerOptions().position(loc).title(h.newTitle.substring(0,15)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

        }*/


    }



    val markerClick = object : GoogleMap.OnInfoWindowClickListener {
        override fun onInfoWindowClick(p0: Marker?) {
            for (i in 0..newsList.size - 1) {
                if (p0!!.position.equals(LatLng(newsList[i].lat, newsList[i].longitude))) {
                    var intent = Intent(this@MapsActivity, MainActivity::class.java)
                    intent.putExtra("MyNew", newsList[i])
                    startActivity(intent)

                }
            }
        }


    }





}
