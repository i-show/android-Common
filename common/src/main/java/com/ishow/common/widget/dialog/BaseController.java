/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ishow.common.R;

import java.lang.ref.WeakReference;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class BaseController {

    private final Context mContext;
    private final DialogInterface mDialogInterface;
    private final Window mWindow;

    private View mView;
    private int mDialogLayout;
    private int mViewSpacingLeft;
    private int mViewSpacingTop;
    private int mViewSpacingRight;
    private int mViewSpacingBottom;
    private boolean mViewSpacingSpecified = false;
    private boolean isShowFromBottom = false;

    private TextView mTitleView;
    private CharSequence mTitle;

    private ScrollView mScrollView;
    private TextView mMessageView;
    private CharSequence mMessage;
    private int mMessageGravity;

    private ListView mListView;
    private ListAdapter mAdapter;
    private int mListLayout;
    private int mListItemLayout;
    private int mSingleChoiceItemLayout;
    private int mMultiChoiceItemLayout;
    private int mCheckedItem = -1;
    /**
     * 右侧确定按钮
     */
    private Button mPositiveButton;
    private CharSequence mPositiveButtonText;
    private Message mPositiveButtonMessage;
    private ColorStateList mPositiveButtonTextColor;
    /**
     * 左侧取消按钮
     */
    private Button mNegativeButton;
    private CharSequence mNegativeButtonText;
    private Message mNegativeButtonMessage;
    private ColorStateList mNegativeButtonTextColor;

    private Handler mHandler;

    private View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Message m = null;
            if (v == mPositiveButton && mPositiveButtonMessage != null) {
                m = Message.obtain(mPositiveButtonMessage);
            } else if (v == mNegativeButton && mNegativeButtonMessage != null) {
                m = Message.obtain(mNegativeButtonMessage);
            }

            if (m != null) {
                m.sendToTarget();
            }

            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialogInterface)
                    .sendToTarget();
        }
    };

    @SuppressWarnings("WeakerAccess")
    public BaseController(Context context, DialogInterface dialogInterface, Window window) {
        mContext = context;
        mDialogInterface = dialogInterface;
        mWindow = window;
        mHandler = new ButtonHandler(dialogInterface);

        TypedArray a = context.obtainStyledAttributes(null, R.styleable.DialogLayouts, R.attr.dialogLayouts, 0);
        mDialogLayout = a.getResourceId(R.styleable.DialogLayouts_dialogMainLayout, R.layout.dialog);
        mListLayout = a.getResourceId(R.styleable.DialogLayouts_dialogListLayout, R.layout.dialog_select);
        mListItemLayout = a.getResourceId(R.styleable.DialogLayouts_dialogListItem, R.layout.dialog_select_item);
        mSingleChoiceItemLayout = a.getResourceId(R.styleable.DialogLayouts_dialogSingleChoiceItem, R.layout.dialog_select_singlechoice);
        mMultiChoiceItemLayout = a.getResourceId(R.styleable.DialogLayouts_dialogMultiChoiceItem, R.layout.dialog_select_multichoice);
        a.recycle();

        init();
    }

    /**
     * 初始化部分信息
     */
    private void init() {
        mMessageGravity = Gravity.CENTER;
    }


    /**
     * {@link BaseDialog#onCreate(Bundle)}中调用
     */
    void installContent() {
        /* We use a custom title so never request a window title */
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);

        if (mView == null || !canTextInput(mView)) {
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        mWindow.setContentView(mDialogLayout);
        setupView();
    }


    private void setupView() {
        //  如果从底部弹出那么不需要设置padding
        if (isShowFromBottom) {
            View parent = mWindow.findViewById(R.id.parentPanel);
            parent.setPadding(0, 0, 0, 0);
        }

        LinearLayout topPanel = mWindow.findViewById(R.id.topPanel);
        setupTitle(topPanel);

        LinearLayout contentPanel = mWindow.findViewById(R.id.contentPanel);
        setupContent(contentPanel);


        View buttonPanel = mWindow.findViewById(R.id.buttonPanel);
        boolean hasButtons = setupButtons();
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }

        if (mView != null) {
            FrameLayout customPanel = mWindow.findViewById(R.id.customPanel);
            FrameLayout custom = mWindow.findViewById(R.id.custom);
            custom.addView(mView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
            if (mViewSpacingSpecified) {
                custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
            }
            if (mListView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
            }
        } else {
            mWindow.findViewById(R.id.customPanel).setVisibility(View.GONE);
        }

        setupList();
    }

    private void setupTitle(LinearLayout topPanel) {
        if (TextUtils.isEmpty(mTitle)) {
            View title = mWindow.findViewById(R.id.alertTitle);
            title.setVisibility(View.GONE);
            topPanel.setVisibility(View.GONE);
        } else {
            mTitleView = mWindow.findViewById(R.id.alertTitle);
            mTitleView.setText(mTitle);
        }
    }

    private void setupContent(LinearLayout contentPanel) {
        mScrollView = mWindow.findViewById(R.id.scrollView);
        mScrollView.setFocusable(false);

        // Special case for users that only want to display a String
        mMessageView = mWindow.findViewById(R.id.message);
        if (mMessageView == null) {
            return;
        }

        if (mMessage != null) {
            mMessageView.setText(mMessage);
            mMessageView.setGravity(mMessageGravity);
        } else {
            mMessageView.setVisibility(View.GONE);
            mScrollView.removeView(mMessageView);

            if (mListView != null) {
                /// M: If the count of mAdapter is equal to one, make sure the
                /// divider will not be drawn. @{
                if (mAdapter != null && mAdapter.getCount() == 1) {
                    mListView.setDividerHeight(0);
                }
                contentPanel.removeView(mScrollView);
                contentPanel.addView(mListView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            } else {
                contentPanel.setVisibility(View.GONE);
            }
        }
    }

    private boolean setupButtons() {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int whichButtons = 0;

        mPositiveButton = mWindow.findViewById(R.id.positive);
        mPositiveButton.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mPositiveButtonText)) {
            mPositiveButton.setVisibility(View.GONE);
        } else {
            mPositiveButton.setText(mPositiveButtonText);
            if (mPositiveButtonTextColor != null) {
                mPositiveButton.setTextColor(mPositiveButtonTextColor);
            }
            mPositiveButton.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mNegativeButton = mWindow.findViewById(R.id.negative);
        mNegativeButton.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mNegativeButtonText)) {
            mNegativeButton.setVisibility(View.GONE);
        } else {
            mNegativeButton.setText(mNegativeButtonText);
            if (mNegativeButtonTextColor != null) {
                mNegativeButton.setTextColor(mNegativeButtonTextColor);
            }
            mNegativeButton.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        // get Res ID
        TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.DialogButtonBackground, R.attr.dialogButtonBackgrounds, 0);
        int leftBg = a.getResourceId(R.styleable.DialogButtonBackground_leftBackground, 0);
        int rightBg = a.getResourceId(R.styleable.DialogButtonBackground_rightBackground, 0);
        int wholeBg = a.getResourceId(R.styleable.DialogButtonBackground_wholeBackground, 0);
        a.recycle();
        /*
         * NEGATIVE    POSITIVE
         *   BU2          BU1
         *    2            1
         *   10            1
         */
        //  3 = 011 7 = 111
        if (whichButtons == 3) {
            // right
            mPositiveButton.setBackgroundResource(rightBg);
            // left
            mNegativeButton.setBackgroundResource(leftBg);
        } else if (whichButtons == 2) {
            mNegativeButton.setBackgroundResource(wholeBg);
        } else if (whichButtons == 1) {
            mPositiveButton.setBackgroundResource(wholeBg);
        }
        return whichButtons != 0;
    }

    private void setupList() {
        if ((mListView != null) && (mAdapter != null)) {
            mListView.setAdapter(mAdapter);
            if (mCheckedItem > -1) {
                mListView.setItemChecked(mCheckedItem, true);
                mListView.setSelection(mCheckedItem);
            }
        }
    }

    /**
     * 设置Dialog从底部弹出
     */
    private void setShowFromBottom(boolean bottom) {
        isShowFromBottom = bottom;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    /**
     * 设置文本内容的Gravity
     *
     * @param gravity {@link Gravity#CENTER 等}
     */
    public void setMessageGravity(int gravity) {
        mMessageGravity = gravity;
        if (mMessageView != null) {
            mMessageView.setGravity(mMessageGravity);
        }
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(View view) {
        mView = view;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog along with the spacing around that view
     */
    @SuppressWarnings("WeakerAccess")
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        mView = view;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    @SuppressWarnings("WeakerAccess")
    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return mPositiveButton;
            case DialogInterface.BUTTON_NEGATIVE:
                return mNegativeButton;
            default:
                return null;
        }
    }

    /**
     * Sets a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code listener} or {@code msg}.
     *
     * @param whichButton Which button, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param listener    The {@link DialogInterface.OnClickListener} to use.
     */
    private void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        Message msg = null;
        if (listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                mPositiveButtonText = text;
                mPositiveButtonMessage = msg;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mNegativeButtonText = text;
                mNegativeButtonMessage = msg;
                break;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    /**
     * 设置右侧字体颜色
     */
    @SuppressWarnings("unused")
    public void setPositiveButtonTextColor(@ColorInt int color) {
        mPositiveButtonTextColor = ColorStateList.valueOf(color);
        setPositiveButtonTextColor(mNegativeButtonTextColor);
    }


    /**
     * 设置右侧字体颜色
     */
    @SuppressWarnings("WeakerAccess")
    public void setPositiveButtonTextColor(ColorStateList colors) {
        mPositiveButtonTextColor = colors;
        if (mPositiveButton != null && mPositiveButtonTextColor != null) {
            mPositiveButton.setTextColor(mPositiveButtonTextColor);
        }
    }

    /**
     * 设置左侧字体颜色
     */
    @SuppressWarnings("unused")
    public void setNegativeButtonTextColor(@ColorInt int color) {
        mNegativeButtonTextColor = ColorStateList.valueOf(color);
        setNegativeButtonTextColor(mNegativeButtonTextColor);
    }


    /**
     * 设置左侧字体颜色
     */
    @SuppressWarnings("WeakerAccess")
    public void setNegativeButtonTextColor(ColorStateList colors) {
        mNegativeButtonTextColor = colors;
        if (mNegativeButton != null && mNegativeButtonTextColor != null) {
            mNegativeButton.setTextColor(mNegativeButtonTextColor);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public ListView getListView() {
        return mListView;
    }


    @SuppressWarnings({"WeakerAccess", "unused"})
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    /**
     * 当前包含的View是否可以输入
     */
    private static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }


    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> dialogReference;

        private ButtonHandler(DialogInterface dialogInterface) {
            dialogReference = new WeakReference<>(dialogInterface);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(dialogReference.get(), msg.what);
                    break;
                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    public static class Params {
        public final Context mContext;
        final LayoutInflater mInflater;

        View mView;
        DialogInterface.OnCancelListener mOnCancelListener;
        DialogInterface.OnDismissListener mOnDismissListener;
        DialogInterface.OnKeyListener mOnKeyListener;
        int mViewSpacingLeft;
        int mViewSpacingTop;
        int mViewSpacingRight;
        int mViewSpacingBottom;
        boolean mViewSpacingSpecified = false;
        boolean isShowFromBottom = false;

        CharSequence mTitle;
        CharSequence mMessage;
        int mMessageGravity;

        DialogInterface.OnClickListener mPositiveButtonListener;
        CharSequence mPositiveButtonText;
        ColorStateList mPositiveButtonTextColor;

        DialogInterface.OnClickListener mNegativeButtonListener;
        CharSequence mNegativeButtonText;
        ColorStateList mNegativeButtonTextColor;

        ListAdapter mAdapter;
        DialogInterface.OnClickListener mOnClickListener;
        DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        CharSequence[] mItems;
        boolean mIsMultiChoice;
        boolean mIsSingleChoice;
        boolean mCancelable;
        boolean[] mCheckedItems;
        int mCheckedItem = -1;

        Params(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMessageGravity = Gravity.CENTER;
        }

        void apply(BaseController controller) {
            controller.setShowFromBottom(isShowFromBottom);
            controller.setTitle(mTitle);
            controller.setMessage(mMessage);
            controller.setMessageGravity(mMessageGravity);

            controller.setPositiveButtonTextColor(mPositiveButtonTextColor);
            controller.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveButtonText, mPositiveButtonListener);
            controller.setNegativeButtonTextColor(mNegativeButtonTextColor);
            controller.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText, mNegativeButtonListener);

            // For a list, the client can either supply an array of items or an
            // adapter or a cursor
            if ((mItems != null) || (mAdapter != null)) {
                createListView(controller);
            }
            if (mView != null) {
                if (mViewSpacingSpecified) {
                    controller.setView(mView, 0, 0, 0, 0);
                } else {
                    controller.setView(mView);
                }
            }
        }

        private void createListView(final BaseController dialog) {
            final ListView listView = (ListView) mInflater.inflate(dialog.mListLayout, null);
            ListAdapter adapter;

            if (mIsMultiChoice) {
                adapter = new ArrayAdapter<CharSequence>(mContext, dialog.mMultiChoiceItemLayout, android.R.id.text1, mItems) {
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        if (mCheckedItems != null) {
                            boolean isItemChecked = mCheckedItems[position];
                            if (isItemChecked) {
                                listView.setItemChecked(position, true);
                            }
                        }
                        return view;
                    }
                };
            } else {
                int layout = mIsSingleChoice ? dialog.mSingleChoiceItemLayout : dialog.mListItemLayout;
                adapter = (mAdapter != null) ? mAdapter : new ArrayAdapter<>(mContext, layout, android.R.id.text1, mItems);
            }

            /* Don't directly set the adapter on the ListView as we might
             * want to add a footer to the ListView later.
             */
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;

            if (mOnClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        mOnClickListener.onClick(dialog.mDialogInterface, position);
                        if (!mIsSingleChoice) dialog.mDialogInterface.dismiss();
                    }
                });
            } else if (mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        if (mCheckedItems != null) {
                            mCheckedItems[position] = listView.isItemChecked(position);
                        }
                        mOnCheckboxClickListener.onClick(dialog.mDialogInterface, position, listView.isItemChecked(position));
                    }
                });
            }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mIsSingleChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            } else if (mIsMultiChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            dialog.mListView = listView;
        }
    }

}
