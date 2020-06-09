package com.example.musicplayerdome.search.view;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.musicplayerdome.search.fragment.SongSearchFragment;
import com.example.musicplayerdome.search.other.KeywordsEvent;
import com.example.musicplayerdome.search.other.SearchPresenter;
import com.example.musicplayerdome.util.ClickUtil;
import com.example.musicplayerdome.util.XToastUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import static com.example.musicplayerdome.search.view.SearchActivity.KEYWORDS;
import java.util.ArrayList;
import java.util.List;

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
        initView();
        pagerAdapter = new MultiFragmentPagerAdapter(getSupportFragmentManager());
        fragments.add(new SongSearchFragment());
//        fragments.add(new FeedSearchFragment());
//        fragments.add(new SingerSearchFragment());
        pagerAdapter.init(fragments);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initModule() {

    }

    private void initView(){
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

        if (getIntent().getStringExtra(KEYWORDS) != null) {
            keywords = getIntent().getStringExtra(KEYWORDS);
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
}