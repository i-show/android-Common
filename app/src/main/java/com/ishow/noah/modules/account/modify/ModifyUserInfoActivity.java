package com.ishow.noah.modules.account.modify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.utils.image.select.OnSelectPhotoListener;
import com.ishow.common.utils.image.select.SelectPhotoUtils;
import com.ishow.common.widget.textview.TextViewPro;
import com.ishow.noah.R;
import com.ishow.noah.manager.UserManager;
import com.ishow.noah.modules.base.AppBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyUserInfoActivity extends AppBaseActivity implements
        OnSelectPhotoListener,
        ModifyUserInfoContract.View {

    @BindView(R.id.header)
    TextViewPro mHeader;
    private ModifyUserInfoContract.Presenter mPresenter;

    private SelectPhotoUtils mSelectPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        mPresenter = new ModifyUserInfoPresenter(this);

        mSelectPhotoUtils = new SelectPhotoUtils(this, SelectPhotoUtils.SelectMode.SINGLE);
        mSelectPhotoUtils.setOnSelectPhotoListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        UserManager userManager = UserManager.getInstance();
        mHeader.setRightImageUrl2(userManager.getAvatar(this), ImageLoader.LoaderMode.CIRCLE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSelectPhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateAvatar(String avatar) {
        mHeader.setRightImageUrl2(avatar, ImageLoader.LoaderMode.CIRCLE_CROP);
    }


    @OnClick(R.id.header)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header:
                selectPhoto();
                break;
        }
    }


    private void selectPhoto() {
        mSelectPhotoUtils.setSelectMode(SelectPhotoUtils.SelectMode.SINGLE);
        mSelectPhotoUtils.select(1, 1, Bitmap.CompressFormat.JPEG);
    }

    @Override
    public void onSelectedPhoto(List<String> multiPath, String singlePath) {
        mPresenter.modifyAvatar(this, singlePath);
    }
}