package com.example.AmateurShipper;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.bumptech.glide.load.DataSource;
import com.example.AmateurShipper.Adapter.MapAdapter;
import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;
import com.example.AmateurShipper.MainActivity;
import com.example.AmateurShipper.Model.PlaceObject;
//import com.example.AmateurShipper.Util.StickyRecyclerView;
import com.example.AmateurShipper.PostObject;
import com.example.AmateurShipper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.annotations.IconFactory;
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
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
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
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.geojson.Point.fromLngLat;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

import java.lang.ref.WeakReference;

import com.example.AmateurShipper.Interface.statusInterfaceRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, Callback<DirectionsResponse>, PermissionsListener, MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener, statusInterfaceRecyclerView, ReceivedOrderAdapter.OnReceivedOderListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String ROUTE_SOURCE_ID = "ROUTE-SOURCE-ID";

    private static final String ICON_LAYER_ID = "icon-layer-id";


    int storedPosition = 0;


    Resources.Theme theme;
    private static final String ICON_GEOJSON_SOURCE_ID = "geojson-icon-source-id";
    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String FIRST_shop = "first-shop";
    private static final String ANY_shop = "any-shop";
    private static final String FIRST_user = "first-user";
    private static final String ANY_user = "any-user";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final String BLUE_COLOR = "#FF3C4673";
    private static final float POLYLINE_WIDTH = 5;
    private static final String CARLOS = "Carlos";
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 2;
    private DirectionsRoute optimizedRoute;
    private MapboxOptimization optimizedClient;
    private static final List<Point> stops = new ArrayList<>();
    private static final List<Point> shop_lists = new ArrayList<>();
    private static final List<Point> user_lists = new ArrayList<>();
    private Point origin_new;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    private CarmenFeature home;
    private CarmenFeature work;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";


    ArrayList<PlaceObject> locationObjects = new ArrayList<>();
    ArrayList<PlaceObject> locationAfterSort = new ArrayList<>();
    //to get location permissions.

    Point origin = null;
    Point destination = null;
    private LocationChangeListeningActivityLocationCallback callback = new LocationChangeListeningActivityLocationCallback(this);
    double distance;
    double currentLat;
    TextView tv;
    double currentLong;
    MapView mapView;
    MapboxMap mapboxMap;
    int key = 0;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocationEngine locationEngine;
    RecyclerView recyclerview_map;
    MainActivity mainActivity;
    private DatabaseReference mDatabase;
    List<PostObject> mData = new ArrayList<>();
    MapAdapter mapAdapter;

    String iDUser;
    FragmentManager fragmentManager;
    FragmentManager fm;
    String[] Colors = {
            "#536DFE",
            "#FFFF00",
            "#00FFFF",
            "#FF00FF",
            "#0084FF",
            "#F2C94C",
            "#11CFC5",
            " #e91e63",
            "#FF0000",
            "#00FF00",
            "#0000FF",
            "#303963",
            "#3C4673"
    };
    int[] locations_shop = {
            R.drawable.shop1,
            R.drawable.shop2,
            R.drawable.shop3,
            R.drawable.shop4,
            R.drawable.shop5
    };
    int[] locations_user = {
            R.drawable.user1,
            R.drawable.user2,
            R.drawable.user3,
            R.drawable.user4,
            R.drawable.user5
    };

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
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        recyclerview_map = view.findViewById(R.id.recyclerview_map);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        fm = getActivity().getSupportFragmentManager();
        getUid();
        mainActivity = (MainActivity) getActivity();
        mainActivity.setCountOrder(0);
        mainActivity.disableNotification();
        if (mData != null) {
            mData.clear();
        }
        getListStatusReceived();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_map.setHasFixedSize(true);
        recyclerview_map.setLayoutManager(mLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerview_map);

        // Add the origin Point to the list

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        stops.clear();
        shop_lists.clear();


        return view;
    }

    public void createNewAdapter() {
        mapAdapter = new MapAdapter(mData, MapFragment.this, fm, this);
    }
    public void getListStatusReceived() {
        mDatabase.child("received_order_status").child(iDUser).orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i(TAG, "onDataChange snapshot:  snapshot ton tai");

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostObject data = dataSnapshot.getValue(PostObject.class);
                        mData.add(data);
                    }
                    createNewAdapter();
                    recyclerview_map.setAdapter(mapAdapter);
                    mapAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "khong the load", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Load ID User
    public void getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        iDUser = user.getUid();
    }
    //clear map styles
    public void clearStyle(int position) {
        //shop
        mapboxMap.getStyle().removeLayer("LAYER_ID_SHOP_" + position);
        mapboxMap.getStyle().removeLayer(" ICON_LAYER_ID_SHOP_" + position);
        mapboxMap.getStyle().removeSource("OPTIMIZED_ROUTE_SOURCE_ID_SHOP_" + position);
        mapboxMap.getStyle().removeSource("ICON_GEOJSON_SOURCE_ID_SHOP_" + position);
        removeOptimizedRoute(mapboxMap.getStyle(), "OPTIMIZED_ROUTE_SOURCE_ID_SHOP_" + position);
        mapboxMap.getStyle().removeImage("ICON_IMAGE_SHOP_" + position);
        mapboxMap.getStyle().removeLayer("DESTINATION_LAYER_ID_SHOP_" + position);
        mapboxMap.getStyle().removeSource("DESTINATION_SOURCE_ID_SHOP_" + position);
        mapboxMap.getStyle().removeSource("FEATURE_PROPERTY_KEY_SHOP_" + position);
        //customer
        mapboxMap.getStyle().removeLayer("LAYER_ID_USER_" + position);
        mapboxMap.getStyle().removeLayer(" ICON_LAYER_ID_USER_" + position);
        mapboxMap.getStyle().removeSource("OPTIMIZED_ROUTE_SOURCE_ID_USER_" + position);
        mapboxMap.getStyle().removeSource("ICON_GEOJSON_SOURCE_ID_USER_" + position);
        removeOptimizedRoute(mapboxMap.getStyle(), "OPTIMIZED_ROUTE_SOURCE_ID_USER_" + position);
        mapboxMap.getStyle().removeImage("ICON_IMAGE_USER_" + position);
        mapboxMap.getStyle().removeLayer("DESTINATION_LAYER_ID_USER_" + position);
        mapboxMap.getStyle().removeSource("DESTINATION_SOURCE_ID_USER_" + position);
        mapboxMap.getStyle().removeSource("FEATURE_PROPERTY_KEY_USER_" + position);
        mapboxMap.clear();
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
    private void initSearchFab() {
        getActivity().findViewById(R.id.get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(currentLat, currentLong))
                        .zoom(18)
                        .tilt(13)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
            }
        });
        getActivity().findViewById(R.id.get_overview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceObject currentLatLng= new PlaceObject(currentLat, currentLong, -1, 2, 0);
                locationAfterSort.set(0,currentLatLng);
                for (int i = 1; i < locationAfterSort.size(); i++) {
                    Log.i(TAG, "onCreateView: " + locationAfterSort.get(i).getType() + "/" + locationAfterSort.get(i).getDiem_thu());
                    if (locationAfterSort.get(i).getType() == 1) {
                        get_route_options_shop("ICON_IMAGE_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                "OPTIMIZED_ROUTE_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                "ICON_GEOJSON_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                " ICON_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                i, "LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                "DESTINATION_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                "DESTINATION_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                " FEATURE_PROPERTY_KEY_SHOP_" + locationAfterSort.get(i).getDiem_thu(), i,
                                fromLngLat(locationAfterSort.get(i - 1).getLongitude(), locationAfterSort.get(i - 1).getLatitude()), locationAfterSort.get(i).getDiem_thu() - 1);
                    } else if (locationAfterSort.get(i).getType() == 2) {
                        get_route_options_user("ICON_IMAGE_USER_" + locationAfterSort.get(i).getDiem_thu(), "OPTIMIZED_ROUTE_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(), "ICON_GEOJSON_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                " ICON_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                i, "LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                "DESTINATION_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                "DESTINATION_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                " FEATURE_PROPERTY_KEY_USER_" + locationAfterSort.get(i).getDiem_thu(), i,
                                fromLngLat(locationAfterSort.get(i - 1).getLongitude(), locationAfterSort.get(i - 1).getLatitude()), i - 1, locationAfterSort.get(i).getDiem_thu() - 1);
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void initLocationEngine() {
        Log.i(TAG, "initLocationEngine: " + 0);
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
                mDatabase.child("received_order_status").child(iDUser).orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            locationObjects.add(new PlaceObject(currentLat, currentLong, -1, 2, 0));
                            int diem = 1;
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                double receive_lat = Double.parseDouble(snap.child("receiveLat").getValue(String.class));
                                double receive_lng = Double.parseDouble(snap.child("receiveLng").getValue(String.class));
                                double ship_lat = Double.parseDouble(snap.child("shipLat").getValue(String.class));
                                double ship_lng = Double.parseDouble(snap.child("shipLng").getValue(String.class));
                                locationObjects.add(new PlaceObject(receive_lat, receive_lng,1, 2, diem));
                                locationObjects.add(new PlaceObject(ship_lat, ship_lng, 2, 0, diem));
                                diem++;
                            }
                            for (int i = 0; i < locationObjects.size(); i++)
                                Log.i(TAG, "onDataChange: " + locationObjects.get(i).getLatitude() + "/" + locationObjects.get(i).getLongitude());
                            enableLocationComponent(style);
                            Log.i(TAG, "onStyleLoaded + onmapready: " + currentLong + "/" + currentLat + "=" + callback.toString());
                            mapboxMap.addOnMapClickListener(MapFragment.this::onMapClick);
                            mapboxMap.addOnMapLongClickListener(MapFragment.this::onMapClick);
                            initSearchFab();

                            if (alreadyTwelveMarkersOnMap()) {
                                Toast.makeText(getActivity(), R.string.only_twelve_stops_allowed, Toast.LENGTH_LONG).show();
                            } else {
//                    locationObjects.add(new PlaceObject(currentLat, currentLong, -1, 2, 0));
//                    locationObjects.add(new PlaceObject(16.06631377702285, 108.20579592212839, 1, 2, 1));
//                    locationObjects.add(new PlaceObject(16.066396255320264, 108.20515219196177, 1, 2, 2));
//                    locationObjects.add(new PlaceObject(16.067231346155886, 108.2073086880199, 1, 2, 3));
//                    locationObjects.add(new PlaceObject(16.06782931027943, 108.20788804516984, 2, 0, 1));
//                    locationObjects.add(new PlaceObject(16.05116619419552, 108.21383599400859, 2, 0, 2));
//                    locationObjects.add(new PlaceObject(16.067975595656062, 108.21731185477657, 2, 0, 3));
                                int luu = 0;
                                for (int i = 0; i < locationObjects.size(); i++) {
                                    LatLng shop1 = new LatLng(locationObjects.get(i).getLatitude(), locationObjects.get(i).getLongitude());
                                    LatLng shop2 = new LatLng(locationObjects.get(i + 1).getLatitude(), locationObjects.get(i + 1).getLongitude());
                                    distance = shop1.distanceTo(shop2);
                                    for (int j = i + 1; j < locationObjects.size(); j++) {
                                        LatLng from = new LatLng(locationObjects.get(i).getLatitude(), locationObjects.get(i).getLongitude());
                                        if (locationObjects.get(j).getCheck() != 0) {

                                            Log.i(TAG, "onStyleLoaded+++++: " + distance);
                                            LatLng to = new LatLng(locationObjects.get(j).getLatitude(), locationObjects.get(j).getLongitude());
                                            double distanceTo = from.distanceTo(to);
                                            if (distanceTo <= distance) {
                                                distance = distanceTo;
                                                luu = j;
                                            }
                                        }
                                    }
                                    Log.i(TAG, "Checkk: " + luu);
                                    if (locationObjects.size() == 2)
                                        luu--;
                                    if (locationObjects.get(luu).getType() == 1) {
                                        Log.i(TAG, "AAA: chay" + locationObjects.get(luu).getDiem_thu() + "/" + locationObjects.get(luu).getType());
                                        for (int ch = 0; ch < locationObjects.size(); ch++) {
                                            if (locationObjects.get(ch).getType() == 2 &&
                                                    locationObjects.get(ch).getDiem_thu() == locationObjects.get(luu).getDiem_thu()) {
                                                locationObjects.get(ch).setCheck(1);
                                            }
                                        }
                                    } else
                                        Log.i(TAG, "BBB: chay" + locationObjects.get(luu).getDiem_thu() + "/" + locationObjects.get(luu).getType());
                                    locationAfterSort.add(locationObjects.get(i));
                                    locationObjects.set(0, locationObjects.get(luu));
                                    locationObjects.remove(luu);
                                    //Collections.swap(locationShopObjects,0,luu);
                                    i--;
                                    if (locationObjects.size() == 1) {
                                        locationAfterSort.add(locationObjects.get(0));
                                        break;
                                    }
                                }
                                for (int i = 1; i < locationAfterSort.size(); i++) {
                                    Log.i(TAG, "onCreateView: " + locationAfterSort.get(i).getType() + "/" + locationAfterSort.get(i).getDiem_thu());
                                    if (locationAfterSort.get(i).getType() == 1) {
                                        get_route_options_shop("ICON_IMAGE_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                "OPTIMIZED_ROUTE_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                "ICON_GEOJSON_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                " ICON_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                i, "LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                "DESTINATION_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                "DESTINATION_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                                                " FEATURE_PROPERTY_KEY_SHOP_" + locationAfterSort.get(i).getDiem_thu(), i,
                                                fromLngLat(locationAfterSort.get(i - 1).getLongitude(), locationAfterSort.get(i - 1).getLatitude()), locationAfterSort.get(i).getDiem_thu() - 1);
                                    } else if (locationAfterSort.get(i).getType() == 2) {
                                        get_route_options_user("ICON_IMAGE_USER_" + locationAfterSort.get(i).getDiem_thu(), "OPTIMIZED_ROUTE_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(), "ICON_GEOJSON_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                                " ICON_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                                i, "LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                                "DESTINATION_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                                "DESTINATION_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                                                " FEATURE_PROPERTY_KEY_USER_" + locationAfterSort.get(i).getDiem_thu(), i,
                                                fromLngLat(locationAfterSort.get(i - 1).getLongitude(), locationAfterSort.get(i - 1).getLatitude()), i - 1, locationAfterSort.get(i).getDiem_thu() - 1);
                                    }
                                }
                            }
                        }else
                            Log.i(TAG, "onDataChange: XUI");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void get_route_options_shop(String image_shop, String source_shop, String geojson_source_shop,
                                       String icon_layer_id_shop,
                                       int start_index, String layer_id_bottom_shop,
                                       String destination_layer_shop, String destination_source_shop,
                                       String feature_properties_shop, int color, Point start_location, int getDiemThu) {
        //init Route and Marker for shop and user
        initMarkerIconSymbolLayer_shop(mapboxMap.getStyle(), image_shop, geojson_source_shop, icon_layer_id_shop, start_index, getDiemThu);
        initOptimizedRouteLineLayer_shop(mapboxMap.getStyle(), source_shop, layer_id_bottom_shop, icon_layer_id_shop, color);
        Point destination = fromLngLat(locationAfterSort.get(start_index).getLongitude(), locationAfterSort.get(start_index).getLatitude());
        getRoute_shop(start_location, destination, source_shop);

    }

    public void get_single_route_options_shop(String image_shop, String source_shop, String geojson_source_shop,
                                              String icon_layer_id_shop,
                                              int start_index, String layer_id_bottom_shop,
                                              String destination_layer_shop, String destination_source_shop,
                                              String feature_properties_shop, int color, Point start_location, int getDiemThu) {
        //init Route and Marker for shop and user

        initMarkerIconSymbolLayer_shop(mapboxMap.getStyle(), image_shop, geojson_source_shop, icon_layer_id_shop, start_index, getDiemThu);
        initOptimizedRouteLineLayer_shop(mapboxMap.getStyle(), source_shop, layer_id_bottom_shop, icon_layer_id_shop, color);
        Point destination = fromLngLat(locationAfterSort.get(start_index).getLongitude(), locationAfterSort.get(start_index).getLatitude());
        getRoute_shop(start_location, destination, source_shop);

    }

    public void get_route_options_user(String image_user,
                                       String geojson_source_user, String icon_layer_id_user,
                                       String source_user, int start_index,
                                       String layer_id_bottom_user,
                                       String destination_layer_user, String destination_source_user,
                                       String feature_properties_user, int color, Point end, int end_index, int getIdemThu) {
        //init Route and Marker for shop and user
        initMarkerIconSymbolLayer_user(mapboxMap.getStyle(), image_user, geojson_source_user, icon_layer_id_user, start_index, getIdemThu);
        initOptimizedRouteLineLayer_user(mapboxMap.getStyle(), source_user, layer_id_bottom_user, icon_layer_id_user, color);
        Point start = fromLngLat(locationAfterSort.get(start_index).getLongitude(), locationAfterSort.get(start_index).getLatitude());
        getRoute_user(start, end, source_user);
    }

    public void clearStyle(String source, String layer, String geojson_source, String icon_layer, String image, String icon_layer_shop) {
        removeOptimizedRoute(mapboxMap.getStyle(), source);
        mapboxMap.getStyle().removeLayer(layer);
        mapboxMap.getStyle().removeLayer(icon_layer);
        mapboxMap.getStyle().removeLayer(icon_layer_shop);
        mapboxMap.getStyle().removeSource(source);
        mapboxMap.getStyle().removeSource(geojson_source);
        mapboxMap.getStyle().removeImage(image);
        mapboxMap.clear();
    }

    public void clearAllStyle() {
        Log.i(TAG, "clearAllStyle: divide" + locationAfterSort.size() / 2);
        for (int i = 1; i <= locationAfterSort.size() / 2; i++) {
            clearStyle(i);
        }
    }

    public void get_single_route_options_user(String image_user,
                                              String geojson_source_user, String icon_layer_id_user,
                                              String source_user, int start_index,
                                              String layer_id_bottom_user,
                                              String destination_layer_user, String destination_source_user,
                                              String feature_properties_user, int color, Point end, int end_index, int getDiemThu) {
        initMarkerIconSymbolLayer_user(mapboxMap.getStyle(), image_user, geojson_source_user, icon_layer_id_user, end_index, getDiemThu);
        initOptimizedRouteLineLayer_user(mapboxMap.getStyle(), source_user, layer_id_bottom_user, icon_layer_id_user, color);
        Point start = fromLngLat(locationAfterSort.get(start_index).getLongitude(), locationAfterSort.get(start_index).getLatitude());
        getRoute_user(start, end, source_user);
    }

    public static com.mapbox.mapboxsdk.annotations.Icon drawableToIcon(@NonNull Context context, @DrawableRes int id, @ColorInt int colorRes) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, colorRes);
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    private void initMarkerIconSymbolLayer_shop(@NonNull Style loadedMapStyle, String name, String geojson_source_shop, String icon_layer_id_shop, int index, int getDiemThu) {
        // Add the LineLayer to the map. This layer will display the directions route.// Add the marker image to
        loadedMapStyle.removeImage(name);
        loadedMapStyle.removeLayer(icon_layer_id_shop);
        loadedMapStyle.removeSource(geojson_source_shop);
        loadedMapStyle.addImage(name, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(locations_shop[getDiemThu], theme))));
        // Add the source to the map
        loadedMapStyle.addSource(new GeoJsonSource(geojson_source_shop,
                Feature.fromGeometry(fromLngLat(locationAfterSort.get(index).getLongitude(), locationAfterSort.get(index).getLatitude()))));
        loadedMapStyle.addLayer(new SymbolLayer(icon_layer_id_shop, geojson_source_shop).withProperties(
                iconImage(name),
                iconSize(1f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                iconOffset(new Float[]{0f, -7f})
        ));
    }

    private void initMarkerIconSymbolLayer_user(@NonNull Style loadedMapStyle, String name, String icon_layer_id_user, String icon_geojson_source_id_user_, int index, int getDiemthu) {
        loadedMapStyle.removeImage(name);
        loadedMapStyle.removeLayer(icon_layer_id_user);
        loadedMapStyle.removeSource(icon_geojson_source_id_user_);
        loadedMapStyle.addImage(name,
                Objects.requireNonNull(
                        BitmapUtils.getBitmapFromDrawable(
                                getResources().getDrawable(locations_user[getDiemthu], theme))));
        loadedMapStyle.addSource(new GeoJsonSource(icon_geojson_source_id_user_,
                Feature.fromGeometry(fromLngLat(locationAfterSort.get(index).getLongitude(), locationAfterSort.get(index).getLatitude()))
        ));
        loadedMapStyle.addLayer(new SymbolLayer(icon_layer_id_user, icon_geojson_source_id_user_).withProperties(
                iconImage(name),
                iconSize(1f),
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                iconOffset(new Float[]{0f, -7f})
        ));

    }

    private void initOptimizedRouteLineLayer_user(@NonNull Style loadedMapStyle, String id, String layerId, String icon_layer_id_user, int color) {
        loadedMapStyle.removeLayer(layerId);
        loadedMapStyle.removeSource(id);
        loadedMapStyle.addSource(new GeoJsonSource(id));
        loadedMapStyle.addLayerBelow(new LineLayer(layerId, id)
                .withProperties(
                        lineColor(Color.parseColor(Colors[color])),
                        lineWidth(POLYLINE_WIDTH)
                ), icon_layer_id_user);
    }

    private void initOptimizedRouteLineLayer_shop(@NonNull Style loadedMapStyle, String source_id, String layerId, String icon_layer_id_shop, int color) {
        loadedMapStyle.removeLayer(layerId);
        loadedMapStyle.removeSource(source_id);
        loadedMapStyle.addSource(new GeoJsonSource(source_id));
        loadedMapStyle.addLayerBelow(new LineLayer(layerId, source_id)
                .withProperties(
                        lineColor(Color.parseColor(Colors[color])),
                        lineWidth(POLYLINE_WIDTH)
                ), icon_layer_id_shop);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.requestLocationPermissions(getActivity());
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getRoute_user(Point start, Point destination, String source_id) {
        removeOptimizedRoute(mapboxMap.getStyle(), source_id);
        MapboxDirections client = MapboxDirections.builder()
                .origin(start)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Timber.d("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.d("No routes found");
                    return;
                }

                drawOptimizedRoute_user(mapboxMap.getStyle(), response.body().routes().get(0), source_id);
                Toast.makeText(getActivity(), "distanace1" + response.body().routes().get(0).distance(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
                if (!throwable.getMessage().equals("Coordinate is invalid: 0,0")) {
                    Toast.makeText(getActivity(),
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRoute_shop(Point currentLocation, Point destination, String source_id) {
        removeOptimizedRoute(mapboxMap.getStyle(), source_id);
        MapboxDirections client = MapboxDirections.builder()
                .origin(currentLocation)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Timber.d("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.d("No routes found");
                    return;
                }
                drawOptimizedRoute_shop(mapboxMap.getStyle(), response.body().routes().get(0), source_id);
                Toast.makeText(getActivity(), "distanace1" + response.body().routes().get(0).distance(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
                if (!throwable.getMessage().equals("Coordinate is invalid: 0,0")) {
                    Toast.makeText(getActivity(),
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    addFirstStopToStopsList();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onItemClick(int position) {
        clearAllStyle();
        clearStyle(storedPosition);
        position += 1;
        storedPosition = position;
        Log.i(TAG, "Stored postion" + position + "///////////////////////////////" + "ICON_GEOJSON_SOURCE_ID_SHOP_" + storedPosition);
        for (int i = 1; i < locationAfterSort.size(); i++) {
            Log.i(TAG, "onCreateView: " + locationAfterSort.get(i).getType() + "/" + locationAfterSort.get(i).getDiem_thu() + "position: " + position);
            if (locationAfterSort.get(i).getType() == 1 && locationAfterSort.get(i).getDiem_thu() == position) {
                get_single_route_options_shop("ICON_IMAGE_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        "OPTIMIZED_ROUTE_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        "ICON_GEOJSON_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        " ICON_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        i, "LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        "DESTINATION_LAYER_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        "DESTINATION_SOURCE_ID_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        " FEATURE_PROPERTY_KEY_SHOP_" + locationAfterSort.get(i).getDiem_thu(),
                        i, fromLngLat(currentLong, currentLat), locationAfterSort.get(i).getDiem_thu() - 1);
                key = i;

            } else if (locationAfterSort.get(i).getType() == 2 && locationAfterSort.get(i).getDiem_thu() == position) {
                get_single_route_options_user("ICON_IMAGE_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        "OPTIMIZED_ROUTE_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        "ICON_GEOJSON_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        " ICON_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        key, "LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        "DESTINATION_LAYER_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        "DESTINATION_SOURCE_ID_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        " FEATURE_PROPERTY_KEY_USER_" + locationAfterSort.get(i).getDiem_thu(),
                        key, fromLngLat(locationAfterSort.get(i).getLongitude(), locationAfterSort.get(i).getLatitude()), i,
                        locationAfterSort.get(i).getDiem_thu() - 1);
            }
        }
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onReceivedItem(int position) {

    }

    private class LocationChangeListeningActivityLocationCallback
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

                if (activity.mapboxMap != null && result.getLastLocation() != null) {

                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());

                    currentLat = locationComponent.getLastKnownLocation().getLatitude();
                    currentLong = locationComponent.getLastKnownLocation().getLongitude();

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
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            initLocationEngine();
            Log.i(TAG, "onCameraTrackingChanged: " + currentLat + "-" + currentLong);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
        // tv.setText(currentLat + "");
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
        return false;
    }
    private void removeOptimizedRoute(@NonNull Style style, String source_id) {
        GeoJsonSource optimizedLineSource = style.getSourceAs(source_id);
        if (optimizedLineSource != null) {
            optimizedLineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{}));
        }
    }

    private boolean alreadyTwelveMarkersOnMap() {
        return stops.size() == 12;
    }

    private void addDestinationMarker(@NonNull Style style, LatLng point) {
        List<Feature> destinationMarkerList = new ArrayList<>();
        for (Point singlePoint : stops) {
            destinationMarkerList.add(Feature.fromGeometry(
                    fromLngLat(singlePoint.longitude(), singlePoint.latitude())));
        }
        destinationMarkerList.add(Feature.fromGeometry(fromLngLat(point.getLongitude(), point.getLatitude())));
        GeoJsonSource iconSource = style.getSourceAs(ICON_GEOJSON_SOURCE_ID);
        SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id")
                .withProperties(PropertyFactory.textField(Expression.get("FEATURE-PROPERTY-KEY")));
        if (iconSource != null) {
            iconSource.setGeoJson(FeatureCollection.fromFeatures(destinationMarkerList));
        }
    }

    private void addDestinationMarker_shop(@NonNull Style style, LatLng point, String layerID_shop, String sourceID_shop, String feature_property_key_shop) {
        List<Feature> destinationMarkerList = new ArrayList<>();
        for (Point singlePoint : shop_lists) {
            destinationMarkerList.add(Feature.fromGeometry(
                    fromLngLat(singlePoint.longitude(), singlePoint.latitude())));
        }
        destinationMarkerList.add(Feature.fromGeometry(fromLngLat(point.getLongitude(), point.getLatitude())));
        GeoJsonSource iconSource = style.getSourceAs(ICON_GEOJSON_SOURCE_ID);
        SymbolLayer symbolLayer = new SymbolLayer(layerID_shop, sourceID_shop)
                .withProperties(PropertyFactory.textField(Expression.get(feature_property_key_shop)));
        if (iconSource != null) {
            iconSource.setGeoJson(FeatureCollection.fromFeatures(destinationMarkerList));
        }
    }
    private void addFirstStopToStopsList() {
        // Set first stop
        origin_new = fromLngLat(currentLong, currentLat);
        Toast.makeText(getActivity(), "" + currentLat + "/" + currentLong, Toast.LENGTH_SHORT).show();
        stops.add(origin_new);
    }


    private void drawOptimizedRoute_shop(@NonNull Style style, DirectionsRoute route, String sourceId) {

        GeoJsonSource optimizedLineSource = style.getSourceAs(sourceId);
        optimizedLineSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                LineString.fromPolyline(route.geometry(), PRECISION_6))));
    }

    private void drawOptimizedRoute_user(@NonNull Style style, DirectionsRoute route, String sourceId) {

        GeoJsonSource optimizedLineSource = style.getSourceAs(sourceId);
        optimizedLineSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                LineString.fromPolyline(route.geometry(), PRECISION_6))));
    }

}