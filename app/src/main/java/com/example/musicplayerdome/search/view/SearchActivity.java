package com.example.musicplayerdome.search.view;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.database.SearchHistoryDaoOp;
import com.example.musicplayerdome.databinding.ActivitySearchBinding;
import com.example.musicplayerdome.rewrite.SearchEditText;
import com.example.musicplayerdome.rewrite.SearchHistoryTagLayout;
import com.example.musicplayerdome.search.adapter.HotSearchAdapter;
import com.example.musicplayerdome.search.bean.AlbumSearchBean;
import com.example.musicplayerdome.search.bean.FeedSearchBean;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.example.musicplayerdome.search.bean.PlayListSearchBean;
import com.example.musicplayerdome.search.bean.RadioSearchBean;
import com.example.musicplayerdome.search.bean.SearchHistoryBean;
import com.example.musicplayerdome.search.bean.SingerSearchBean;
import com.example.musicplayerdome.search.bean.SongSearchBean;
import com.example.musicplayerdome.search.bean.SynthesisSearchBean;
import com.example.musicplayerdome.search.bean.UserSearchBean;
import com.example.musicplayerdome.search.bean.VideoDataBean;
import com.example.musicplayerdome.search.bean.VideoUrlBean;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity 搜索页面
 * 展示热门搜索，也可以进行搜索
 */
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    private static final String TAG = "SearchActivity";
    public static final String KEYWORDS = "keywords";
    ActivitySearchBinding binding;
    private TextView btn_search;
    private SearchEditText etSearch;
    private HotSearchAdapter adapter;
    private HotSearchDetailBean searchDetailBean;
    private List<SearchHistoryBean> stringList = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);

        ImmersionBar.with(this)
                .statusBarColor(R.color.red)
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setEditText(getString(R.string.colorTransWithe));
        setRightSearchButton();

        adapter = new HotSearchAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.rvHotsearch.setLayoutManager(manager);
        binding.rvHotsearch.setAdapter(adapter);
        adapter.setListener(searchListener);

        showDialog();
        mPresenter.getHotSearchDetail();
    }

    @Override
    protected void initView(){
        etSearch = findViewById(R.id.et_search);
        etSearch.requestFocus();
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        binding.ivRubbishBin.setOnClickListener(this);
        setMargins(binding.rlTitle,0,(getStatusBarHeight(this)-10),0,0);
    }

    private HotSearchAdapter.OnHotSearchAdapterClickListener searchListener = position -> {
        if (searchDetailBean != null) {
            String keywords = searchDetailBean.getData().get(position).getSearchWord();
            searchSong(keywords);
        }
    };
    private SearchHistoryTagLayout.OnHistoryTagClickListener tagListener = position -> {
        String keywords = stringList.get(position).getKeyowrds();
        searchSong(keywords);
    };

    //根据关键字去搜索
    private void searchSong(String keywords) {
        stringList.add(new SearchHistoryBean(keywords));
        if (stringList.size() > 10) {
            stringList.remove(0);
        }
        for (int i = 0; i < stringList.size() - 1; i++) {
            //去重
            if (stringList.get(i).getKeyowrds().equals(keywords)) {
                stringList.remove(i);
                break;
            }
        }
        //将每次搜索的关键字存储到数据库
        SearchHistoryDaoOp.saveData(this, stringList);

        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra(KEYWORDS, keywords);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rubbish_bin:
                new MaterialDialog.Builder(SearchActivity.this)
                        .content("确定清空全部历史记录？")
                        .positiveText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .negativeText("清空")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                //直接清楚数据库
                                SearchHistoryDaoOp.deleteAllData(SearchActivity.this);
                                stringList = SearchHistoryDaoOp.queryAll(SearchActivity.this);
                                binding.tlSearchhistory.addHistoryText(stringList, tagListener);
                            }
                        })
                        .show();
                break;
            case R.id.btn_search:
                String keywords = etSearch.getKeyWords();
                if (!TextUtils.isEmpty(keywords)) {
                    searchSong(keywords);
                } else {
                    XToastUtils.info("请输入关键字！");
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        stringList.clear();

        int key = (int) SharedPreferencesUtil.getData("Ykey",0);
        if (key!=3){
            if (SongPlayManager.getInstance().isDisplay()) {
                binding.bottomController.setVisibility(View.VISIBLE);
            } else {
                binding.bottomController.setVisibility(View.GONE);
            }
        }else{
            binding.bottomController.setVisibility(View.GONE);
        }

        //从GreenDao里拿搜索历史
        if (SearchHistoryDaoOp.queryAll(this) != null) {
            stringList = SearchHistoryDaoOp.queryAll(this);
            if (stringList.size() > 0) {
                binding.searchHistory.setVisibility(View.VISIBLE);
                binding.ivRubbishBin.setVisibility(View.VISIBLE);
                binding.tlSearchhistory.setVisibility(View.VISIBLE);
            } else {
                binding.searchHistory.setVisibility(View.GONE);
                binding.ivRubbishBin.setVisibility(View.GONE);
                binding.tlSearchhistory.setVisibility(View.GONE);
            }
        }
        binding.tlSearchhistory.addHistoryText(stringList, tagListener);
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {
        hideDialog();
        searchDetailBean = bean;
        adapter.loadMore(bean.getData());
    }

    @Override
    public void onGetHotSearchDetailFail(String e) {
        hideDialog();
        XToastUtils.error("获取热门搜索失败，请检查网络尝试");
    }

    @Override
    public void onGetSongSearchSuccess(SongSearchBean bean) {

    }

    @Override
    public void onGetSongSearchFail(String e) {

    }

    @Override
    public void onGetFeedSearchSuccess(FeedSearchBean bean) {

    }

    @Override
    public void onGetFeedSearchFail(String e) {

    }

    @Override
    public void onGetSingerSearchSuccess(SingerSearchBean bean) {

    }

    @Override
    public void onGetSingerSearchFail(String e) {

    }

    @Override
    public void onGetAlbumSearchSuccess(AlbumSearchBean bean) {

    }

    @Override
    public void onGetAlbumSearchFail(String e) {

    }

    @Override
    public void onGetPlayListSearchSuccess(PlayListSearchBean bean) {

    }

    @Override
    public void onGetPlayListSearchFail(String e) {

    }

    @Override
    public void onGetRadioSearchSuccess(RadioSearchBean bean) {

    }

    @Override
    public void onGetRadioSearchFail(String e) {

    }

    @Override
    public void onGetUserSearchSuccess(UserSearchBean bean) {

    }

    @Override
    public void onGetUserSearchFail(String e) {

    }

    @Override
    public void onGetSynthesisSearchSuccess(SynthesisSearchBean bean) {

    }

    @Override
    public void onGetSynthesisSearchFail(String e) {

    }

    @Override
    public void onGetVideoDataSuccess(VideoUrlBean bean) {

    }

    @Override
    public void onGetVideoDataFail(String e) {

    }

    @Override
    public void onGetVideoCommentSuccess(MusicCommentBean bean) {

    }

    @Override
    public void onGetVideoCommentFail(String e) {

    }

    @Override
    public void onGetVideoDetailsSuccess(VideoDataBean bean) {

    }

    @Override
    public void onGetVideoDetailsFail(String e) {

    }
}