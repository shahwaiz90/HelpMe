package com.devtuts.helpme;
 
import java.util.Vector; 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION 			=   1; 
	// Database Name
	private static final String DATABASE_NAME 			= 	"GuardianDB";

	// Table names 
	private static final String TABLE_GUARDIAN_MEMBERS  = 	"GuardianNumbers"; 
	private static final String TABLE_GUARDIAN_MESSAGE  = 	"GuardianMessage"; 
	 

	// TABLE_GUARDIAN_MEMBERS Table Columns names
	private static final String GUARDIAN_ID 			= 	"id";  
	private static final String GUARDIAN_MEMBER_NUMBER  = 	"number";

	// TABLE_GUARDIAN_MESSAGE Table Columns names
	private static final String GUARDIAN_MSG_ID 		= 	"id";  
	private static final String GUARDIAN_MEMBER_MSG  	= 	"message";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
 
		String CREATE_GUARDIAN_TABLE = "CREATE TABLE "
				+ TABLE_GUARDIAN_MEMBERS + "(" + GUARDIAN_ID + " INTEGER PRIMARY KEY,"   
				+ GUARDIAN_MEMBER_NUMBER   + " TEXT" + ")";
 
		String CREATE_GUARDIAN_MESSAGE = "CREATE TABLE "
				+ TABLE_GUARDIAN_MESSAGE + "(" + GUARDIAN_MSG_ID + " INTEGER PRIMARY KEY,"   
				+ GUARDIAN_MEMBER_MSG   + " TEXT" + ")";
		
		db.execSQL(CREATE_GUARDIAN_TABLE);
		db.execSQL(CREATE_GUARDIAN_MESSAGE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUARDIAN_MEMBERS);  
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUARDIAN_MESSAGE);
		// Create tables again
		onCreate(db);
	}
 // GET GUARDIAN MESSAGE
	void addGuardianMessage(String msg) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GUARDIAN_MEMBER_MSG, msg);

		// Inserting Row
		db.insert(TABLE_GUARDIAN_MESSAGE, null, values);
		db.close(); // Closing database connection
	}
	String  getGuardianMessage() {
		String latestMessage = "";
			try{
				String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MESSAGE+" ORDER BY "+GUARDIAN_MSG_ID+" DESC";
				SQLiteDatabase db  = this.getWritableDatabase(); 
				Cursor cursor 	   = db.rawQuery(selectQuery, null); 
				int count 		   = cursor.getCount();
				if(count > 0){
					 cursor.moveToFirst(); 
					 latestMessage = cursor.getString(1);
				}
				db.close(); // Closing database connection
			}catch(Exception e){
				
			}
		return latestMessage;
	} 
//	GET GUARDIAN NUMBER
	void addGuardianMember(String number) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GUARDIAN_MEMBER_NUMBER, number);

		// Inserting Row
		db.insert(TABLE_GUARDIAN_MEMBERS, null, values);
		db.close(); // Closing database connection
	}

	Vector<AddGuardianModel> getGuardianMembers() {

		Vector<AddGuardianModel> vectorObject = new Vector <AddGuardianModel>();

		String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MEMBERS;
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		int count = cursor.getCount();

		for (int i = 0; i < count; i++) {
			AddGuardianModel addGuardianModel = new AddGuardianModel();

			addGuardianModel.setId(Integer.parseInt(cursor.getString(0))); 
			addGuardianModel.setNumber(cursor.getString(1));
			vectorObject.add(addGuardianModel);
			cursor.moveToNext();
		} 
		db.close(); // Closing database connection
		return vectorObject;
	}  
	public boolean IsNumberPresent(String number) {
		String filterNumber = number.replace("+", ""); 
		String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MEMBERS + " WHERE "
				+ GUARDIAN_MEMBER_NUMBER + " = '" + number+"'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.getCount() == 0) {
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}
	void removeGuardianMember(String Number) {

		SQLiteDatabase db = this.getWritableDatabase(); 
		db.delete(DatabaseHandler.TABLE_GUARDIAN_MEMBERS,  GUARDIAN_MEMBER_NUMBER + " =?", new String[]{
				 Number });

		db.close(); // Closing database connection
	}
}
