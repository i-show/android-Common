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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ishow.common.R;
import com.ishow.common.utils.DeviceUtils;


/**
 * A subclass of Dialog that can display one, two or three buttons. If you only want to
 * display a String in this dialog box, use the setMessage() method.  If you
 * want to display a more complex view, look up the FrameLayout called "custom"
 * and add your view to it:
 * <p/>
 * <pre>
 * FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
 * fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
 * </pre>
 * <p/>
 * <p>The HaiyangDialog class takes care of automatically setting
 * WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} for you based on whether
 * any views in the dialog return true from {@link View#onCheckIsTextEditor()
 * View.onCheckIsTextEditor()}.  Generally you want this set for a Dialog
 * without text editors, so that it will be placed on top of the current
 * input method UI.  You can modify this behavior by forcing the flag to your
 * desired LoaderMode after calling {@link #onCreate}.
 * <p/>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating dialogs, read the
 * <a href="{@docRoot}guide/topics/ui/dialogs.html">Dialogs</a> developer guide.</p>
 * </div>
 */

public class BaseDialog extends Dialog implements DialogInterface {
    private static final String TAG = "BaseDialog";
    private static boolean isShowFromBottom;
    private BaseController mController;

    protected BaseDialog(Context context) {
        this(context, R.style.Theme_Dialog);
    }

    /**
     * Construct an BaseDialog that uses an explicit theme.  The actual Style
     * that an HaiyangDialog uses is a private implementation, however you can
     * here supply either the name of an attribute in the theme from which
     * to get the dialog's Style (such as {@link android.R.attr#alertDialogTheme}
     */
    protected BaseDialog(Context context, int theme) {
        super(context, theme);
        mController = new BaseController(getContext(), this, getWindow());
    }


    /**
     * @return Whether the dialog is currently showing.
     */
    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    /**
     * Gets one of the buttons used in the dialog.
     * <p/>
     * If a button does not exist in the dialog, null will be returned.
     *
     * @param whichButton The identifier of the button that should be returned.
     *                    For example, this can be
     *                    {@link DialogInterface#BUTTON_POSITIVE}.
     * @return The button from the dialog, or null if a button does not exist.
     */
    @SuppressWarnings("unused")
    public Button getButton(int whichButton) {
        return mController.getButton(whichButton);
    }

