package com.bright.common.utils.http.okhttp.callback;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.utils.http.okhttp.exception.CanceledException;
import com.bright.common.utils.json.JsonValidator;
import com.bright.common.widget.YToast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * JSON
 */
public abstract class BaseJsonCallBack extends CallBack<String> {
    private static final String TAG = "BaseJsonCallBack";
    private Context mContext;
    private boolean isShowTip;

    public BaseJsonCallBack() {
        this(null, false);
    }

    public BaseJsonCallBack(Context context, boolean showTip) {
        mContext = context;
        isShowTip = showTip;
    }

    @Override
    public boolean validateReponse(Response response, int id) throws Exception {
        boolean isValid = super.validateReponse(response, id);
        if (!isValid) {
            // 如果父类的没有通过则返回false
            return false;
        }
        String result = response.body().string();
        // 检测json是否有效！如果无效不进行返回
        JsonValidator validator = new JsonValidator();
        return validator.validate(result);
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

    @Override
    public boolean onError(Call call, Exception e, String errorMessage, int errorType, int id) {
        // 如果不进行提示消息 那么直接返回false
        if (!isShowTip) {
            return false;
        }
        try {
            if (e instanceof ConnectException) {
                toast(R.string.net_poor_connections);
                return true;
            } else if (e instanceof SocketTimeoutException) {
                toast(R.string.server_error);
                return true;
            } else if (e instanceof UnknownHostException) {
                toast(R.string.server_path_error);
                return true;
            } else if (e instanceof CanceledException) {
                Log.d(TAG, "onError: call is canceled");
                return true;
            } else if (!TextUtils.isEmpty(errorMessage)) {
                toast(errorMessage);
                return true;
            }
        } catch (WindowManager.BadTokenException tokenerror) {
            Log.i(TAG, "onError: tokenerror =" + tokenerror);
        }

        return false;
    }

    public void toast(int toast) {
        if (mContext != null) {
            toast(mContext.getString(toast));
        }
    }

    public void toast(String toast) {
        if (mContext != null) {
            YToast.makeText(mContext.getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }

}
