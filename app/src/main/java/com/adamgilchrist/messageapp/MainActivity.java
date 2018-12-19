package com.adamgilchrist.messageapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.adamgilchrist.messageapp.db.MessageContract;
import com.adamgilchrist.messageapp.db.MessageDbHelper;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private MessageDbHelper mHelper;
    private ListView mMessageListView;
    private ArrayAdapter<String> mAdapter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new MessageDbHelper(this);
        mMessageListView = findViewById(R.id.message_list);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_message:

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText messageEditText = new EditText(this);
                messageEditText.setHint("Message");
                layout.addView(messageEditText);

                final EditText contactEditText = new EditText(this);
                contactEditText.setHint("Contact");
                layout.addView(contactEditText);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new message")
                        .setMessage("Add a new message")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = String.valueOf(messageEditText.getText());
                                String contact = String.valueOf(contactEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(MessageContract.MessageEntry.COL_MESSAGE_TITLE, message);
                                values.put(MessageContract.MessageEntry.COL_CONTACT_TITLE, contact);
                                db.insertWithOnConflict(MessageContract.MessageEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteMessage(View view){
        View parent = (View) view.getParent();
        TextView messageTextView = parent.findViewById(R.id.message_title);
        String string  = String.valueOf(messageTextView.getText());
        String[] message = string.split("\\:");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(MessageContract.MessageEntry.TABLE,
                    MessageContract.MessageEntry.COL_CONTACT_TITLE + " = ?",
                    new String[]{message[0]});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> messageList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(MessageContract.MessageEntry.TABLE, new String[]{MessageContract.MessageEntry._ID, MessageContract.MessageEntry.COL_MESSAGE_TITLE, MessageContract.MessageEntry.COL_CONTACT_TITLE}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String newString = cursor.getString(cursor.getColumnIndex(MessageContract.MessageEntry.COL_CONTACT_TITLE)) + ":" + cursor.getString(cursor.getColumnIndex(MessageContract.MessageEntry.COL_MESSAGE_TITLE));
            messageList.add(newString);
        }
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this, R.layout.item_message, R.id.message_title, messageList);
            mMessageListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(messageList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void sendMessage(View view){
        View parent = (View) view.getParent();
        TextView messageTextView = parent.findViewById(R.id.message_title);
        String string  = String.valueOf(messageTextView.getText());
        String[] both = string.split("\\:");
        System.out.println(both[0] + " " + both[1]);
        String contact = both[0];
        String message = both[1];
        System.out.println(contact);
        System.out.println(message);

    }

    private void setupAlarm(View view) {
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, );
        alarm.set(AlarmManager.RTC, cal.getTimeInMillis(), pintent);
        MainActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                } else {
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
