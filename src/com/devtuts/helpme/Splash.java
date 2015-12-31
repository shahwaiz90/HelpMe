package com.devtuts.helpme;
  
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Splash extends Activity {

	String[] 	phoneNumberList;
	Button 		about;
	Button 		sms;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		final int sleeptimer = 3000;

		Thread myThread = new Thread() {
			@Override
			public void run() {
				try {
					int currentwait = 0;
					while (currentwait < sleeptimer) {
						sleep(200);
						currentwait += 200;

						if (currentwait == 200) {
						}
					}
				} catch (Exception e) {
				} finally {
 
					Intent loginPanel = new Intent(Splash.this, MainPage.class);
					startActivity(loginPanel);
					finish();
				}
			}
		};
		myThread.start();
	}
}
