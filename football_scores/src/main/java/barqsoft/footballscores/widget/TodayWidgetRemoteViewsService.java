package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import java.text.SimpleDateFormat;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TodayWidgetRemoteViewsService extends RemoteViewsService {
    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY,

    };
    private static final int INDEX_LEAGUE_COL = 0;
    private static final int INDEX_DATE_COL = 1;
    private static final int INDEX_TIME_COL = 2;
    private static final int INDEX_HOME_COL = 3;
    private static final int INDEX_AWAY_COL = 4;
    private static final int INDEX_HOME_GOALS_COL = 5;
    private static final int INDEX_AWAY_GOALS_COL = 6;
    private static final int INDEX_MATCH_ID = 7;
    private static final int INDEX_MATCH_DAY = 8;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();

                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

                Uri scoreUri = DatabaseContract.scores_table.buildScoreWithDate();
                data = getContentResolver().query(scoreUri, SCORE_COLUMNS, null,
                        new String[]{mformat.format(System.currentTimeMillis())}, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_today_list_item);

                String home_col = data.getString(INDEX_HOME_COL);
                String away_col = data.getString(INDEX_AWAY_COL);
                String time_col = data.getString(INDEX_TIME_COL);
                String scores = Utilies.getScores(data.getInt(INDEX_HOME_GOALS_COL), data.getInt(INDEX_AWAY_GOALS_COL));
                Double match_id = data.getDouble(INDEX_MATCH_ID);
                int home_crest_id = Utilies.getTeamCrestByTeamName(data.getString(INDEX_HOME_COL), getApplicationContext());
                int away_crest_id = Utilies.getTeamCrestByTeamName(data.getString(INDEX_AWAY_COL), getApplicationContext());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    if(scores.equals(" - "))
                        setRemoteContentDescription(views, String.format(getString(R.string.list_item_no_score_content_desc), home_col, away_col, time_col, ""));
                    else
                        setRemoteContentDescription(views, String.format(getString(R.string.list_item_content_desc), home_col, away_col, scores, time_col, ""));
                }

                views.setTextViewText(R.id.widget_home_name, home_col);
                views.setTextViewText(R.id.widget_away_name, away_col);
                views.setTextViewText(R.id.widget_date, time_col);
                views.setTextViewText(R.id.widget_score, scores);
                views.setImageViewResource(R.id.widget_home_crest, home_crest_id);
                views.setImageViewResource(R.id.widget_away_crest, away_crest_id);

                final Intent fillInIntent = new Intent();
                Uri scoreUri = DatabaseContract.scores_table.buildScoreWithDate();
                fillInIntent.setData(scoreUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_list_item, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_today_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
