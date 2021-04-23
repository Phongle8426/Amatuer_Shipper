package com.example.AmateurShipper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;


import com.example.AmateurShipper.Util.LocationListeningCallback;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    private static final String ROUTE_LAYTER_ID = "ROUTE-LAYER-ID";
    private static final String ROUTE_SOURCE_ID = "ROUTE-SOURCE-ID";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private static final String ROUTE_LAYER_ID = "route-layer-id";


    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private CarmenFeature home;
    private CarmenFeature work;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private static final int REQUEST_CODE = 5678;
    String address;
    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;
    Point origin = Point.fromLngLat(108.18511468010284, 16.06491495661151);
    Point destination = Point.fromLngLat(108.21827671263680, 16.058174650163377);
    private LocationListeningCallback callback = new LocationListeningCallback(this);

    int c = 0;
    double distance;
    String st;
    String startLocation = "";
    String endLocation = "";

    MapView mapView;
    MapboxMap mapboxMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab_profile, tab_statis;
    public PageAdapterProfile pagerAdapterProfile;
    Button confirmButton;
    private LocationEngine locationEngine;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
//        else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }

        PermissionsManager permissionsManager;

        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

// Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
            Toast.makeText(getActivity(), " granted", Toast.LENGTH_SHORT).show();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
            Toast.makeText(getActivity(), "just granted", Toast.LENGTH_SHORT).show();
        }

//        if(ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//            Toast.makeText(getActivity(), "is not granted", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(getActivity(), "is not granted", Toast.LENGTH_SHORT).show();
//        }


        Mapbox.getInstance(getContext(),getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        confirmButton = view.findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmed();
            }
        });
        mapView = (MapView)view.findViewById(R.id.mapView);
        mapView.getMapAsync(this);





        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if (response.body() == null) {
            Toast.makeText(getActivity(), "No routes found", Toast.LENGTH_LONG);
            return;
        } else if (response.body().routes().size() < 1) {
            Toast.makeText(getActivity(), "No routes found", Toast.LENGTH_SHORT).show();
            return;
        }
        final DirectionsRoute currentRoute = response.body().routes().get(0);

        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                    if (source != null) {
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                    }
                }
            });
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

    }
    public void navigationRoute(){
        NavigationRoute.builder(getActivity())
                .accessToken("pk.eyJ1IjoidHJvbmd0aW4iLCJhIjoiY2tubGluaDk5MGk2MDJvcGJubXBmYjAybSJ9.iod8C2tfXJYSq3sA9ngCtA")
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>(){

                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(getActivity(), "No routes found", Toast.LENGTH_LONG);
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Toast.makeText(getActivity(), "No routes found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final DirectionsRoute currentRoute = response.body().routes().get(0);
                        boolean simulateRoute = true;


                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();

                        NavigationLauncher.startNavigation(getActivity(), options);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }
    public void confirmed(){
        navigationRoute();

    }
    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(108.20944498264936,16.059270410781075 ))
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .geometry(Point.fromLngLat(108.21731185566273,16.067913737424842))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }
    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})
        ));
    }
    private void initSearchFab() {
        getActivity().findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ")
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }
    /**
     * Called when the map is ready to be used.
     *
     * @param mapboxMap An instance of MapboxMap associated with the {@link MapFragment} or
     *                  {@link MapView} that defines the callback.
     */

    public void initializeLocationEngine(){
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());
    }



    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap=mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
// Set the origin location to the Alhambra landmark in Granada, Spain.
                enableLocationComponent(style);
                initSearchFab();

                addUserLocations();

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null);

                Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, mBitmap);

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);


                origin = Point.fromLngLat(108.18511468010284, 16.06491495661151);
                destination = Point.fromLngLat( 108.21827671263680,16.058174650163377) ;

                initSource(style);

                initLayers(style);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(destination.latitude(), destination.longitude()))
                        .zoom(18)
                        .tilt(13)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    LatLng source;

                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {

                        if (c == 0) {
                            origin = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            source = point;
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(point);
                            markerOptions.title("Source");
                            mapboxMap.addMarker(markerOptions);
                            reverseGeocodeFunc(point,c);


                        }
                        if (c == 1) {
                            destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            getRoute(mapboxMap, origin, destination);
                            MarkerOptions markerOptions2 = new MarkerOptions();
                            markerOptions2.position(point);
                            markerOptions2.title("destination");
                            mapboxMap.addMarker(markerOptions2);
                            reverseGeocodeFunc(point,c);
                            getRoute(mapboxMap, origin, destination);
                            // double d = point.distanceTo(source);



                        }



                      /*  startActivityForResult(
                                new PlacePicker.IntentBuilder()
                                        .accessToken("pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ")
                                        .placeOptions(PlacePickerOptions.builder()
                                                .statingCameraPosition(new CameraPosition.Builder()
                                                        .target(point).zoom(16).build())
                                                .build())
                                        .build(this), REQUEST_CODE);
                        */
                        if (c > 1) {
                            c = 0;

                            // mapboxMap.clear();
                            //   Toast.makeText(MainActivity.this,d+" metres", Toast.LENGTH_LONG).show();

                        }

                        c++;
                        return true;


                    }

                });


                getRoute(mapboxMap, origin, destination);
            }
        });
    }

    private void reverseGeocodeFunc(LatLng point,int c)
    {
        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ")
                .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_POI)
                .build();
        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {
                    // CarmenFeature feature =results.get(0);
                    CarmenFeature feature;
                    // Log the first results Point.
                    Point firstResultPoint = results.get(0).center();
                    //   for (int i = 0; i < results.size(); i++) {
                    //  feature = results.get(i);
                    feature=results.get(0);
                    if(c==0)
                    {
                        startLocation+=feature.placeName();
                        startLocation=startLocation.replace(", Dhaka, Bangladesh",".");
                        TextView tv = getActivity().findViewById(R.id.s);
                        tv.setText(startLocation);

                    }
                    if(c==1) {
                        endLocation += feature.placeName();
                        endLocation = endLocation.replace(", Dhaka, Bangladesh", ".");
                        TextView tv2 = getActivity().findViewById(R.id.d);
                        tv2.setText(endLocation);
                    }

                    // endLocation = endLocation.replace(",Dhaka,Bangladesh", " ");
                    // Toast.makeText(MapsActivity.this, endLocation, Toast.LENGTH_LONG).show();





                    // startLocation=feature.placeName()+"";

                    //   Toast.makeText(MainActivity.this, "" + results.get(i), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "" + feature.placeName(), Toast.LENGTH_LONG).show();

                    //  }
                    Log.d("MyActivity", "onResponse: " + firstResultPoint.toString());

                } else {

                    // No result for your request were found.
                    Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.requestLocationPermissions(getActivity());
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.marker)));

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -9f})));
    }
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken())
                .build();
        client.enqueueCall(this);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            //Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
        /*if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {
                Toast.makeText(MapsActivity.this, String.format(address
                        , carmenFeature.toJson()), Toast.LENGTH_SHORT).show();
            }
        }*/
    }
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle).build());

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

}

