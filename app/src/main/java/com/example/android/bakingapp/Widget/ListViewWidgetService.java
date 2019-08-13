package com.example.android.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.SharedPreferencesUtil;

import java.util.List;


public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new RemoteViewsFactory(this.getApplicationContext(), intent);
    }

    public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        String[] ingredientsList;

        //constructor
        public RemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredientsList = SharedPreferencesUtil.getIngredientsList(mContext);

        }

        @Override
        public int getCount() {
            return ingredientsList.length;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews item = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);
            item.setTextViewText(R.id.widget_list_item, ingredientsList[i]);
            return item;
        }
    }
}
