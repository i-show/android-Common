/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.noahark.entries.egg;

import android.content.Context;
import android.content.Intent;

/**
 * Created by yuhaiyang on 2017/6/5.
 * Egg
 */

public class Egg {
    public String name;
    public Class action;

    public Intent getFormatAction(Context context) {
        Intent intent = new Intent(context, action);
        intent.putExtra(Key.NAME, name);
        return intent;
    }

    @SuppressWarnings("WeakerAccess")
    public static final class Key {
        public static final String NAME = "key_egg_name";
    }
}
