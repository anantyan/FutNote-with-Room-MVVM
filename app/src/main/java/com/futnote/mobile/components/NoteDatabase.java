package com.futnote.mobile.components;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "tblNote")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabase)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomDatabase = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note(1, "Singa", "Lorem ipsum dolor sit 1", 1));
            noteDao.insert(new Note(2, "Harimau", "Lorem ipsum dolor sit 2", 2));
            noteDao.insert(new Note(3, "Serigala", "Lorem ipsum dolor sit 3", 3));
            noteDao.insert(new Note(4, "Rubah", "Lorem ipsum dolor sit 4", 4));
            return null;
        }
    }
}
