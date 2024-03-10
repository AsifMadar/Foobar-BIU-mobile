package il.ac.biu.project.foobar.repositories.PostsTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import il.ac.biu.project.foobar.api.post.GetPostsAPI;
import il.ac.biu.project.foobar.entities.Comment;
import il.ac.biu.project.foobar.entities.PostJsonDetails;
import il.ac.biu.project.foobar.entities.PostDetails;
import il.ac.biu.project.foobar.entities.PostManager;
import il.ac.biu.project.foobar.repositories.PostDao;
import il.ac.biu.project.foobar.entities.UserDetails;
import il.ac.biu.project.foobar.utils.images;

public class GetPostsTask extends AsyncTask<Void, Void, Void> {
    private GetPostsAPI apiService = new GetPostsAPI();
    private MutableLiveData<List<PostDetails>> postListData;
    private PostDao dao;
    private UserDetails userDetails = UserDetails.getInstance();
    SwipeRefreshLayout postsRefresh;

    public GetPostsTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao, SwipeRefreshLayout postsRefresh) {
        this.dao = dao;
        this.postListData = postListData;
        this.postsRefresh = postsRefresh;
    }

    public GetPostsTask(MutableLiveData<List<PostDetails>> postListData, PostDao dao) {
        this.dao = dao;
        this.postListData = postListData;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        apiService.getPosts(userDetails.getJwt(), new GetPostsAPI.GetPostsResponseCallback() {
            @Override
            public void onSuccess(List<PostJsonDetails> postJsonDetailsList) {
                // Convert PostJsonDetails to PostDetails
                List<PostDetails> postDetailsList = convertToPostDetailsList(postJsonDetailsList);
                dao.deleteAll();
                for (PostDetails postDetails : postDetailsList) {
                    dao.insert(postDetails);
                }
                postListData.postValue(dao.getPosts());
                if (postsRefresh != null)
                  postsRefresh.setRefreshing(false);
                PostManager.getInstance().removeAllPosts();
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private List<PostDetails> convertToPostDetailsList(List<PostJsonDetails> postJsonDetails) {
        List<PostDetails> postDetailsList = new ArrayList<>();
        for (PostJsonDetails jsonDetails : postJsonDetails) {
            String id = jsonDetails.id;
            String authorDisplayName = jsonDetails.author != null ? jsonDetails.author.displayName : "";
            String username = jsonDetails.author != null ? jsonDetails.author.username : "";
            Bitmap authorProfilePicture = images.base64ToBitmap(jsonDetails.author.profileImage);
            String userInput = jsonDetails.contents;
            Bitmap picture = jsonDetails.images.length > 0 ? images.base64ToBitmap(jsonDetails.images[0]) : null;
            long time = jsonDetails.timestamp;

            PostDetails postDetails = new PostDetails(id, username, authorDisplayName, authorProfilePicture, userInput, picture, time);

            // Adding likes
            if (jsonDetails.likes != null) {
                postDetails.setLikeList(Arrays.asList(jsonDetails.likes));
            }

            // Adding comments
            if (jsonDetails.comments != null) {
                for (PostJsonDetails.CommentJsonDetails commentJson : jsonDetails.comments) {
                    Comment comment = new Comment(
                            commentJson.author.displayName,
                            images.base64ToBitmap(commentJson.author.profileImage),
                            commentJson.contents,
                            commentJson.timestamp
                    );
                    if (commentJson.likes != null) {
                        for (PostJsonDetails.UserJsonDetails likeUser : commentJson.likes) {
                            comment.addLike(likeUser.username);
                        }
                    }
                    postDetails.addComment(comment);
                }
            }

            postDetailsList.add(postDetails);
        }
        return postDetailsList;
    }
}
