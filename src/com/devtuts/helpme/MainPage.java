package com.devtuts.helpme;
 

import java.util.Vector;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText;
import android.widget.Toast;

public class MainPage extends Activity {
	Button addGuardian; 
	Button redButton;   
	Button updateMessage;
	EditText customText;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		context 		= 	this;
		addGuardian 	= 	(Button)   findViewById(R.id.addGuardian);
		redButton   	= 	(Button)   findViewById(R.id.helpMeButton);
		updateMessage	=	(Button)   findViewById(R.id.updateMessage);
		customText		=	(EditText) findViewById(R.id.customText);
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		if(db.getGuardianMessage() != null || db.getGuardianMessage() != "")
		{
			customText.setText(db.getGuardianMessage()); 
		}
		db.close();
		 
		updateMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(customText.getText().toString() != ""){
					
					DatabaseHandler db = new DatabaseHandler(getApplicationContext());
					db.addGuardianMessage(customText.getText().toString().trim());  
					customText.setText(db.getGuardianMessage());
					Toast.makeText(context, "Your Location Would Be Sent With Your Custom Message, Don't Worry :-)", Toast.LENGTH_LONG).show();
					db.close();
				}else{
					Toast.makeText(context, "Please Do Give your Custom Message, Your Location Will Be Sent Automatically Each Time!", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		addGuardian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addGuardian = new Intent(MainPage.this, AddGuardian.class);
				startActivity(addGuardian);
			}
		});
		
		redButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				sendSMS();
				Toast.makeText(context, "Your Custom Message with Your Location Has Been Sent, Don't Worry Someone Will Contact You Soon! :-)", Toast.LENGTH_LONG).show();
			}
		}); 
	}
	private void sendSMS(){
		Vector<AddGuardianModel>    guardianModelVector = new Vector<AddGuardianModel>();
		DatabaseHandler db  	 = 	new DatabaseHandler(getApplicationContext());
		guardianModelVector 	 = 	db.getGuardianMembers();
		String message 			 =	db.getGuardianMessage();
		SmsManager sms 			 = 	SmsManager.getDefault(); 
		String myCurrentLocation =  getUserLocation();
		
		for (int i = 0; i < guardianModelVector.size(); i++) { 
			
			String number = ((AddGuardianModel) guardianModelVector.elementAt(i)).getNumber().toString(); 
			sms.sendTextMessage(number, null, message +" Location Link: "+myCurrentLocation, null, null);
		}
	} 
	private String getUserLocation(){
		double lat;
		String sendData = "";
		double lon;
		LocationManager locationManager = null;
		LocationListener mlocListener = null;
		locationManager = (LocationManager) context .getSystemService(Context.LOCATION_SERVICE);

		Location location;

		if (locationManager .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mlocListener = new MyLocationListener();
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000, 1,
					mlocListener);

			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (location != null) {
				synchronized (this) {

					lat = location.getLatitude();
					lon = location.getLongitude();
				}
			} else {
				lat = 0;
				lon = 0;
			}
		} else {
			sendData += "Wireless Network Off ";
			lat = 0;
			lon = 0;
		}

		if (!locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			if (locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				if (lat == 0 || lon == 0) {
					mlocListener = new MyLocationListener();

					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 1000, 0,
							mlocListener);

					if (MyLocationListener.latitude > 0) {

						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						if (location != null) {
							synchronized (this) {
								lat = location.getLatitude();
								lon = location.getLongitude();
							}
						} else {
							lat = 0;
							lon = 0;
						}
					} else {
						lat = 0;
						lon = 0;
					}
				}
			} else {
				sendData += "GPS Off ";
			}
		}

		if (lat == 0 || lon == 0) {
			sendData += "Location temporarily unavailable. Please try later";
		} else {
			sendData += "\nLink To Google Maps: \nhttps://maps.google.com/maps?q="
					+ lat + "+" + lon;
		}
		return sendData;
	} 
}
