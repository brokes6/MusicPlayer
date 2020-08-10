package com.example.musicplayerdome.search.view;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.SearchContract;
import com.example.musicplayerdome.base.BaseActivity;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.database.SearchHistoryDaoOp;
import com.example.musicplayerdome.databinding.ActivitySearchResultBinding;
import com.example.musicplayerdome.main.adapter.MultiFragmentPagerAdapter;
import com.example.musicplayerdome.rewrite.SearchEditText;
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
import com.example.musicplayerdome.search.fragment.FeedSearchFragment;
import com.example.musicplayerdome.search.fragment.SongSearchFragment;
import com.example.musicplayerdome.search.other.KeywordsEvent;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.song.bean.MusicCommentBean;
import com.example.musicplayerdome.song.other.SongPlayManager;
import com.example.musicplayerdome.util.ClickUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import static com.example.musicplayerdome.search.view.SearchActivity.KEYWORDS;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity.SearchResultActivity 搜索结果页面
 * 展示搜索的结果，分为2个页面
 */
public class SearchResultActivity extends BaseActivity<SearchPresenter> implements SearchContract.View{
    private static final String TAG = "SearchResultActivity";
    ActivitySearchResultBinding binding;
    private List<BaseFragment> fragments = new ArrayList<>();
    private List<SearchHistoryBean> stringList = new ArrayList<>();
    private MultiFragmentPagerAdapter pagerAdapter;
    private SearchEditText etSearch;
    private TextView btn_search;
    private String keywords;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_result);

        ImmersionBar.with(this)
                .statusBarColor(R.color.red)
                .statusBarDarkFont(false)
                .init();
        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new SongSearchFragment());
        fragments.add(new FeedSearchFragment());
        pagerAdapter.init(fragments);
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initView(){
        etSearch = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);

        btn_search.setOnClickListener(this);
        setMargins(binding.rlTitle,0,(getStatusBarHeight(this)-10),0,0);
    }

    @Override
    protected void initData() {
        setBackBtn(getString(R.string.colorWhite));
        setRightSearchButton();
        setEditText(getString(R.string.colorTransWithe));

        binding.vpContainer.setAdapter(pagerAdapter);
        binding.vpContainer.setOffscreenPageLimit(6);
        binding.tablayout.setViewPager(binding.vpContainer);
        binding.tablayout.setCurrentTab(0);

        //从GreenDao里拿搜索历史
        if (SearchHistoryDaoOp.queryAll(this) != null) {
            stringList = SearchHistoryDaoOp.queryAll(this);
        }
        //接收到前面传递来的关键字
        if (getIntent().getStringExtra(KEYWORDS) != null) {
            keywords = getIntent().getStringExtra(KEYWORDS);
            //将关键字进行postSticky发送粘性事件
            EventBus.getDefault().postSticky(new KeywordsEvent(keywords));
            etSearch.setText(keywords);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastClick(1000, v)) {
            return;
        }
        switch (v.getId()){
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
        SearchHistoryDaoOp.saveData(this, stringList);

        EventBus.getDefault().postSticky(new KeywordsEvent(keywords));

    }

    public int getPosition() {
        Log.d(TAG, "getCurrentTab : " + binding.tablayout.getCurrentTab());
        return binding.tablayout.getCurrentTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @Override
    public void onGetHotSearchDetailSuccess(HotSearchDetailBean bean) {

    }

    @Override
    public void onGetHotSearchDetailFail(String e) {

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

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}