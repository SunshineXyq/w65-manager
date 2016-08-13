package com.bluedatax.wdsm.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.utils.AMapUtil;
import com.bluedatax.wdsm.utils.Constants;
import com.bluedatax.wdsm.utils.ToastUtil;

public class MapFragment extends Fragment implements LocationSource, AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {

    MapView mMapView = null;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private TextView errInfo;
    private LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);
    private ProgressDialog progDialog;
    private GeocodeSearch geocodeSearch;
    private String addressName;
    private Marker regeoMarker;
    private Marker marker1;
    private Marker marker2;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment, null);
        errInfo = (TextView) view.findViewById(R.id.location_errInfo);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        init();
        getAddress(latLonPoint);
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
        geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(getActivity());

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
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));  //设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);                              //设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);     //设置默认定位按钮是否显示
        aMap.getUiSettings().setCompassEnabled(true);              //提供指南针功能，始终指向正北方向
        aMap.getUiSettings().setScaleControlsEnabled(true);        //设置地图的默认显示尺寸
        aMap.setMyLocationEnabled(true);                           //显示定位层并可触发定位
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);         //设置定位类型的定位模式
        aMap.setOnMarkerClickListener(this);
        addMarkersToMap();
    }

    /**
     * 往地图上添加marker
     */

    private void addMarkersToMap() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.anchor(0.5f, 0.5f);         //图标摆放在地图上的基准点
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        marker1 = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(Constants.ZHAOYANGQU);
        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
        markerOption.draggable(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.arrow));
        marker2 = aMap.addMarker(markerOption);

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
     *
     * @param onLocationChangedListener 实现监听
     */

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
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
     *
     * @param aMapLocation 获取定位参数
     */

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                errInfo.setVisibility(View.GONE);
                mListener.onLocationChanged(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo();
                errInfo.setVisibility(View.VISIBLE);
                errInfo.setText(errText);
            }
        }
    }

    /**
     * 响应逆地理编码
     *
     * @param latLonPoint 地理经纬度
     */

    private void getAddress(final LatLonPoint latLonPoint) {
        showDialog();
        //第一个参数表示经纬度，第二个参数表示范围多少米，第三个参数表示是火星坐标还是GPS原生坐标
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 显示进度条对话框
     */
    private void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);         //明确进度条进度
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 逆地理编码回调
     *
     * @param result 返回查询结果
     * @param rCode  查询返回码
     */

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null &&
                    result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                marker1.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                ToastUtil.show(getActivity(), addressName);
                System.out.println(result.getRegeocodeAddress().getFormatAddress());
            } else {
                ToastUtil.show(getActivity(), R.string.no_result);
            }
        } else {
            ToastUtil.showerror(getActivity(), rCode);
        }
    }

    /**
     * 隐藏进度条对话框
     */

    private void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 对marker标注点点击响应事件
     *
     * @param marker
     * @return
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(marker1)) {
            if (aMap != null) {
                ToastUtil.show(getActivity(), "你点击的是" + marker.getTitle());
                Log.d("marker","被点击");
            }
        }
        return false;
    }
}
