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

package com.ishow.common.widget.spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.view.animation.LinearInterpolator;

import com.ishow.common.widget.spinkit.animation.SpriteAnimatorBuilder;
import com.ishow.common.widget.spinkit.sprite.RectSprite;
import com.ishow.common.widget.spinkit.sprite.Sprite;
import com.ishow.common.widget.spinkit.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class FoldingCube extends SpriteContainer {

    @SuppressWarnings("FieldCanBeLocal")
    private boolean wrapContent = false;

    @Override
    public Sprite[] onCreateChild() {
        Cube[] cubes
                = new Cube[4];
        for (int i = 0; i < cubes.length; i++) {
            cubes[i] = new Cube();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cubes[i].setAnimationDelay(300 * i);
            } else {
                cubes[i].setAnimationDelay(300 * i - 1200);
            }
        }
        return cubes;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int size = Math.min(bounds.width(), bounds.height());
        if (wrapContent) {
            size = (int) Math.sqrt(
                    (size
                            * size) / 2);
            int oW = (bounds.width() - size) / 2;
            int oH = (bounds.height() - size) / 2;
            bounds = new Rect(
                    bounds.left + oW,
                    bounds.top + oH,
                    bounds.right - oW,
                    bounds.bottom - oH
            );
        }

        int px = bounds.left + size / 2 + 1;
        int py = bounds.top + size / 2 + 1;
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(
                    bounds.left,
                    bounds.top,
                    px,
                    py
            );
            sprite.setPivotX(sprite.getDrawBounds().right);
            sprite.setPivotY(sprite.getDrawBounds().bottom);
        }
    }

    @Override
    public void drawChild(Canvas canvas) {

        Rect bounds = clipSquare(getBounds());
        for (int i = 0; i < getChildCount(); i++) {
            int count = canvas.save();
            canvas.rotate(45 + i * 90, bounds.centerX(), bounds.centerY());
            Sprite sprite = getChildAt(i);
            sprite.draw(canvas);
            canvas.restoreToCount(count);
        }
    }

    private class Cube extends RectSprite {

        Cube() {
            setAlpha(0);
            setRotateX(-180);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.1f, 0.25f, 0.75f, 0.9f, 1f};
            return new SpriteAnimatorBuilder(this).
                    alpha(fractions, 0, 0, 255, 255, 0, 0).
                    rotateX(fractions, -180, -180, 0, 0, 0, 0).
                    rotateY(fractions, 0, 0, 0, 0, 180, 180).
                    duration(2400).
                    interpolator(new LinearInterpolator())
                    .build();
        }
    }
}
