# RxLoadUtils
------
	1、在项目根目录中的build.gradle添加：
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
	
	2、在app中的build.gradle添加：
	dependencies {
  		implementation 'com.github.istaru:RxLoadUtils:v1.0'
	}
	
	3、使用刷新与loading：
	（1）自己的application需要继承BaseApplication
	（2）在xml文件中引用SmartRefreshLayout
	<?xml version="1.0" encoding="utf-8"?>
	<com.scwang.smartrefresh.layout.SmartRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		android:layout_width="match_parent"
		android:layout_height="match_parent">
		<com.scwang.smartrefresh.layout.SmartRefreshLayout
			android:id="@+id/refreshLayout"
			android:layout_width="match_parent"
			android:layout_height="match_parent">
			<include layout="@layout/view_recycler" />
		</com.scwang.smartrefresh.layout.SmartRefreshLayout>
	</com.scwang.smartrefresh.layout.SmartRefreshLayout>

	（3）RecyclerView或ListView需要被LoadingLayout包裹
	<?xml version="1.0" encoding="utf-8"?>
	<com.zx.zll.rxjava.views.LoadingLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:id="@+id/loading"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	    <android.support.v7.widget.RecyclerView
		android:id="@+id/recycler"
		android:layout_width="match_parent"
		android:layout_height="match_parent" />
	</com.zx.zll.rxjava.views.LoadingLayout>

	4、网络请求与refresh、loading结合使用：
	Map<String, Object> map = new HashMap<>();
		map.put("location", "");
		map.put("appKey", "");
		map.put("timestamp", "");
		map.put("deviceId", "");
		map.put("token", "")
		map.put("sign", "");
		HttpClient.getInstance()
			.setBaseUrl("")
			.createService(ContentService.class)
			.getBanner(map)
			.compose(Transformer.<JSONArray>call())
			.subscribe(new Subscriber<JSONArray>(loadingLayout, refreshLayout) {
			    @Override
			    protected void onSuccess(JSONArray jsonObject) {

			    }

			    @Override
			    protected void onError(String message) {

			    }
			});
------
