package com.example.firstapp.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This activity is the first activity in the application. This maintains the list of to do items and
 * allows adding and removing items to the list.
 */
public class SimpleTodoActivity extends ActionBarActivity {


    private final int REQUEST_CODE = 20;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private File filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setTextFilterEnabled(true);

        items = new ArrayList<String>();
        readItems();

        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);

        setUpListViewListener();

    }

    /**
     * Listeners on the list view containing the items
     */
    private void setUpListViewListener() {

        //long click on the item in the list deletes the items from the list
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                               @Override
                                               public boolean onItemLongClick(AdapterView<?> adapter,
                                                                              View item, int pos, long id) {
                                                   items.remove(pos);
                                                   itemsAdapter.notifyDataSetChanged();
                                                   writeItems();
                                                   return true;
                                               }
                                           }
        );

        //single clicking on the item enables it for modifying existing item
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SimpleTodoActivity.this, EditItemActivity.class);
                //send the item and its position ot the new activity for modifying
                i.putExtra("todoItemVal", items.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String newTodoVal = data.getExtras().getString("newTodoVal");
            // Extract the position of the items in the list
            int position = data.getExtras().getInt("position");

            if(position != -1){
                items.set(position, newTodoVal);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_todo, menu);
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

    /**
     * The Add Task button adds a item to the existing list of items and appends the items
     * to a file
     * @param view
     */
    public void btnAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etAddNewItems);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeItems();
    }

    /**
     * Read the items from a text file on the device
     */
    private void readItems() {
        filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    /**
     * Write the items list to a file.
     */
    private void writeItems() {
        filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
