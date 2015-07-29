package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.example.ersin.myapplication.backend.myApi.MyApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.nullcognition.androidlibjokes.ActivityJokeIntentBuilder;

import java.io.IOException;


public class MainActivity extends AppCompatActivity{

	String s;
	private InterstitialAd interstitialAd;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart(){
		super.onStart();
		loadInterstitial();
	}

	public void loadInterstitial(){

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
		AdRequest ar = new AdRequest.Builder().build();
		interstitialAd.loadAd(ar);
	}

	public void showInterstitial(){
		if(interstitialAd.isLoaded()){
			interstitialAd.show();
		}
	}


	public void tellJoke(View view){
		showInterstitial();
		new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "T"));
		// had a 404 error because of "" blank input, either have some text or fix the backend implementation api, to not ask for a string, 0 parameters
	}


}

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String>{
	private MyApi myApiService = null;
	private Context context;

	@Override
	protected String doInBackground(Pair<Context, String>... params){
		if(myApiService == null){  // Only do this once
			MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
					new AndroidJsonFactory(), null)
					// options for running against local devappserver
					// - 10.0.2.2 is localhost's IP address in Android emulator
					// - turn off compression when running against local devappserver
					.setRootUrl("http://10.0.3.2:8080/_ah/api/")
					.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer(){
						@Override
						public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException{
							abstractGoogleClientRequest.setDisableGZipContent(true);
						}
					});
			// end options for devappserver

			myApiService = builder.build();
		}

		context = params[0].first;
		String name = params[0].second;

		try{
			return myApiService.sayHi(name).execute().getData();
		}
		catch(IOException e){
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result){
		context.startActivity(new ActivityJokeIntentBuilder(result).build(context));
	}
}
