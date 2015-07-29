package com.nullcognition.androidlibjokes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class ActivityJoke extends AppCompatActivity{

	@Extra
	String joke;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke);
		ActivityJokeIntentBuilder.inject(getIntent(), this);

		((TextView)findViewById(R.id.txtJoke)).setText(joke);
	}

}
