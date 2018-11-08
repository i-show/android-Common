package com.ishow.noah.modules.main.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.noah.R;
import com.ishow.noah.entries.UserContainer;
import com.ishow.noah.modules.account.modify.ModifyUserInfoActivity;
import com.ishow.noah.modules.base.AppBaseFragment;
import com.ishow.noah.modules.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class MineFragment extends AppBaseFragment implements MineContract.View {

    @BindView(R.id.avatar)
    ImageView mAvatarView;
    @BindView(R.id.name)
    TextView mNameView;
    Unbinder unbinder;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_mine, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        mPresenter = new MinePresenter(this);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void update(UserContainer userContainer) {
        ImageLoader.with(getContext())
                .load(userContainer.getUser().getAvatar())
                .mode(ImageLoader.LoaderMode.CIRCLE_CROP)
                .placeholder(R.drawable.logo)
                .into(mAvatarView);


        mNameView.setText(userContainer.getUser().getNickName());
    }

    @OnClick({R.id.modify_user_info, R.id.settings})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.modify_user_info:
                AppRouter.with(getContext())
                        .target(ModifyUserInfoActivity.class)
                        .start();
                break;
            case R.id.settings:
                AppRouter.with(getContext())
                        .target(SettingsActivity.class)
                        .start();
                break;
        }

    }
}
