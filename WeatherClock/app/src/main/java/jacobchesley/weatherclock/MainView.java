package jacobchesley.weatherclock;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.List;
import java.util.Vector;

public class MainView extends FragmentActivity {

    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SetFullScreen();
        this.initialisePaging();
    }

    void SetFullScreen() {

        final Handler handler = new Handler();
        Runnable clockRun = new Runnable() {
            public void run() {
                while (true) {

                    handler.post(new Runnable() {
                        public void run() {
                            // Make app full screen
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                            // Hide navigation buttons at bottom
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

                            // Keep screen on
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(clockRun).start();
    }

    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, CurrentView.class.getName()));
        fragments.add(Fragment.instantiate(this, ClockView.class.getName()));
        fragments.add(Fragment.instantiate(this, SettingsView.class.getName()));

        adapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);

        if(pager == null){
            Log.e("ERROR PAGER", "ERROR");
        }

        else{
            pager.setAdapter(adapter);
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        public int getCount() {
            return this.fragments.size();
        }
    }
}