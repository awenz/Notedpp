package app.sajuuk.noted;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DB_Adapter {
	
	private final Context mCtx;
	private DB_Interface DBI;
    private SQLiteDatabase DB;
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TEXT = "note";
    public static final String DATABASE_TABLE = "notes";
    private static final String DB_Create = "create table notes (_id integer primary key autoincrement, note text not null)";
    
	public DB_Adapter(Context context){
		this.mCtx=context;
	}
	
	public DB_Adapter open() {
		DBI = new DB_Interface(mCtx);
		DB = DBI.getWritableDatabase();
		return this;
	}
	
	public void close(){
		DBI.close();
	}
	
	public long createNote(String text){
		ContentValues data = new ContentValues();
		data.put("note",text);
		return DB.insert("notes", null, data);
	}
	
	public boolean deleteNote(long ID){
		return DB.delete("note", "id" + "=" + ID, null) > 0;
	}
	
	public Cursor fetchNotes(){
		//return DB.rawQuery( "select rowid _id,* from notes", null);
		return DB.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TEXT}, null, null, null, null, null);
	}
	
	public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

        		DB.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TEXT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
	
	public boolean updateNote(long rowId, String text) {
        ContentValues args = new ContentValues();
        args.put(KEY_TEXT, text);

        return DB.update("notes", args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
