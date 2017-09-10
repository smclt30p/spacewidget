package org.supremacy.spacewidget.widget;

/*
 *     ______  _____  ___  ______  ______  _______  __
 *    / __/ / / / _ \/ _ \/ __/  |/  / _ |/ ___/\ \/ /
 *   _\ \/ /_/ / ___/ , _/ _// /|_/ / __ / /__   \  /
 *  /___/\____/_/  /_/|_/___/_/  /_/_/ |_\___/   /_/.org
 *
 *                 Software Supremacy
 *                 www.supremacy.org
 *
 * Copyright (c) 2017 Supremacy Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.supremacy.spacewidget.R;
import org.supremacy.spacewidget.filesystem.FileSystemStatistics;
import org.supremacy.spacewidget.filesystem.FileSystemStatisticsException;
import org.supremacy.spacewidget.filesystem.FileSystemStatisticsServer;

import java.util.Locale;

/**
 * This is the implementation of the actual widget.
 */
public class FreeSpaceWidget extends AppWidgetProvider {

    /*
     * Load the native-lib.so shared library packaged with the APK
     */
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * The actual widget update logic. This is a mess, but oh well.
     * I'm not sure if data binding on widgets is supported.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.free_space_widget);
            Intent intent = new Intent(context, getClass());
            views.setOnClickPendingIntent(R.id.widget_parent, PendingIntent.getBroadcast(context, 0, intent, 0));

            try {
                FileSystemStatistics stats = FileSystemStatisticsServer.getFilesystemInfo("/data");
                double freeGB = stats.getFreeSpaceBytes() / 1000000000.0f;
                double totalGB = stats.getTotalSpaceBytes() / 1000000000.0f;

                views.setTextViewText(R.id.space_text, String.format(Locale.ENGLISH, "%.2fGB/%.2fGB", freeGB, totalGB));
                views.setProgressBar(R.id.space_progress, 100, 100 - stats.getFreeSpacePercentage(), false);

            } catch (FileSystemStatisticsException e) {
                e.printStackTrace();
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    /**
     * This method is called via a self-remote Intent when the user clicks the widget.
     * The click refreshes the data o the widget.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(),getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);

        onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

