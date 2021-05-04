package com.example.AmateurShipper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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


import com.bumptech.glide.load.engine.Resource;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
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
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
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
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener, MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener {

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
    Resources.Theme theme;
    private static final String ICON_GEOJSON_SOURCE_ID = "geojson-icon-source-id";
    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final float POLYLINE_WIDTH = 5;
    private static final String CARLOS = "Carlos";
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static DirectionsRoute optimizedRoute;
    private static MapboxOptimization optimizedClient;
    private static final List<Point> stops = new ArrayList<>();
    private static Point origin_new;
    private PermissionsManager permissionsManager;
    private static LocationComponent locationComponent;
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
    Point origin = null;
    Point destination = null;
    private LocationChangeListeningActivityLocationCallback callback = new LocationChangeListeningActivityLocationCallback(this);

    int c = 0;
    double distance;
    static int a=0;
    String st;
    String startLocation = "";
    String endLocation = "";
    static double currentLat;
    static TextView tv;
    static double currentLong;
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
    Point p1 = Point.fromLngLat(108.21383599400859, 16.05116619419552);
    Point p2 = Point.fromLngLat(108.21731185477657, 16.067975595656062);
    Point p3 = Point.fromLngLat(108.2190854682684, 16.069171008171363);

//    public static final LatLng l1 = new LatLng(16.05116619419552, 108.21383599400859);
//    public static final LatLng l2 = new LatLng(16.067975595656062, 108.21731185477657);
//    public static final LatLng l3 = new LatLng(16.069171008171363, 108.2190854682684);


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
        PermissionsManager permissionsManager;
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
// Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
            Toast.makeText(getActivity(), " granted", Toast.LENGTH_SHORT).show();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
            Toast.makeText(getActivity(), "just granted", Toast.LENGTH_SHORT).show();
        }
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        confirmButton = view.findViewById(R.id.confirm);
        tv = view.findViewById(R.id.s);
        // Add the origin Point to the list
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmed();
            }
        });
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        stops.add(p1);
        stops.add(p2);
        stops.add(p3);
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
        // Cancel the directions API request
        if (optimizedClient != null) {
            optimizedClient.cancelCall();
        }
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }

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

    public void navigationRoute() {
        NavigationRoute.builder(getActivity())
                .accessToken("pk.eyJ1IjoidHJvbmd0aW4iLCJhIjoiY2tubGluaDk5MGk2MDJvcGJubXBmYjAybSJ9.iod8C2tfXJYSq3sA9ngCtA")
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {

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

    public void confirmed() {
        navigationRoute();

    }

    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(108.20944498264936, 16.059270410781075))
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .geometry(Point.fromLngLat(108.21731185566273, 16.067913737424842))
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
                // initializeLocationEngine();
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(currentLat, currentLong))
                        .zoom(18)
                        .tilt(13)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
//                Intent intent = new PlaceAutocomplete.IntentBuilder()
//                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ")
//                        .placeOptions(PlaceOptions.builder()
//                                .backgroundColor(Color.parseColor("#EEEEEE"))
//                                .limit(10)
//                                .addInjectedFeature(home)
//                                .addInjectedFeature(work)
//                                .build(PlaceOptions.MODE_CARDS))
//                        .build(getActivity());
                //startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }
    @SuppressLint("MissingPermission")
    public void initLocationEngine() {
        Log.i(TAG, "initLocationEngine: "+0);
        locationEngine = LocationEngineProvider.getBestLocationEngine(getActivity());
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper());
        locationEngine.getLastLocation(callback);


        Log.i(TAG, "initLocationEngine: " + locationEngine.toString());
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
// Set the origin location
                enableLocationComponent(style);
                addFirstStopToStopsList();
                //tv.setText(String.valueOf(currentLat) + "/" + String.valueOf(currentLong));
                // Add origin and destination to the mapboxMap
                initMarkerIconSymbolLayer(style);
                initOptimizedRouteLineLayer(style,"optimized-source-id"+a,"optimized-layer-id"+a);
                Log.i(TAG, "onStyleLoaded + onmapready: " + currentLong + "/" + currentLat + "=" + callback.toString());
//                Toast.makeText(getActivity(), R.string.click_instructions, Toast.LENGTH_SHORT).show();
//                mapboxMap.addOnMapClickListener(MapFragment.this::onMapClick);
//                mapboxMap.addOnMapLongClickListener(MapFragment.this::onMapClick);
                initSearchFab();
                //Toast.makeText(getActivity(), ""+locationComponent.getLastKnownLocation().getLatitude() + "/" + locationComponent.getLastKnownLocation().getLongitude(), Toast.LENGTH_SHORT).show();

                // addUserLocations();
