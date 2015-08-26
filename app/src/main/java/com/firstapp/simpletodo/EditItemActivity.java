package com.firstapp.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * This activity manages the modification feature of an item in the todo list
 */
public class EditItemActivity extends ActionBarActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editText = (EditText)findViewById(R.id.eteditTodoItem);

        String todoItemVal = getIntent().getStringExtra("todoItemVal");

        editText.setText(todoItemVal);
        editText.setFocusable(true);
    }

    /**
     * Submit button saves the modification to the item and navigates to {link}SimpleToDoActivity{link}
     * @param view
     */
    public void btnOnSubmit(View view) {
        Intent data = new Intent();
        data.putExtra("newTodoVal", editText.getText().toString()); // pass arbitrary data to launched activity
        data.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
