package com.example.musicplayerdome.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


//import com.xuexiang.xui.widget.dialog.LoadingDialog;

import com.example.musicplayerdome.util.LoadingsDialog;


/**
 * 考虑是用懒加载来加载Fragment
 * Created By fuxinbo on 2020/5/14
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements View.OnClickListener {
    private static final String TAG = "BaseFragment";

    //音乐播放地址，后直接接音乐id
    public static final String SONG_URL = "http://music.163.com/song/media/outer/url?id=";

    //网络请求接口
    protected P mPresenter;

    //Loading加载类
    protected LoadingsDialog mDialogs;

    protected Activity activity;

    //fragment标题，用于Tab的标题
    public String fragmentTitle;

    //判断是否隐藏
    private boolean isFragmentVisible;

    //View是否加载完成
    private boolean isPrepared;

    //是否为第一次加载
    private boolean isFirstLoad = true;

    //强制刷新数据 但仍然要 visible & Prepared，采取reset数据的方式，所以要重新走initData
    private boolean forceLoad = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        isFirstLoad = true;
        isPrepared = true;
        View view = initView(inflater, container, savedInstanceState);
        initView();
        lazyLoad();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter = null;
        }
        isPrepared = false;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData();
            }
        }
    }

    /**
     * 初始化数据方法
     */
    protected abstract void initData();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * ViewPager联合使用
     * isVisibleToUser表示是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 显示fragment，顺便刷新数据
     */
    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }

    /**
     * 隐藏fragment
     */
    protected void onInvisible() {
        isFragmentVisible = false;
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    public abstract P onCreatePresenter();


    /**
     * 被ViewPager移出的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据 eg:
     * Bundle args = getArguments();
     * if (args != null) {
     * args.putParcelable(KEY, info);
     * }
     */
    protected abstract void initVariables(Bundle bundle);

    /**
     * 对Loading进行初始化
     */
    private void createDialog() {
        if (mDialogs == null){
            mDialogs = LoadingsDialog.getInstance(getContext());
        }
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        if (isFragmentVisible()) {
            initData();
        } else {
            setForceLoad(true);
        }
    }

    /**
     * 设置fragment的标题文字
     * @param title
     */
    public void setFragmentTitle(String title) {
        fragmentTitle = title;
    }

    /**
     * 获取fragment的标题文字
     * @return
     */
    public String getTitle() {
        return fragmentTitle;
    }

    public void showDialog() {
        mDialogs.show();
    }

    public void hideDialog() {
        mDialogs.dismiss();
    }
}
