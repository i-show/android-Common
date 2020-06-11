package com.ishow.noah.modules.main.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;

import com.ishow.noah.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    private static final String TAG = "yhy";

    public void test(Context context) throws IOException, XmlPullParserException {
        try (XmlResourceParser parser = context.getResources().getXml(R.xml.device_profiles)) {
            final int depth = parser.getDepth();
            int type;

            while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
                if ((type == XmlPullParser.START_TAG) && "profile".equals(parser.getName())) {
                    Log.d(TAG, "getPredefinedDeviceProfiles===================== depth = " + depth + "type = " + type);
                    TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.InvariantDeviceProfile);
                    int numRows = a.getInt(R.styleable.InvariantDeviceProfile_numRows, 0);
                    int numColumns = a.getInt(R.styleable.InvariantDeviceProfile_numColumns, 0);
                    Log.i(TAG, "test: numRows = " + numRows);
                    Log.i(TAG, "test: numColumns = " + numColumns);
                }

            }
        }
    }
}
