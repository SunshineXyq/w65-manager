package com.bluedatax.wdsm.fragment;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.bluedatax.wdsm.R;

public class MapFragment extends Fragment implements LocationSource,AMapLocationListener,
        RadioGroup.OnCheckedChangeListener {

    MapView mMapView = null;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private TextView errInfo;
    private RadioButton gpsFollow;
    private RadioButton gpsLocate;
    private RadioButton gpsRotate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment,null);
        errInfo = (TextView) view.findViewById(R.id.location_errInfo);
        gpsLocate = (RadioButton) view.findViewById(R.id.gps_locate);
        gpsFollow = (RadioButton) view.findViewById(R.id.gps_follow);
        gpsRotate = (RadioButton) view.findViewById(R.id.gps_rotate);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        init();
        return view;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }

    }

    /**
     * 设置amap的属性,自定义定位图标，
     * 设置定位监听
     */
    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));
        myLocationStyle.strokeColor(R.color.black);        //设置圆形的边框颜色
        myLocationStyle.strokeWidth(1.0f);                 //设置圆形边框的粗细
        myLocationStyle.radiusFillColor(Color.argb(100,0,0,180));   //设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);                              //设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);     //设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);                           //显示定位层并可触发定位
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);         //设置定位类型的定位模式


    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 激活定位
     * @param onLocationChangedListener 实现监听
     */

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);        //设置定位监听
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;

    }

    /**
     * 定位成功后的回调方法
     * @param aMapLocation  获取定位参数
     */

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                errInfo.setVisibility(View.GONE);
                mListener.onLocationChanged(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+":"+aMapLocation.getErrorInfo();
                errInfo.setVisibility(View.VISIBLE);
                errInfo.setText(errText);
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.gps_locate:
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow:
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate:
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
        }
    }
}
