package com.ishow.noah.modules.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ishow.noah.R;
import com.ishow.noah.entries.UserContainer;
import com.ishow.noah.modules.base.AppBaseFragment;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class MineFragment extends AppBaseFragment implements MineContract.View {


    private View mRootView;

    private MineContract.Presenter mPresenter;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_mine, container, false);

        mPresenter = new MinePresenter(this);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume(getContext());
    }


    @Override
    public void update(UserContainer userContainer) {
        /*
        ImageLoader.with(getContext())
                .load(userContainer.getUser().getAvatar())
                .mode(ImageLoader.LoaderMode.CIRCLE_CROP)
                .placeholder(R.drawable.logo)
                .into(mAvatarView);


        mNameView.setText(userContainer.getUser().getNickName());
        */
    }
}
