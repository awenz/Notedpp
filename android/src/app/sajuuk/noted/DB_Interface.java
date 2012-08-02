package app.sajuuk.noted;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Interface extends SQLiteOpenHelper{
	
	public static final String DB_Name = "notes";
	public static final int DB_Version = 1;
	public static final String KEY_ROWID = "_id";
    public static final String KEY_TEXT = "note";
	
	private static final String DB_Create = "create table notes (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_TEXT + " text not null)";
	
	public DB_Interface(Context context){
		super(context,DB_Name,null,DB_Version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL(DB_Create);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int db_old, int db_new){
		Log.w(DB_Interface.class.getName(),"Upgrading from "+ db_old + "to " + db_new);
		database.execSQL("DROP TALE IF EXISTS notes");
		onCreate(database);
	}
}
