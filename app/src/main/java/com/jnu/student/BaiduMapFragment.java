package com.jnu.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaiduMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaiduMapFragment extends Fragment {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    public BaiduMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaiduMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaiduMapFragment newInstance(String param1, String param2) {
        BaiduMapFragment fragment = new BaiduMapFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        //获取地图控件引用
        mMapView = (MapView) rootView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 添加图片标记
        LatLng point1 = new LatLng(113.541112, 22.255925);

        // 设置地图中心位置
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(point1);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        // 设置地图显示层级
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.book_no_name);
        MarkerOptions markerOptions = new MarkerOptions().position(point1).icon(bitmap);
        mBaiduMap.addOverlay(markerOptions);

        // 添加文字标记
        LatLng point2 = new LatLng(113.541112, 22.255925);
        TextOptions textOptions = new TextOptions().position(point2).text("Hello Baidu Map");
        mBaiduMap.addOverlay(textOptions);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(requireContext(), "Marker clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}