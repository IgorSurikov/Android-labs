package com.example.mediaplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class VideoFragment extends Fragment implements View.OnClickListener {

    Spinner spinner;
    MediaPlayer mediaPlayer;
    AudioManager am;
    SurfaceView surfaceView;
    File videoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

    public void checkPermissionReadStorage(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

                Toast.makeText(getActivity(), "We Need permission Storage", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private String[] getVideoFileList() {

        checkPermissionReadStorage(getActivity());
        String[] MusicFileList;

        if (videoDir.exists()) {
            MusicFileList = videoDir.list();
        } else {
            MusicFileList = null;
        }
        return MusicFileList;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getVideoFileList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) view.findViewById(R.id.filesSpinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("File name");
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        Button start = (Button) view.findViewById(R.id.start);
        Button stop = (Button) view.findViewById(R.id.stop);
        Button forward = (Button) view.findViewById(R.id.forward);
        Button back = (Button) view.findViewById(R.id.back);
        Button resume = (Button) view.findViewById(R.id.resume);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        forward.setOnClickListener(this);
        back.setOnClickListener(this);
        resume.setOnClickListener(this);
        return view;
    }



    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                releaseMP();
                mediaPlayer = new MediaPlayer();
                SurfaceHolder holder = surfaceView.getHolder();
                mediaPlayer.setDisplay(holder);
                String data = videoDir.getAbsolutePath() + "/" + spinner.getSelectedItem().toString();
                try {
                    mediaPlayer.setDataSource(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes
                                .Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                .build());
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*int videoWidth = mediaPlayer.getVideoWidth();
                int videoHeight = mediaPlayer.getVideoHeight();
                float videoProportion = (float) videoWidth / (float) videoHeight;
                int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                int screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
                float screenProportion = (float) screenWidth / (float) screenHeight;
                android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();

                if (videoProportion > screenProportion) {
                    lp.width = screenWidth;
                    lp.height = (int) ((float) screenWidth / videoProportion);
                } else {
                    lp.width = (int) (videoProportion * (float) screenHeight);
                    lp.height = screenHeight;
                }
                surfaceView.setLayoutParams(lp);*/

                mediaPlayer.start();
                break;
            case R.id.stop:
                if (mediaPlayer == null) {
                    break;
                }
                mediaPlayer.pause();
                break;
            case R.id.forward:
                if (mediaPlayer == null) {
                    break;
                }
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 3000);
                break;
            case R.id.back:
                if (mediaPlayer == null) {
                    break;
                }
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 3000);
                break;
            case R.id.resume:
                if (mediaPlayer == null) {
                    break;
                }
                mediaPlayer.start();
                break;

        }
    }
}