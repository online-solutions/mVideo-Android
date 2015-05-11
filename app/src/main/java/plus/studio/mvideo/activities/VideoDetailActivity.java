package plus.studio.mvideo.activities;

import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

import plus.studio.mvideo.R;
import plus.studio.mvideo.customs.VideoControllerView;
import plus.studio.mvideo.logging.L;

public class VideoDetailActivity extends ActionBarActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    private SurfaceView videoSurface;
    private MediaPlayer player;
    private VideoControllerView controller;

//    String internetUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    String internetUrl = "http://mvideo.herokuapp.com/raw/big_buck_bunny.mp4";
    String localUrl = "android.resource://plus.studio.mvideo/raw/big_buck_bunny";
    private boolean hasActiveHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

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

        videoSurface.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    controller.show();
                }
                return true;
            }
        });

        // set movie Title
        String movieTitle = getIntent().getExtras().getString("movieTitle");
        TextView tvMovieTitle = (TextView) findViewById(R.id.movie_title);
        tvMovieTitle.setText(movieTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_detail, menu);
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
        synchronized (this) {
            hasActiveHolder = true;
            this.notifyAll();
        }

        if(!player.isPlaying()){
            player.prepareAsync();
            player.setDisplay(holder);
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
        synchronized (this) {
            hasActiveHolder = false;

            synchronized(this)          {
                this.notifyAll();
            }
        }
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
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int screenWidth = point.x;

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        lp.width = screenWidth;

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
        return false;
    }

    @Override
    public void toggleFullScreen() {
//        player.pause();
        Intent intent = new Intent(this, VideoFullScreenActivity.class);
        // TODO: change movieId here
        intent.putExtra("movieId", 100);
        startActivity(intent);
//        L.t(getApplicationContext(), "fullscreen");
//        WindowManager.LayoutParams attrs = getWindow().getAttributes();
//        if (!isFullScreen())
//        {
//            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        }
//        else
//        {
//            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        }
//        getWindow().setAttributes(attrs);
    }
    // End VideoMediaController.MediaPlayerControl


}
