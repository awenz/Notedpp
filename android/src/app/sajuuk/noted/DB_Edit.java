package app.sajuuk.noted;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DB_Edit extends Activity{
	private EditText note;
	private Long id;
	private DB_Adapter DBA;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		DBA = new DB_Adapter(this);
		DBA.open();
		
		setContentView(R.layout.note_edit);
		setTitle(R.string.edit_note);
		note = (EditText) findViewById(R.id.note);
		
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		id = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBA.KEY_ROWID);
		if (id == null) {
			Bundle extras = getIntent().getExtras();
			id = extras != null ? extras.getLong(DBA.KEY_ROWID)
									: null;
		}

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
	}
	
	private void populateFields() {
        if (id != null) {
            Cursor notes = DBA.fetchNote(id);
            startManagingCursor(notes);
            note.setText(notes.getString(
                    notes.getColumnIndexOrThrow(DBA.KEY_TEXT)));
        }
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DBA.KEY_ROWID, id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String body = note.getText().toString();

        if (id == null) {
            long id = DBA.createNote(body);
            if (id > 0) {
                id = id;
            }
        } else {
            DBA.updateNote(id, body);
        }
    }
}
