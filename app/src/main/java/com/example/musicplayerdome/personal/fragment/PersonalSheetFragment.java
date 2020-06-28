package com.example.musicplayerdome.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.MineContract;
import com.example.musicplayerdome.base.BaseFragment;
import com.example.musicplayerdome.databinding.DelegateUserInfoBinding;
import com.example.musicplayerdome.history.bean.SonghistoryBean;
import com.example.musicplayerdome.main.bean.AlbumSublistBean;
import com.example.musicplayerdome.main.bean.ArtistSublistBean;
import com.example.musicplayerdome.main.bean.MvSublistBean;
import com.example.musicplayerdome.main.bean.MyFmBean;
import com.example.musicplayerdome.main.bean.PlayModeIntelligenceBean;
import com.example.musicplayerdome.main.other.MinePresenter;
import com.example.musicplayerdome.main.view.SongSheetActivityMusic;
import com.example.musicplayerdome.personal.adapter.UserHomePagePlayListAdapter;
import com.example.musicplayerdome.personal.bean.UserDetailBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistBean;
import com.example.musicplayerdome.personal.bean.UserPlaylistEntity;
import com.example.musicplayerdome.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_AVATARURL;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_CREATOR_NICKNAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_DESCRIPTION;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_ID;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_NAME;
import static com.example.musicplayerdome.main.fragment.HomeFragment.PLAYLIST_PICURL;

public class PersonalSheetFragment extends BaseFragment<MinePresenter> implements MineContract.View {
    private static final String TAG = "PersonalSheetFragment";
    DelegateUserInfoBinding binding;
    private List<UserPlaylistBean.PlaylistBean> playlistBeans = new ArrayList<>();
    private UserDetailBean mCurrentUser;
    private UserHomePagePlayListAdapter mPlayListAdapter;
    public PersonalSheetFragment(UserDetailBean bean){
        setFragmentTitle("主页");
        mCurrentUser = bean;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.delegate_user_info,container,false);
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        if (mCurrentUser!=null){
            showDialog();
            mPresenter.getUserPlaylist(mCurrentUser.getProfile().getUserId());
        }
    }

    @Override
    protected void initView() {
        //基本信息
        String createTime = TimeUtil.getTimeStandardOnlyYMD(mCurrentUser.getCreateTime());
        binding.tvUserDetailCreatetime.setText("村龄: " + createTime + "注册");
        binding.tvUserDetailAge.setText("年龄：" + TimeUtil.getTimeStandardOnlyYMD(mCurrentUser.getProfile().getBirthday()));
        binding.tvUserDetailArea.setText("地区：地区码" + mCurrentUser.getProfile().getProvince() + " " + mCurrentUser.getProfile().getCity());
        //听歌排行
        binding.tvUserInfoToptext.setText(mCurrentUser.getProfile().getNickname() + "的听歌排行");
        binding.tvUserInfoBottomtext.setText("累计听歌" + mCurrentUser.getListenSongs() + "首");
        binding.tvUserInfoToplike.setText(mCurrentUser.getProfile().getNickname() + "喜欢的音乐");
    }

    @Override
    public MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }


    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    //喜欢的音乐 歌单ID
    private long likeMusicId;
    private ArrayList<UserPlaylistEntity> playlistEntities = new ArrayList<>();
    @Override
    public void onGetUserPlaylistSuccess(UserPlaylistBean bean) {
        hideDialog();
        playlistBeans.clear();
        playlistBeans.addAll(bean.getPlaylist());

        binding.tvUserInfoBottomlike.setText(bean.getPlaylist().get(0).getTrackCount() + "首,  播放" + bean.getPlaylist().get(0).getPlayCount() + "次");
        likeMusicId = bean.getPlaylist().get(0).getId();
        //创建的歌单  收藏的歌单
        int size = bean.getPlaylist().size();
        //创建和收藏歌单的分界
        int subIndex = 0;
        for (int i = 0; i < size; i++) {
            if(bean.getPlaylist().get(i).getCreator().getUserId() != mCurrentUser.getProfile().getUserId()){
                subIndex = i;
                break;
            }
        }
        //创建的歌单数量
        int createPlaylistSize = subIndex -1 ;
        //收藏的歌单的数量
        int collectPlayListSize = size - subIndex;
        if (bean.getPlaylist().size()>subIndex && bean.getPlaylist().size()>5){
            Log.e(TAG, "onGetUserPlaylistSuccess: ,,,,,,,,,,,,,,"+subIndex );
            playlistEntities.add(new UserPlaylistEntity("(" +createPlaylistSize +")", "更多歌单", bean.getPlaylist().subList(1, 4)));
            playlistEntities.add(new UserPlaylistEntity("(" +collectPlayListSize +")", "更多歌单", bean.getPlaylist().subList(subIndex, subIndex + 3)));
//            playlistEntities.add(new UserPlaylistEntity("(" +collectPlayListSize +")", "更多歌单", bean.getPlaylist().subList(4, 4 + 3)));
        }else{
            playlistEntities.add(new UserPlaylistEntity("(" +createPlaylistSize +")", "更多歌单", bean.getPlaylist()));
            playlistEntities.add(new UserPlaylistEntity("(" +collectPlayListSize +")", "更多歌单", bean.getPlaylist()));
        }

        mPlayListAdapter = new UserHomePagePlayListAdapter(playlistEntities, getContext());
        //子布局点击事件 歌单详情
        mPlayListAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                Intent intent = new Intent(getContext(), SongSheetActivityMusic.class);
                intent.putExtra(PLAYLIST_PICURL, playlistBeans.get(childPosition+1).getCoverImgUrl());
                intent.putExtra(PLAYLIST_DESCRIPTION, playlistBeans.get(childPosition+1).getDescription());
                intent.putExtra(PLAYLIST_NAME, playlistBeans.get(childPosition+1).getName());
                intent.putExtra(PLAYLIST_CREATOR_NICKNAME, playlistBeans.get(childPosition+1).getCreator().getNickname());
                intent.putExtra(PLAYLIST_CREATOR_AVATARURL, playlistBeans.get(childPosition+1).getCreator().getAvatarUrl());
                intent.putExtra(PLAYLIST_ID, playlistBeans.get(childPosition+1).getId());
                getContext().startActivity(intent);
            }
        });
        binding.rvUserInfoPlaylist.setAdapter(mPlayListAdapter);
        binding.rvUserInfoPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onGetUserPlaylistFail(String e) {
        Log.e(TAG, e);
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
