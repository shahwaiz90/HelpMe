package com.devtuts.helpme;


import java.util.ArrayList;
import java.util.Vector; 
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AddGuardian extends ListActivity {

	ArrayList<String> 			listItemsNames; 
	ArrayAdapter<String> 		adapter;
	Vector <AddGuardianModel> 	guardianModelVector;
	Context 					context;
	ListView 					grantedMembersList;
	CharSequence[] 				removePermission;
	Button 						addGuardian;
	EditText 					guardianNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_guardian);
		
		grantedMembersList	 = 	(ListView) findViewById(android.R.id.list); 
		addGuardian			 =  (Button) findViewById(R.id.addGuardian);
		guardianNumber 		 = 	(EditText) findViewById(R.id.guardianNumber); 
		listItemsNames  	 =  new ArrayList <String> ();
		adapter 			 =	new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1, listItemsNames);
		context 			 = 	this; 
		
		setListAdapter(adapter);
		populateListItems();  
		
		 addGuardian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				boolean isNumberPresent = db.IsNumberPresent(guardianNumber.getText().toString().trim());
				db.close();
				if(isNumberPresent){
					Toast.makeText(context, "Duplicate Numbers are not allowed.", Toast.LENGTH_SHORT).show();
				}
				else if(!guardianNumber.getText().toString().equals("") && !guardianNumber.getText().toString().trim().isEmpty()){
					DatabaseHandler db2 = new DatabaseHandler(getApplicationContext());
					db2.addGuardianMember(guardianNumber.getText().toString().trim());
					db2.close();
					listItemsNames.removeAll(listItemsNames);
					populateListItems();
				}else{
					Toast.makeText(context, "Please Give A Number Of Your Guardian", Toast.LENGTH_SHORT).show();
				} 
			}
		}); 
		grantedMembersList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, final int position, long arg3) {
				removePermission = new CharSequence[] { "Remove" };
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Remove Guardian :(");

				builder.setItems(removePermission, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							String number = guardianModelVector .get(position).getNumber();
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							db.removeGuardianMember(number);
							db.close();
							listItemsNames.remove(position);
							adapter.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_LONG).show();
						}
				});
				builder.show(); 
				return true;
			}
		});
	}

	public void populateListItems() {

		guardianModelVector = new Vector<AddGuardianModel>();
		DatabaseHandler db  = new DatabaseHandler(getApplicationContext());
		guardianModelVector = db.getGuardianMembers();

		for (int i = 0; i < guardianModelVector.size(); i++) {
 
			String number = guardianModelVector.elementAt(i).getNumber().toString();

			listItemsNames.add(number);
			adapter.notifyDataSetChanged();

		}
	}
}