    /**
     * Gets the list view used in the dialog.
     *
     * @return The {@link ListView} from the dialog.
     */
    @SuppressWarnings("unused")
    public ListView getListView() {
        return mController.getListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController.installContent();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (mController.onKeyDown(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (mController.onKeyUp(keyCode, event)) return true;
        return super.onKeyUp(keyCode, event);
    }

    // Just Set with is screen - PADDING_W , no full screen
    @Override
    public void show() {
        Context context = getContext();
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            Log.i(TAG, "show:  activity is null or finishing");
            return;
        }

        super.show();

        Window window = getWindow();
        if (window == null) {
            Log.i(TAG, "show: window is null");
            return;
        }

        LayoutParams lp = window.getAttributes();
        int width = DeviceUtils.getScreenSize()[0];
        int height = DeviceUtils.getScreenSize()[1];
        if (isShowFromBottom) {
            if (width > height) {
                //noinspection SuspiciousNameCombination
                lp.width = height;
            } else {
                lp.width = width;
            }
        } else {
            if (width > height) {
                //noinspection SuspiciousNameCombination
                lp.width = height;
            } else {
                lp.width = (int) (width * 0.8);
            }
        }

        if (isShowFromBottom) {
            lp.gravity = Gravity.BOTTOM;
            window.setWindowAnimations(R.style.Animation_Windows_Bottom);
        }
        window.setAttributes(lp);

    }

    public static class Builder {
        private final BaseController.Params mParams;
        private int mTheme;

        /**
         * Constructor using a context for this builder and the {@link BaseDialog} it creates.
         */
        public Builder(Context context) {
            this(context, R.style.Theme_Dialog);
        }

        /**
         * Constructor using a context and theme for this builder and
         * the {@link BaseDialog} it creates.  The actual theme
         * that an HaiyangDialog uses is a private implementation, however you can
         * here supply either the name of an attribute in the theme from which
         * to get the dialog's Style (such as {@link android.R.attr#alertDialogTheme}
         * or one of the constants
         */
        public Builder(Context context, int theme) {
            mParams = new BaseController.Params(new ContextThemeWrapper(context, theme));
            mTheme = theme;
            isShowFromBottom = false;
        }

        /**
         * Returns a {@link Context} with the appropriate theme for dialogs created by this Builder.
         * Applications should use this Context for obtaining LayoutInflaters for inflating views
         * that will be used in the resulting dialogs, as it will cause views to be inflated with
         * the correct theme.
         *
         * @return A Context for built Dialogs.
         */
        public Context getContext() {
            return mParams.mContext;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int titleId) {
            mParams.mTitle = mParams.mContext.getText(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(CharSequence title) {
            mParams.mTitle = title;
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(int messageId) {
            mParams.mMessage = mParams.mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(CharSequence message) {
            mParams.mMessage = message;
            return this;
        }

        /**
         * 设置MessageGravity
         *
         * @param gravity {@link Gravity#CENTER 等}
         */
        @SuppressWarnings("unused")
        public Builder setMessageGravity(int gravity) {
            mParams.mMessageGravity = gravity;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            mParams.mPositiveButtonText = mParams.mContext.getText(textId);
            mParams.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            mParams.mPositiveButtonText = text;
            mParams.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * 设置字体颜色
         */
        @SuppressWarnings("unused")
        public Builder setPositiveButtonTextColor(@ColorInt int color) {
            mParams.mPositiveButtonTextColor = ColorStateList.valueOf(color);
            return this;
        }

        /**
         * 设置字体颜色
         */
        @SuppressWarnings("unused")
        public Builder setPositiveButtonTextColor(ColorStateList colors) {
            if (colors == null) {
                throw new NullPointerException();
            }
            mParams.mPositiveButtonTextColor = colors;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            mParams.mNegativeButtonText = mParams.mContext.getText(textId);
            mParams.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            mParams.mNegativeButtonText = text;
            mParams.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * 设置字体颜色
         */
        @SuppressWarnings("unused")
        public Builder setNegativeButtonTextColor(@ColorInt int color) {
            mParams.mNegativeButtonTextColor = ColorStateList.valueOf(color);
            return this;
        }

        /**
         * 设置字体颜色
         */
        @SuppressWarnings("unused")
        public Builder setNegativeButtonTextColor(ColorStateList colors) {
            if (colors == null) {
                throw new NullPointerException();
            }
            mParams.mNegativeButtonTextColor = colors;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            mParams.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p/>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(OnDismissListener) setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(OnDismissListener)
         */
        @SuppressWarnings("unused")
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mParams.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mParams.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            mParams.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener. This should be an array type i.e. R.array.foo
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setItems(@ArrayRes int itemsId, final OnClickListener listener) {
            mParams.mItems = mParams.mContext.getResources().getTextArray(itemsId);
            mParams.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setItems(CharSequence[] items, final OnClickListener listener) {
            mParams.mItems = items;
            mParams.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter  The {@link ListAdapter} to supply the list of items
         * @param listener The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            mParams.mAdapter = adapter;
            mParams.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * This should be an array type, e.g. R.array.foo. The list will have
         * a check mark displayed to the right of the text for each checked
         * item. Clicking on an item in the list will not dismiss the dialog.
         * Clicking on a button will dismiss the dialog.
         *
         * @param itemsId      the resource id of an array i.e. R.array.foo
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
            mParams.mItems = mParams.mContext.getResources().getTextArray(itemsId);
            mParams.mOnCheckboxClickListener = listener;
            mParams.mCheckedItems = checkedItems;
            mParams.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items        the text of the items to be displayed in the list.
         * @param checkedItems specifies which items are checked. It should be null in which case no
         *                     items are checked. If non null it must be exactly the same length as the array of
         *                     items.
         * @param listener     notified when an item on the list is clicked. The dialog will not be
         *                     dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                     button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
            mParams.mItems = items;
            mParams.mOnCheckboxClickListener = listener;
            mParams.mCheckedItems = checkedItems;
            mParams.mIsMultiChoice = true;
            return this;
        }


        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. This should be an array type i.e.
         * R.array.foo The list will have a check mark displayed to the right of the text for the
         * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
         * button will dismiss the dialog.
         *
         * @param itemsId     the resource id of an array i.e. R.array.foo
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setSingleChoiceItems(int itemsId, int checkedItem, final OnClickListener listener) {
            mParams.mItems = mParams.mContext.getResources().getTextArray(itemsId);
            mParams.mOnClickListener = listener;
            mParams.mCheckedItem = checkedItem;
            mParams.mIsSingleChoice = true;
            return this;
        }


        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items       the items to be displayed.
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
            mParams.mItems = items;
            mParams.mOnClickListener = listener;
            mParams.mCheckedItem = checkedItem;
            mParams.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter     The {@link ListAdapter} to supply the list of items
         * @param checkedItem specifies which item is checked. If -1 no items are checked.
         * @param listener    notified when an item on the list is clicked. The dialog will not be
         *                    dismissed when an item is clicked. It will only be dismissed if clicked on a
         *                    button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        @SuppressWarnings("unused")
        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, final OnClickListener listener) {
            mParams.mAdapter = adapter;
            mParams.mOnClickListener = listener;
            mParams.mCheckedItem = checkedItem;
            mParams.mIsSingleChoice = true;
            return this;
        }

        /**
         * Sets a listener to be invoked when an item in the list is selected.
         *
         * @param listener The listener to be invoked.
         * @return This Builder object to allow for chaining of calls to set methods
         * @see AdapterView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
         */
        @SuppressWarnings("unused")
        public Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            mParams.mOnItemSelectedListener = listener;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied view is an instance
         * of a {@link ListView} the light background will be used.
         *
         * @param view The view to use as the contents of the Dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setView(View view) {
            mParams.mView = view;
            mParams.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog, specifying the
         * spacing to appear around that view. If the supplied view is an
         * instance of a {@link ListView} the light background will be used.
         *
         * @param view              The view to use as the contents of the Dialog.
         * @param viewSpacingLeft   Spacing between the left edge of the view and
         *                          the dialog frame
         * @param viewSpacingTop    Spacing between the top edge of the view and
         *                          the dialog frame
         * @param viewSpacingRight  Spacing between the right edge of the view
         *                          and the dialog frame
         * @param viewSpacingBottom Spacing between the bottom edge of the view
         *                          and the dialog frame
         * @return This Builder object to allow for chaining of calls to set
         * methods
         * <p/>
         * <p/>
         * This is currently hidden because it seems like people should just
         * be able to put padding around the view.
         */
        @SuppressWarnings("unused")
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            mParams.mView = view;
            mParams.mViewSpacingSpecified = true;
            mParams.mViewSpacingLeft = viewSpacingLeft;
            mParams.mViewSpacingTop = viewSpacingTop;
            mParams.mViewSpacingRight = viewSpacingRight;
            mParams.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        /**
         * 是否是从底部弹出
         */
        public Builder fromBottom(boolean bottom) {
            mParams.isShowFromBottom = bottom;
            isShowFromBottom = bottom;
            return this;
        }


        /**
         * Creates a {@link BaseDialog} with the arguments supplied to this builder. It does not
         * {@link Dialog#show()} the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use {@link #show()} if you don't have any other processing
         * to do and want this to be created and displayed.
         */
        public BaseDialog create() {
            final BaseDialog dialog = new BaseDialog(mParams.mContext, mTheme);
            dialog.setCancelable(mParams.mCancelable);
            dialog.setCanceledOnTouchOutside(mParams.mCancelable);
            dialog.setOnCancelListener(mParams.mOnCancelListener);
            dialog.setOnDismissListener(mParams.mOnDismissListener);
            dialog.setOnKeyListener(mParams.mOnKeyListener);
            mParams.apply(dialog.mController);
            return dialog;
        }

        /**
         * Creates a {@link BaseDialog} with the arguments supplied to this builder and
         * {@link Dialog#show()}'s the dialog.
         */
        public BaseDialog show() {
            BaseDialog dialog = create();
            dialog.show();
            return dialog;
        }

        /**
         * Returns an integer hash code for this object. By contract, any two
         * objects for which {@link #equals} returns {@code true} must return
         * the same hash code value. This means that subclasses of {@code Object}
         * usually override both methods or neither method.
         * <p/>
         * <p>Note that hash values must not change over time unless information used in equals
         * comparisons also changes.
         * <p/>
         * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_hashCode">Writing a correct
         * {@code hashCode} method</a>
         * if you intend implementing your own {@code hashCode} method.
         *
         * @return this object's hash code.
         * @see #equals
         */
        @Override
        public int hashCode() {
            return super.hashCode();
        }


    }
}
