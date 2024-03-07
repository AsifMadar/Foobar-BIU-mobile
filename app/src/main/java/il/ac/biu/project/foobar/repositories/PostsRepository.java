//package il.ac.biu.project.foobar.repositories;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.room.Room;
//
//import java.util.LinkedList;
//import java.util.List;
//import il.ac.biu.project.foobar.entities.PostDetails;
//
//public class PostsRepository {
//    private PostDao dao;
//    private AppDB db;
//
//    private PostListData postListData;
//
//    public PostsRepository() {
//        //  db = Room.databaseBuilder(getApplicationContext(),
//            //    AppDB.class, "PostsDB");
//     //   dao = db.postDao();
//      //  postListData = new PostListData();
//    }
//
//    class PostListData extends MutableLiveData<List<PostDetails>> {
//        public PostListData() {
//            super();
//            setValue(new LinkedList<PostDetails>());
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//      //      new Thread(() -> postListData.postValue(dao.get())).start();
//        }
//    }
//
//    public LiveData<List<PostDetails>> getAll() {
//        return postListData;
//    }
//
//    public void reload(){
//        new GetPostsTask(postListData, dao).execute();
//    }
//
//
//
//}
//
