package com.example.primeraplicacionprueba.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Place
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun Map (
    modifierr: Modifier = Modifier,
activateClick: Boolean = false,
places :List<Place> = emptyList(),
onMapClickListener :(Point) -> Unit ={},
onPlaceSelected: (Place) -> Unit = {},
centerPoint: Point? = null,
centerZoom: Double? = null,
selectedPoint: Point? = null

){
    val context = LocalContext.current

    val hasPermission = rememberLocationPermissionState{
        Toast.makeText(
            context,
            if(it) context.getString(R.string.toast_location_permission_granted_alt) else context.getString(R.string.toast_location_permission_denied_alt),
            Toast.LENGTH_SHORT
        ).show()
    }
    var clickedPoint by rememberSaveable { mutableStateOf<Point?>(null) }

    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter= painterResource(R.drawable.red_marker)
    )
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            setCameraOptions {
                zoom(7.0)
                center(Point.fromLngLat(-75.6491181, 4.4687891))
                pitch(45.0)
            }
        }
    }
    MapboxMap (
        modifier = modifierr,
        mapViewportState = mapViewportState,
        onMapClickListener = {
            if(activateClick){
                onMapClickListener(it)
                clickedPoint = it
            }
            true
        }

    ){
        if(hasPermission){

            MapEffect (key1 = "follow_puck") { mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }


                mapViewportState.transitionToFollowPuckState(
                    defaultTransitionOptions = DefaultViewportTransitionOptions.Builder()
                        .maxDurationMs(0).build()
                )
            }

        }
        clickedPoint?.let {
            PointAnnotation(point = it ){
                iconImage = marker
            }
        }

        // Centrar la cámara si se solicita un punto objetivo (por ejemplo, tras aplicar filtros)
        if (centerPoint != null) {
            MapEffect(key1 = centerPoint to centerZoom) { mapView ->
                mapViewportState.setCameraOptions {
                    center(centerPoint)
                    centerZoom?.let { zoom(it) }
                }
            }
        }

        if (places.isNotEmpty()) {
            places.forEach { place ->
                PointAnnotation(
                    point = Point.fromLngLat(place.location.longitude, place.location.latitude)
                ) {
                    iconImage = marker
                        interactionsState.onClicked {
                        onPlaceSelected(place)
                        true
                    }
                }
            }
        }

        // Mostrar marcador para punto seleccionado (por ejemplo, en CreatePlace)
        selectedPoint?.let {
            PointAnnotation(point = it) {
                iconImage = marker
            }
        }
    }
}

@Composable
fun rememberLocationPermissionState(
    permission: String = Manifest.permission.ACCESS_FINE_LOCATION,
    onPermissionResult: (Boolean) -> Unit
): Boolean {
    val context = LocalContext.current
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
        onPermissionResult(granted)
    }

    LaunchedEffect (Unit) {
        if (!permissionGranted.value) {
            launcher.launch(permission)
        }
    }

    return permissionGranted.value
}