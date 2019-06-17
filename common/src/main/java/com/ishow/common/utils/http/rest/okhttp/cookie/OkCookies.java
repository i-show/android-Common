package com.ishow.common.utils.http.rest.okhttp.cookie;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import androidx.annotation.Keep;
import okhttp3.Cookie;

@Keep
public class OkCookies implements Serializable {

    private transient final Cookie mCookies;
    private transient Cookie mClientCookies;

    public OkCookies(Cookie cookies) {
        this.mCookies = cookies;
    }

    public Cookie getCookies() {
        Cookie bestCookies = mCookies;
        if (mClientCookies != null) {
            bestCookies = mClientCookies;
        }
        return bestCookies;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(mCookies.name());
        out.writeObject(mCookies.value());
        out.writeLong(mCookies.expiresAt());
        out.writeObject(mCookies.domain());
        out.writeObject(mCookies.path());
        out.writeBoolean(mCookies.secure());
        out.writeBoolean(mCookies.httpOnly());
        out.writeBoolean(mCookies.hostOnly());
        out.writeBoolean(mCookies.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = builder.path(path);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        mClientCookies = builder.build();
    }
}