package cn.com.wosuo.taskrecorder.ui.taskloc;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.TextureMapView;

/**
 * 这个类的版权是百度地图的，只是百度地图版本的Fragment不是androidx的Fragment，故需搬过来改了下
 */
public class TextureSupportMapFragment extends Fragment {
    private static final String a = TextureSupportMapFragment.class.getSimpleName();
    private TextureMapView b;
    private BaiduMapOptions c;

    public TextureSupportMapFragment() {
    }

    private TextureSupportMapFragment(BaiduMapOptions var1) {
        this.c = var1;
    }

    public static TextureSupportMapFragment newInstance() {
        return new TextureSupportMapFragment();
    }

    public static TextureSupportMapFragment newInstance(BaiduMapOptions var0) {
        return new TextureSupportMapFragment(var0);
    }

    public BaiduMap getBaiduMap() {
        return this.b == null ? null : this.b.getMap();
    }

    public TextureMapView getMapView() {
        return this.b;
    }

    public void onAttach(Activity var1) {
        super.onAttach(var1);
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
    }

    public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        this.b = new TextureMapView(this.getActivity(), this.c);
        return this.b;
    }

    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
    }

    public void onActivityCreated(Bundle var1) {
        super.onActivityCreated(var1);
    }

    public void onViewStateRestored(Bundle var1) {
        super.onViewStateRestored(var1);
        if (var1 != null) {
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        this.b.onResume();
    }

    public void onSaveInstanceState(Bundle var1) {
        super.onSaveInstanceState(var1);
    }

    public void onPause() {
        super.onPause();
        this.b.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.b.onDestroy();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
    }
}

