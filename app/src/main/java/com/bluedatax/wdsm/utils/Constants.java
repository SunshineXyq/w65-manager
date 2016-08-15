package com.bluedatax.wdsm.utils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

public class Constants {

	public static final int ERROR = 1001;// 网络异常
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// 路径规划中公交模式
	public static final int ROUTE_DRIVING_RESULT = 2003;// 路径规划中驾车模式
	public static final int ROUTE_WALK_RESULT = 2004;// 路径规划中步行模式
	public static final int ROUTE_NO_RESULT = 2005;// 路径规划没有搜索到结果

	public static final int GEOCODER_RESULT = 3000;// 地理编码或者逆地理编码成功
	public static final int GEOCODER_NO_RESULT = 3001;// 地理编码或者逆地理编码没有数据

	public static final int POISEARCH = 4000;// poi搜索到结果
	public static final int POISEARCH_NO_RESULT = 4001;// poi没有搜索到结果
	public static final int POISEARCH_NEXT = 5000;// poi搜索下一页

	public static final int BUSLINE_LINE_RESULT = 6001;// 公交线路查询
	public static final int BUSLINE_id_RESULT = 6002;// 公交id查询
	public static final int BUSLINE_NO_RESULT = 6003;// 异常情况

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// 方恒国际中心经纬度
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// 成都市经纬度
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// 郑州市经纬度
	public static final LatLng ZHAOYANGQU_ONE = new LatLng(40.003662, 116.465271);  //朝阳区南湖中园设备1
	public static final LatLng ZHAOYANGQU_TWO = new LatLng(40.0037772871,116.4649888822);  //朝阳区南湖中园设备2
	public static final LatLng ZHAOYANGQU_THREE = new LatLng(40.0036532871,116.4647288822);  //朝阳区南湖中园设备3
	public static final LatLng ZHAOYANGQU_FOUR = new LatLng(40.0037362871,116.4642888822);  //朝阳区南湖中园设备4
	public static final LatLng ZHAOYANGQU_FIVE = new LatLng(40.0039912871,116.4646388822);  //朝阳区南湖中园设备5
	public static final LatLng ZHAOYANGQU_SIX = new LatLng(40.0039912871,116.4641848822);  //朝阳区南湖中园设备6
	public static final LatLng ZHAOYANGQU_SENVEN = new LatLng(40.0040192871,116.4648138822);  //朝阳区南湖中园设备7
	public static final LatLng ZHAOYANGQU_EIGHT = new LatLng(40.0038392871,116.4644998822);  //朝阳区南湖中园设备8
	public static final LatLng ZHAOYANGQU_NINE = new LatLng(40.0040532871,116.4653218822);  //朝阳区南湖中园设备9
	public static final LatLng ZHAOYANGQU_TEN = new LatLng(40.0036042871,116.4655728822);  //朝阳区南湖中园设备10
}
