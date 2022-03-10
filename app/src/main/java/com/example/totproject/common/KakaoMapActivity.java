package com.example.totproject.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.totproject.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;


public class KakaoMapActivity extends AppCompatActivity implements MapView.MapViewEventListener
        , MapReverseGeoCoder.ReverseGeoCodingResultListener {

    MapPOIItem marker;
    TextView tv_find_location, tv_refresh_location;
    TextView tv_nowlocation;
    final int ADDR_REQ_CODE = 1001;
    MapView mapView;
    MapPoint mapPoints;
    double lac=0;
    double loc=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakaomap_act);
        mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        marker = new MapPOIItem();
        tv_nowlocation = findViewById(R.id.tv_nowlocation);
        tv_find_location = findViewById(R.id.tv_find_location);
        tv_refresh_location = findViewById(R.id.tv_refresh_location);
        mapPoints = MapPoint.mapPointWithGeoCoord(120, 120);
        mapView.setMapCenterPoint(mapPoints, true);
        mapView.setZoomLevel(2, true);
        marker.setTag(0);
        marker.setItemName("원하시는 위치를 지도에서 꾹 눌러주세요.");
        marker.setMapPoint(mapPoints);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.


        mapView.addPOIItem(marker);
        mapView.setMapViewEventListener(this);
        mapView.selectPOIItem(marker, true);
        tv_nowlocation.setText("CommonVal.defaultAddr");

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // 유저 위경도 세팅영역??
        Location location =  manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ? manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)  ;
        lac = location.getLatitude();
        loc = location.getLongitude();
        tv_find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_refresh_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(KakaoMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(KakaoMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location =  manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ? manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)  ;
                lac = location.getLatitude();
                loc = location.getLongitude();
                changeMarker(MapPoint.mapPointWithGeoCoord(lac ,loc));






            }
        });
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.imgv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });







    }//onCreate()



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==ADDR_REQ_CODE && resultCode == RESULT_OK){

            String addrs = data.getStringExtra("addr");
            Geocoder geocoder = new Geocoder(KakaoMapActivity.this);
            try {
                List<Address> list = null;
                list = geocoder.getFromLocationName(
                        addrs, // 지역 이름
                        10); // 읽을 개수
                if(list != null){

                    changeMarker( MapPoint.mapPointWithGeoCoord(list.get(0).getLatitude() , list.get(0).getLongitude()));
                    MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder("8925876f611b3b4ee7f7c9c830957654",
                            MapPoint.mapPointWithGeoCoord(list.get(0).getLatitude() , list.get(0).getLongitude()),
                            this,
                            KakaoMapActivity.this); reverseGeoCoder.startFindingAddress();
                }else{

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        changeMarker( mapPoint);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        changeMarker( mapPoint);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

       // CommonVal.defaultAddr = s ;
        //   marker.setItemName(CommonVal.defaultAddr);
        //tv_nowlocation.setText(s);
       // mapView.selectPOIItem(marker , true);
       // CommonVal.latitude = mapPoints.getMapPointGeoCoord().latitude;
       // CommonVal.longitude = mapPoints.getMapPointGeoCoord().longitude;
       // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(CommonVal.latitude , CommonVal.longitude) , true);
       // mapView.setZoomLevel(2, true);
       // marker.setTag(0);
       // marker.setMapPoint(mapPoints);
       // marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
       // marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
       // mapView.selectPOIItem(marker , true);

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    public void changeMarker(MapPoint mapPoint){

        mapPoints = mapPoint ;

        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder("8925876f611b3b4ee7f7c9c830957654",
                mapPoint,
                this,
                KakaoMapActivity.this); reverseGeoCoder.startFindingAddress();
        reverseGeoCoder.startFindingAddress();


        //marker.setDraggable(true);
    }


}