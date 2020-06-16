package com.example.mytts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeakInput;
    private Button mButtonSpeakOne;
    private Button mButtonSpeakAll;
    private Spinner mSpinnerSentences;

    private String mSelectedSentence;

    private void speakAll() {
        String[] sentences = getResources().getStringArray(R.array.SentencesDrill);
        for (String text : sentences)
            mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
    }
    private void speakOne() {
        mTTS.speak(mSelectedSentence, TextToSpeech.QUEUE_FLUSH, null);
    }
    private void speakInput() {
        String text = mEditText.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSpeakInput = (Button) findViewById(R.id.button_speak_input);
        mButtonSpeakOne = (Button) findViewById(R.id.button_speak_one);
        mButtonSpeakAll = (Button) findViewById(R.id.button_speak_all);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mSeekBarPitch = (SeekBar)findViewById(R.id.seek_bar_pitch);
        mSeekBarSpeed = (SeekBar) findViewById(R.id.seek_bar_speed);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_sentences);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SentencesDrill, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                mSelectedSentence = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSeekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float pitch = (float) progress / 50;
                if (pitch < 0.1) pitch = 0.1f;
                mTTS.setPitch(pitch);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = (float) progress / 50;
                if (speed < 0.1) speed = 0.1f;
                mTTS.setSpeechRate(speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                     || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeakInput.setEnabled(true);
                        mButtonSpeakOne.setEnabled(true);
                        mButtonSpeakAll.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mButtonSpeakAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakAll();
            }
        });

        mButtonSpeakInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakInput();
            }
        });

        mButtonSpeakOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOne();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}