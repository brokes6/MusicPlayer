package com.example.musicplayerdome.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.adapter.RecyclerViewBannerAdapter;
import com.example.musicplayerdome.databinding.FragmentHomeBinding;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private RecyclerViewBannerAdapter mAdapterHorizontal;
    public static String[] urls = new String[]{
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        initView();
        initBanner();
        return binding.getRoot();
    }
    private void initView(){

    }
    private void initBanner(){
        binding.blHorizontal.setAdapter(mAdapterHorizontal = new RecyclerViewBannerAdapter(urls));
    }
}
