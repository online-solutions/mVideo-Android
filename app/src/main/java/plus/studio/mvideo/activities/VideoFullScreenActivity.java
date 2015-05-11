package plus.studio.mvideo.activities;

import android.app.ActionBar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;

import plus.studio.mvideo.R;
import plus.studio.mvideo.customs.VideoControllerView;
import plus.studio.mvideo.logging.L;

public class VideoFullScreenActivity extends ActionBarActivity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private SurfaceView videoSurface;
    private MediaPlayer player;
    private VideoControllerView controller;
    int navigationButtonFlag = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    int normalButtonFlag = 0;

    String internetUrl = "http://mvideo.herokuapp.com/raw/big_buck_bunny.mp4";
    String localUrl = "android.resource://plus.studio.mvideo/raw/big_buck_bunny";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full_screen);

        setFullScreen();

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(internetUrl));
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoSurface.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (controller.isShowing()) {
                    L.d("hide");
                    controller.hide();
                    setFullScreen();
                } else {
                    L.d("show");
                    controller.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_full_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        if (!player.isPlaying()) {
            player.prepareAsync();
        } else {
            finish();
        }

        // TODO: When start activity, this method will be call
        // When press back to previous activity, this method will be call too, i don't know why
        // I make an else for this, it's bad idea, fix late

        L.d("surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));

        //Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        //Get the width of the screen
        DisplayMetrics point = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            getWindowManager().getDefaultDisplay().getMetrics(point);
        } else if (Build.VERSION.SDK_INT >= 17) {
            getWindowManager().getDefaultDisplay().getRealMetrics(point);
        }

        int screenWidth = point.widthPixels;

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        lp.width = screenWidth;

        // set media controller width
        LinearLayout layout = (LinearLayout) controller.findViewById(R.id.media_controller);
        layout.getLayoutParams().width = screenWidth;

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);

        //Commit the layout parameters
        videoSurface.setLayoutParams(lp);

        player.start();
    }
// End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void toggleFullScreen() {
        finish();
    }
    // End VideoMediaController.MediaPlayerControl

    private void setFullScreen() {
        if (ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey()) {
            L.d("not has softkey");
            getWindow().getDecorView().setFitsSystemWindows(false);
            getWindow().getDecorView().setSystemUiVisibility(normalButtonFlag);
        } else {
            L.d("has softkey");
            getWindow().getDecorView().setFitsSystemWindows(true);
            getWindow().getDecorView().setSystemUiVisibility(navigationButtonFlag);
        }
    }
}

