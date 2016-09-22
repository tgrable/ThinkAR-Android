package com.Trekk.ThinkAR;

import android.content.ContextWrapper;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.unity3d.player.UnityPlayer;

/**
 * Created by timgrable1 on 9/9/16.
 */
public class UnityPlayerWrapper extends UnityPlayer {
    public UnityPlayerWrapper(ContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected void setFullscreen(boolean b) { super.setFullscreen(false); }
}