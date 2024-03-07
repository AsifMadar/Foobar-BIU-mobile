//package il.ac.biu.project.foobar.repositories;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.List;
//
//import il.ac.biu.project.foobar.entities.PostDetails;
//
//@Dao
//public interface PostDao {
//    @Query("SELECT *FROM PostDetails")
//    List<PostDetails> getPosts();
//
//    @Query("SELECT * FROM PostDetails WHERE id = :id")
//    PostDetails get(int id);
//
//    @Insert
//    void insert(PostDetails... posts);
//
//    @Update
//    void update(PostDetails... posts);
//
//    @Delete
//    void delete(PostDetails... posts);
//}
//
