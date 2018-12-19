package com.adamgilchrist.messageapp.db;
import android.provider.BaseColumns;

public class MessageContract {
    public static final String DB_NAME = "com.adamgilchrist.messageapp.db";
    public static final int DB_VERSION = 1;

    public class MessageEntry implements BaseColumns{
        public static final String TABLE = "messages";
        public static final String COL_MESSAGE_TITLE = "message";
        public static final String COL_CONTACT_TITLE = "contact";
    }
}
