package com.example.afinal.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.afinal.SearchFragment;
import com.example.afinal.StatsFragment;
import com.example.afinal.R;
import com.example.afinal.GraphFragment;
import com.example.afinal.NewsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "PageAdapter";
    /**
     * Indicates that only the current fragment will be
     * in the Lifecycle.State#RESUMED state. All other Fragments
     * are capped at Lifecycle.State#STARTED.
     */

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_search, R.string.tab_stats, R.string.tab_graph, R.string.tab_news};
    private static final int[] TAB_ICONS = new int[]{R.drawable.ic_textsms_white_24dp, R.drawable.ic_mic_white_24dp, R.drawable.ic_location_on_white_24dp, R.drawable.ic_map_black_24dp};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d(TAG, "new fragment:  " + position);
                return new SearchFragment();

            case 1:
                Log.d(TAG, "new fragment:  " + position);
                return new StatsFragment();
            case 2:
                Log.d(TAG, "new fragment:  " + position);
                return new GraphFragment();
            case 3:
                Log.d(TAG, "new fragment:  " + position);
                return new NewsFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        /*Drawable myDrawable;
        myDrawable = mContext.getResources().getDrawable(TAB_ICONS[position],null);
        SpannableStringBuilder sb = new SpannableStringBuilder(" "); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return mContext.getResources().getString(TAB_TITLES[position]);
        }*/
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
}