//                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.motocross, null);
//                Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                // Add the symbol layer icon to map for future use
//                style.addImage(symbolIconId, mBitmap);
//                if (alreadyTwelveMarkersOnMap()) {
//                    Toast.makeText(getActivity(), "only_twelve_stops_allowed", Toast.LENGTH_LONG).show();
//                }
//                else {
                    if (style != null) {
                        Log.i(TAG, "onStyleLoaded: "+ 1);
                        for (int i = 0; i < stops.size(); i++) {
                            Log.i(TAG, "onStyleLoaded: " + stops);
                            Toast.makeText(getActivity(), "size" + stops.size(), Toast.LENGTH_SHORT).show();
                            LatLng latLng = new LatLng(stops.get(i).latitude(), stops.get(i).longitude());
                            addDestinationMarker(style, latLng);
                            // addPointToStopsList(latLng);
                            getOptimizedRoute(style, stops,"optimized-source-id"+a);
                        }
                    }
               // }
                // Create an empty GeoJSON source using the empty feature collection
                // setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                // setupLayer(style);
                // origin = Point.fromLngLat(108.18511468010284, 16.06491495661151);
                // destination = Point.fromLngLat( 108.21827671263680,16.058174650163377) ;
                // initSource(style);

                //initLayers(style);
//                CameraPosition position = new CameraPosition.Builder()
//                        .target(new LatLng(destination.latitude(), destination.longitude()))
//                        .zoom(18)
//                        .tilt(13)
//                        .build();
//                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
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
                            //reverseGeocodeFunc(point,c);


                        }
                        if (c == 1) {
                            destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                            // getRoute(mapboxMap, origin, destination);
                            MarkerOptions markerOptions2 = new MarkerOptions();
                            markerOptions2.position(point);
                            markerOptions2.title("destination");
                            mapboxMap.addMarker(markerOptions2);
                            reverseGeocodeFunc(point, c);
                            //getRoute(mapboxMap, origin, destination);
                            // double d = point.distanceTo(source);


                        }
                        if (c > 1) {
                            c = 0;
                        }
                        c++;
                        return true;
                    }
                });
                //              getRoute(mapboxMap, origin, destination);
            }
        });
    }


    private void initMarkerIconSymbolLayer(@NonNull Style loadedMapStyle) {
// Add the LineLayer to the map. This layer will display the directions route.// Add the marker image to
        loadedMapStyle.addImage("icon-image", Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.motocross, theme))));

