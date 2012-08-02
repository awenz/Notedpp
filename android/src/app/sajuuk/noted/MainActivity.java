package app.sajuuk.noted;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {
	
	private DB_Adapter DBA;
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        DBA = new DB_Adapter(this);
        DBA.open();
        fillData();
        registerForContextMenu(getListView());
    }
    
    private void fillData(){
    	Cursor notesCursor = DBA.fetchNotes();
    	startManagingCursor(notesCursor);
    	String [] from = new String[]{DB_Adapter.KEY_TEXT};
    	int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter notes = new SimpleCursorAdapter(this,R.layout.notes_row,notesCursor,from,to);
    	setListAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                DBA.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createNote() {
        Intent i = new Intent(this, DB_Edit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, DB_Edit.class);
        i.putExtra("_id", id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
