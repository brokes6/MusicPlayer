package com.example.musicplayerdome.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.base.BasePresenter;
import com.example.musicplayerdome.databinding.FragmentPersonalsheetBinding;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.login.bean.LoginBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.other.MinePresenter;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.personal.bean.PlayListItemBean;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.other.UseridEvent;
import com.example.musicplayerdome.song.adapter.UserPlaylistAdapter;
import com.example.musicplayerdome.util.GsonUtil;
import com.example.musicplayerdome.util.SharePreferenceUtil;
import com.example.musicplayerdome.util.SharedPreferencesUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_DESCRIPTION;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;
import static com.example.musicplayerdome.personal.view.PersonalActivity.USER_ID;

public class PersonalSheetFragment extends BaseFragment<MinePresenter> implements View.OnClickListener, MineContract.View {
    private static final String TAG = "PersonalSheetFragment";
    FragmentPersonalsheetBinding binding;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private List<PlayListItemBean> adapterList = new ArrayList<>();
    private UserPlaylistAdapter adapter;
    private LoginBean loginBean;
    private long userid;
    public PersonalSheetFragment(long id){
        setFragmentTitle("主页");
        userid = id;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personalsheet,container,false);

        return binding.getRoot();
    }

    @Override
    protected void initData() {
        loginBean = GsonUtil.fromJSON(SharePreferenceUtil.getInstance(getContext()).getUserInfo(""), LoginBean.class);
        adapter = new UserPlaylistAdapter(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        binding.PSheet.setLayoutManager(manager);
        binding.PSheet.setAdapter(adapter);
        adapter.setListener(listener);
        adapter.setName(loginBean.getAccount().getUserName());
        adapter.setShowSmartPlay(true);
        if (userid!=0){
            showDialog();
            mPresenter.getUserPlaylist(userid);
        }
    }

    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    UserPlaylistAdapter.OnPlayListItemClickListener listener = new UserPlaylistAdapter.OnPlayListItemClickListener() {
        @Override
        public void onPlayListItemClick(int position) {
            Intent intent = new Intent(getContext(), SongSheetActivityMusic.class);
            intent.putExtra(PLAYLIST_PICURL, playlistBeans.get(position).getCoverImgUrl());
            intent.putExtra(PLAYLIST_DESCRIPTION, playlistBeans.get(position).getDescription());
            intent.putExtra(PLAYLIST_NAME, playlistBeans.get(position).getName());
            intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistBeans.get(position).getCreator().getNickname());
            intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistBeans.get(position).getCreator().getAvatarUrl());
            intent.putExtra(PLAYLIST_ID, playlistBeans.get(position).getId());
            getContext().startActivity(intent);
        }

        @Override
        public void onSmartPlayClick(int position) {
            showDialog();
            mPresenter.getIntelligenceList(1, playlistBeans.get(position).getId());
        }
    };

    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {
        hideDialog();
        Log.d(TAG, "onGetUserPlaylistSuccess :" + bean);
        playlistBeans.clear();
        playlistBeans.addAll(bean.getPlaylist());
        for (int i = 0; i < playlistBeans.size(); i++) {
            PlayListItemBean beanInfo = new PlayListItemBean();
            beanInfo.setCoverUrl(playlistBeans.get(i).getCoverImgUrl());
            beanInfo.setPlaylistId(playlistBeans.get(i).getId());
            beanInfo.setPlayCount(playlistBeans.get(i).getPlayCount());
            beanInfo.setPlayListName(playlistBeans.get(i).getName());
            beanInfo.setSongNumber(playlistBeans.get(i).getTrackCount());
            beanInfo.setPlaylistCreator(playlistBeans.get(i).getCreator().getNickname());
            adapterList.add(beanInfo);
        }
        adapter.loadMore(adapterList);
    }

    @Override
    public void onGetUserPlaylistFail(String e) {
        Log.e(TAG, "onGetUserPlaylistFail: ？？？？？？？？？？？？？"+e);
    }

    @Override
    public void onGetUserPlaylistAgainSuccess(UserPlaylistBean bean) {

    }

    @Override
    public void onGetUserPlaylistAgainFail(String e) {

    }

    @Override
    public void onGetUserDetailSuccess(UserDetailBean bean) {

    }

    @Override
    public void onGetUserDetailFails(String e) {

    }

    @Override
    public void onGetPlayHistoryListSuccess(SonghistoryBean bean) {

    }

    @Override
    public void onGetPlayHistoryListFail(String e) {

    }

    @Override
    public void onGetIntelligenceListSuccess(PlayModeIntelligenceBean bean) {

    }

    @Override
    public void onGetIntelligenceListFail(String e) {

    }

    @Override
    public void onGetMvSublistBeanSuccess(MvSublistBean bean) {

    }

    @Override
    public void onGetMvSublistBeanFail(String e) {

    }

    @Override
    public void onGetArtistSublistBeanSuccess(ArtistSublistBean bean) {

    }

    @Override
    public void onGetArtistSublistBeanFail(String e) {

    }

    @Override
    public void onGetAlbumSublistBeanSuccess(AlbumSublistBean bean) {

    }

    @Override
    public void onGetAlbumSublistBeanFail(String e) {

    }

    @Override
    public void onGetMyFMSuccess(MyFmBean bean) {

    }

    @Override
    public void onGetMyFMFail(String e) {

    }
}
