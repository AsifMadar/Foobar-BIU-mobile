package il.ac.biu.project.foobar.repositories;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.utils.Converters;


@Database(entities = {PostDetails.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
}
