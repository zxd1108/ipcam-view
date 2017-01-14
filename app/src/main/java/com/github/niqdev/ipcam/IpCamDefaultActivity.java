package com.github.niqdev.ipcam;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_AUTH_PASSWORD;
import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_AUTH_USERNAME;
import static com.github.niqdev.ipcam.settings.SettingsActivity.PREF_IPCAM_URL;

public class IpCamDefaultActivity extends AppCompatActivity {

    private static final int TIMEOUT = 5;

    @BindView(R.id.mjpegViewDefault1)
    MjpegView mjpegView1;

    @BindView(R.id.mjpegViewDefault2)
    MjpegView mjpegView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcam_default);
        ButterKnife.bind(this);
    }

    private void loadIpCam1() {
        Mjpeg.newInstance()
            .open("http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg", TIMEOUT)
            .subscribe(
                inputStream -> {
                    mjpegView1.setSource(inputStream);
                    mjpegView1.setDisplayMode(DisplayMode.BEST_FIT);
                    mjpegView1.showFps(true);
                },
                throwable -> {
                    Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                });
    }

    private void loadIpCam2() {
        Mjpeg.newInstance()
            .open("http://iris.not.iac.es/axis-cgi/mjpg/video.cgi?resolution=320x240", TIMEOUT)
            .subscribe(
                inputStream -> {
                    mjpegView2.setSource(inputStream);
                    mjpegView2.setDisplayMode(DisplayMode.BEST_FIT);
                    mjpegView2.showFps(true);
                },
                throwable -> {
                    Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIpCam1();
        loadIpCam2();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mjpegView1.stopPlayback();
        mjpegView2.stopPlayback();
    }

}
