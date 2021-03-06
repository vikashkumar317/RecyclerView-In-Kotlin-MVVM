package com.example.recyclerviewinkotlin.ui.fragment

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.recyclerviewinkotlin.R
import com.example.recyclerviewinkotlin.db.userdata.Task
import com.example.recyclerviewinkotlin.viewmodel.RouteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.lang.Math.abs
import java.lang.Math.atan


class MapFragment() : Fragment() {
    lateinit var itemTask: Task
    var mapFragment: SupportMapFragment? = null
    lateinit var createRoute: Button
    lateinit var startBtn: Button
    lateinit var llDistance: LinearLayout
    lateinit var llButton: LinearLayout
    lateinit var distanceTV: TextView
    lateinit var timeTV: TextView
    lateinit var mContext: Context
    private var movingCabMarker: Marker? = null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize itemTask from arguments data
        arguments?.let {
            itemTask = it.getParcelable("my_task")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        createRoute = view.findViewById(R.id.createRoute)
        startBtn = view.findViewById(R.id.startBtn)
        llDistance = view.findViewById(R.id.llDistance)
        distanceTV = view.findViewById(R.id.distanceTV)
        timeTV = view.findViewById(R.id.timeTV)
        llButton = view.findViewById(R.id.llButton)
        llButton.visibility = View.VISIBLE
    }


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        showDestinationLocation(googleMap)
    }

    private fun showDestinationLocation(googleMap: GoogleMap){
        // add marker to destination location ( from itemTask )
        val destinationLocation = LatLng(itemTask.job_latitude.toDouble(), itemTask.job_longitude.toDouble())
        googleMap.addMarker(MarkerOptions().position(destinationLocation).title("Destination Location"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 10f))
        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(googleMap.cameraPosition.target).zoom(17f).bearing(30f).tilt(45f).build()))

        // when createRoute Button is clicked
        createRoute.setOnClickListener {
            checkLocationPermission(googleMap, destinationLocation)
        }
    }

    private fun checkLocationPermission(googleMap: GoogleMap, destinationLoc: LatLng) {
        // check if user gives permission for ACCESS FINE LOCATION and ACCESS COARSE LOCATION
        if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
            if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 2)
                } else {
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 2)
                }
            }
        } else {
            // get reference of Location Manager
            val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            // get Last Location using GPS
            // https://stackoverflow.com/questions/5757565/how-to-find-my-current-location-latitude-longitude-on-click-of-a-button-in-a
            var location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // if GPS Last location is not available try to get Last Location using network provider
            if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            // if last location from both provider is not available
            // then wait until GPS provider gives current location
            while(location == null){
                var tempLoc: Location? = null
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, LocationListener{
                    tempLoc = it
                })
                location = tempLoc
            }
            if(location != null){
                //
                var sourceLocation: LatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(sourceLocation).title("Source Location"))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceLocation, 5f))

                // create url using source and destination location
                var url = "json?mode=driving&transit_routing_preference=less_driving&origin="+sourceLocation.latitude+","+sourceLocation.longitude+"&destination="+destinationLoc.latitude+","+destinationLoc.longitude+"&key="+resources.getString(R.string.google_maps_key)

                // call googleapis to get route data between source and destination
                RouteViewModel().getRouteData(url).observe(this, Observer {
                    llButton.visibility = View.GONE
                    llDistance.visibility = View.VISIBLE

                    // show distance between source and destination
                    distanceTV.text = it.routes[0].legs[0].distance.text

                    // show required duration to cover distance between source and destination
                    timeTV.text = it.routes[0].legs[0].duration.text

                    // get List of LatLng after decoding polyline points of steps
                    var path = ArrayList<LatLng>()
                    for (x in it.routes[0].legs[0].steps){
                        path.addAll(decodePoly(x.polyline.points))
                    }

                    // Adjusting Bounds (show local area between start and end position)
                    var builder = LatLngBounds.Builder()
                    for(x in path) builder.include(x)
                    var bounds = builder.build()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))

                    // draw route of blue color between start and end position
                    var polylineOptions = PolylineOptions()
                    polylineOptions.color(Color.BLUE)
                        .width(5f)
                        .addAll(path)
                    googleMap.addPolyline(polylineOptions)

                    // when startBtn is clicked
                    startBtn.setOnClickListener(View.OnClickListener {
                        llDistance.visibility = View.GONE

                        // start cab movement
                        showMovingCab(path, googleMap)
                    })
                })
            }

        }
    }

    private fun showMovingCab(cabLatLngList: ArrayList<LatLng>, googleMap: GoogleMap) {
        handler = Handler()
        var index = 0

        // update car location until car reaches destination position
        runnable = Runnable {
            run {
                if (index < cabLatLngList.size) {
                    updateCarLocation(cabLatLngList[index], googleMap)
                    handler.postDelayed(runnable, 300)
                    ++index
                } else {
                    handler.removeCallbacks(runnable)
                }
            }
        }
        handler.postDelayed(runnable, 500)
    }

    // return bitMap image of Car
    private fun getCarBitmap(): Bitmap {
        val bitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_car)
        return Bitmap.createScaledBitmap(bitmap, 50, 100, false)
    }

    // to update car Location
    private fun updateCarLocation(latLng: LatLng, googleMap: GoogleMap) {
        var tempRotation = 0f;

        // add Car Marker
        if (movingCabMarker == null) {
            movingCabMarker = addCarMarkerAndGet(latLng, googleMap)
        }

        // for source position
        if (previousLatLng == null) {
            currentLatLng = latLng
            previousLatLng = currentLatLng
            movingCabMarker?.position = currentLatLng
            movingCabMarker?.setAnchor(0.5f, 0.5f)
            animateCamera(currentLatLng!!, googleMap)
        }

        // except source position
        else {
            previousLatLng = currentLatLng
            currentLatLng = latLng
            val valueAnimator = carAnimator()
            valueAnimator.addUpdateListener { va ->
                if (currentLatLng != null && previousLatLng != null) {

                    // calculate next Location using fraction value
                    val multiplier = va.animatedFraction
                    val nextLocation = LatLng(
                        multiplier * currentLatLng!!.latitude + (1 - multiplier) * previousLatLng!!.latitude,
                        multiplier * currentLatLng!!.longitude + (1 - multiplier) * previousLatLng!!.longitude
                    )

                    // place cab on the calculated position (next Location)
                    movingCabMarker?.position = nextLocation

                    // place cab in the middle with respect to the route
                    movingCabMarker?.setAnchor(0.5f, 0.5f)

                    // move camera and map according to position change and bearing value
                    var cameraPosition = CameraPosition.Builder().target(nextLocation).zoom(12f)

                    val rotation = getRotation(previousLatLng!!, nextLocation)
                    if (!rotation.isNaN()) {
                        movingCabMarker?.rotation = rotation
                        cameraPosition.bearing(rotation)
                    }
                    if(abs(tempRotation-rotation) > 30)
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()))
                    else
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()))
                    tempRotation = rotation
                }
            }
            valueAnimator.start()
        }
    }

    // angle between start position and end position
    fun getRotation(start: LatLng, end: LatLng): Float {
        val latDifference: Double = abs(start.latitude - end.latitude)
        val lngDifference: Double = abs(start.longitude - end.longitude)
        var rotation = -1F
        when {
            start.latitude < end.latitude && start.longitude < end.longitude -> {
                rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
            }
            start.latitude >= end.latitude && start.longitude < end.longitude -> {
                rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
            }
            start.latitude >= end.latitude && start.longitude >= end.longitude -> {
                rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
            }
            start.latitude < end.latitude && start.longitude >= end.longitude -> {
                rotation =
                    (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
            }
        }
        return rotation
    }

    // car valueAnimator using LinearInterpolator
    private fun carAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 3000
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }

    // animate camera position according to given position
    private fun animateCamera(latLng: LatLng, googleMap: GoogleMap) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    // return Car Marker
    private fun addCarMarkerAndGet(latLng: LatLng, googleMap: GoogleMap): Marker {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getCarBitmap())
        return googleMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )
    }

    // to decode Poly
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }


    companion object {
        private const val MY_TASK = "my_task"

        // return MapFragment instance
        @JvmStatic
        fun newInstance(task: Task): MapFragment{
            val fragment = MapFragment()
            var  args = Bundle()
            args.putSerializable(MY_TASK, task)
            fragment.arguments = args
            return fragment
        }
    }
}