package nirays.com.airspy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import nirays.com.airspy.R;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(
                        R.layout.info_window_layout, null);
                TextView t = (TextView) v.findViewById(R.id.infocontent_tv_type);
                int n = 50;
                t.setText(n+"");
                v.setBackgroundColor(getColor(n));
                return v;
            }
        });
        //CameraUpdate center=
        //        CameraUpdateFactory.newLatLng(new LatLng(12,
        //                77));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

        //mMap.moveCamera(center);
        mMap.animateCamera(zoom);
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                MIN_TIME,
//                MIN_DISTANCE,
//                this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                LatLng lt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                // TODO Auto-generated method stub
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lt, 10);
                mMap.animateCamera(cameraUpdate);
                Marker m = mMap.addMarker(new MarkerOptions().position(lt).title("It's Me!"));
                m.showInfoWindow();
            }
        });
        return v;


    }
    private int getColor(int val) {
        String[] valRanges = getResources().getStringArray(R.array.ranges);
        String[] valRangeColor = getResources().getStringArray(R.array.rangeColors);
        int retColor = Color.parseColor("#db0000");
        for (int i = 0;i<valRanges.length;i++
                ) {
            int valRange = Integer.parseInt(valRanges[i]);
            if (val<= valRange){
                retColor = Color.parseColor(valRangeColor[i]);
                break;
            }
        }
        return retColor;
    }

}
