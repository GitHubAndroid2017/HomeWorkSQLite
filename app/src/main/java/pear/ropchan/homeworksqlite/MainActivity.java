package pear.ropchan.homeworksqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";

    // данные для таблицы мужчин
    String[] male_name = { "Александр", "Игорь", "Антон", "Иван" };
    int[] years_men = {24, 18, 27, 32};

    // данные для таблицы женщин
    String[] a_womans_name = {"Дарья", "Екатерина", "Ольга", "Юлия"};
    int[] years_women = {24, 18, 27, 32};

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Подключаемся к БД
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        // Описание курсора
        Cursor c;

        // выводим в лог данные по мужским именам
        Log.d(LOG_TAG, "--- Table male ---");
        c = db.query("Man", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим в лог данные по женским именам
        Log.d(LOG_TAG, "--- Table womans ---");
        c = db.query("Women", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем query
        Log.d(LOG_TAG, "--- INNER JOIN with query---");
        String table = "Man as MN inner join Women as WN on MN.years_men = WN.years_women";
        String columns[] = { "MN.male_name as male_name", "WN.a_womans_name as a_womans_name", "years_men as years_women" };
        String selection = "years_men < ?";
        String[] selectionArgs = {"32"};
        c = db.query(table, columns, selection, selectionArgs, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // закрываем БД
        dbh.close();
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // класс для работы с БД
    class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            ContentValues cv = new ContentValues();

            // создаем таблицу мужчин
            db.execSQL("create table Man ("
                    + "id integer primary key,"
                    + "male_name text," + "years_men integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < male_name.length; i++) {
                cv.clear();
                cv.put("male_name", male_name[i]);
                cv.put("years_men", years_men[i]);
                db.insert("Man", null, cv);
            }

            // создаем таблицу женщин
            db.execSQL("create table Women ("
                    + "id integer primary key autoincrement,"
                    + "a_womans_name text,"
                    + "years_women integer"
                    + ");");

            // заполняем ее
            for (int i = 0; i < a_womans_name.length; i++) {
                cv.clear();
                cv.put("a_womans_name", a_womans_name[i]);
                cv.put("years_women", years_women[i]);
                db.insert("Women", null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}