// Add the source to the map
        loadedMapStyle.addSource(new GeoJsonSource(ICON_GEOJSON_SOURCE_ID,
                Feature.fromGeometry(Point.fromLngLat(origin_new.longitude(), origin_new.latitude()))));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_GEOJSON_SOURCE_ID).withProperties(
                iconImage("icon-image"),
                iconSize(1f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                iconOffset(new Float[]{0f, -7f})
        ));
    }
    private static void initOptimizedRouteLineLayer(@NonNull Style loadedMapStyle, String source, String layer) {
        loadedMapStyle.addSource(new GeoJsonSource(source));
        loadedMapStyle.addLayerBelow(new LineLayer(layer, source)
                .withProperties(
                        lineColor(Color.parseColor(TEAL_COLOR)),
                        lineWidth(POLYLINE_WIDTH)
                ), ICON_LAYER_ID);
    }

    private void reverseGeocodeFunc(LatLng point, int c) {
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
                    feature = results.get(0);
                    if (c == 0) {
                        startLocation += feature.placeName();
                        startLocation = startLocation.replace(", Dhaka, Bangladesh", ".");
                        //TextView tv = getActivity().findViewById(R.id.s);
                        //tv.setText(startLocation);

                    }
                    if (c == 1) {
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
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    @SuppressLint("ResourceType")
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
//        loadedMapStyle.addImage(RED_PIN_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.blue_marker, theme))));

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -9f})));
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
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                   // addFirstStopToStopsList();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }
    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapFragment> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(MapFragment activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @SuppressLint("StringFormatInvalid")
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapFragment activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    Toast.makeText(activity.getContext(), "Location on null" + location.getLatitude() + "/" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Toast which displays the new location's coordinates
                Toast.makeText(activity.getActivity(), String.format(activity.getActivity().getString(R.string.new_location)+
                                String.valueOf(result.getLastLocation().getLatitude())),
                        Toast.LENGTH_LONG).show();

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    a++;
                    // Log.i(TAG, "onSuccessMAOOOOOOOO: "+result.getLastLocation());
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    currentLat = locationComponent.getLastKnownLocation().getLatitude();
                    currentLong = locationComponent.getLastKnownLocation().getLongitude();
                    //stops.remove(stops.size()-1);
                    //addFirstStopToStopsList();
                   // initOptimizedRouteLineLayer(activity.mapboxMap.getStyle(),"optimized-source-id"+a,"optimized-layer-id"+a);
                    Log.i(TAG, "so luong: "+stops.size());
                    //locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
                    // Set the component's render mode
                    //locationComponent.setRenderMode(RenderMode.COMPASS);
                    tv.setText(locationComponent.getLastKnownLocation().getLatitude()+"");
                    //Log.i(TAG, "onSuccess: " + locationComponent.getLastKnownLocation().getLatitude());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            MapFragment activity = activityWeakReference.get();
            if (activity != null) {

            }
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
             locationComponent = mapboxMap.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();
            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
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
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            initLocationEngine();
            // locationComponent.getLocationEngine().getLastLocation(callback);
//            currentLat = locationComponent.getLastKnownLocation().getLatitude();
//            currentLong = locationComponent.getLastKnownLocation().getLongitude();
            //Toast.makeText(getActivity().getApplicationContext(), "lat" + currentLat + "/" + "long" + currentLong, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onCameraTrackingChanged: " + currentLat + "-" + currentLong);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    /**
     * Called when the user clicks on the map view.
     *
     * @param point The projected map coordinate the user clicked on.
     * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
     * false otherwise.
     */
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        // Optimization API is limited to 12 coordinate sets

        return true;
    }

    /**
     * Called when the user long clicks on the map view.
     *
     * @param point The projected map coordinate the user long clicked on.
     * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
     * false otherwise.
     */
    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
        stops.clear();
        if (mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                resetDestinationMarkers(style);
                removeOptimizedRoute(style);
                //addFirstStopToStopsList();
                return true;
            }
        }
        return false;
    }
    private void resetDestinationMarkers(@NonNull Style style) {
        GeoJsonSource optimizedLineSource = style.getSourceAs(ICON_GEOJSON_SOURCE_ID);
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(Point.fromLngLat(origin_new.longitude(), origin_new.latitude()));
        }
    }

    private static void removeOptimizedRoute(@NonNull Style style) {
        GeoJsonSource optimizedLineSource = style.getSourceAs("optimized-route-source-id");
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {}));
        }
    }

    private boolean alreadyTwelveMarkersOnMap() {
        return stops.size() == 12;
    }

    private static void addDestinationMarker(@NonNull Style style, LatLng point) {
        List<Feature> destinationMarkerList = new ArrayList<>();
        for (Point singlePoint : stops) {
            destinationMarkerList.add(Feature.fromGeometry(
                    Point.fromLngLat(singlePoint.longitude(), singlePoint.latitude())));
        }
        destinationMarkerList.add(Feature.fromGeometry(Point.fromLngLat(point.getLongitude(), point.getLatitude())));
        GeoJsonSource iconSource = style.getSourceAs(ICON_GEOJSON_SOURCE_ID);
        if (iconSource != null) {
            iconSource.setGeoJson(FeatureCollection.fromFeatures(destinationMarkerList));
        }
    }

    private void addPointToStopsList(LatLng point) {
        stops.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
    }

    private static void addFirstStopToStopsList() {
// Set first stop
        origin_new = Point.fromLngLat(currentLong,currentLat);
        //Toast.makeText(getActivity(), ""+currentLat + "/" + currentLong, Toast.LENGTH_SHORT).show();
        stops.add(origin_new);
    }

    private static void getOptimizedRoute(@NonNull final Style style, List<Point> coordinates, String source) {
        optimizedClient = MapboxOptimization.builder()
                .source(FIRST)
                .destination(ANY)
                .coordinates(coordinates)
                .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoidHJvbmd0aW4iLCJhIjoiY2tubGluaDk5MGk2MDJvcGJubXBmYjAybSJ9.iod8C2tfXJYSq3sA9ngCtA")
                .build();

        optimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
                if (!response.isSuccessful()) {
//                    Timber.d(getString(R.string.no_success));
//                    Toast.makeText(getContext(), R.string.no_success, Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body() != null) {
                        List<DirectionsRoute> routes = response.body().trips();
                        if (routes != null) {
                            if (routes.isEmpty()) {
//                                Timber.d("%s size = %s", getString(R.string.successful_but_no_routes), routes.size());
//                                Toast.makeText(getActivity(), R.string.successful_but_no_routes,
//                                        Toast.LENGTH_SHORT).show();
                            } else {
// Get most optimized route from API response
                                optimizedRoute = routes.get(0);
                                drawOptimizedRoute(style, optimizedRoute,source);

                            }
                        } else {
//                            Timber.d("list of routes in the response is null");
//                            Toast.makeText(getActivity(), String.format(getString(R.string.null_in_response),
//                                    "The Optimization API response's list of routes"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        Timber.d("response.body() is null");
//                        Toast.makeText(getActivity(), String.format(getString(R.string.null_in_response),
//                                "The Optimization API response's body"), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
            }
        });
    }

    private static void drawOptimizedRoute(@NonNull Style style, DirectionsRoute route, String source) {
        GeoJsonSource optimizedLineSource = style.getSourceAs(source);
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                    LineString.fromPolyline(route.geometry(), PRECISION_6))));
        }
    }
}
