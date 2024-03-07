package il.ac.biu.project.foobar.entities;

/**
 * A class that represents the structure of posts received from the server.
 */
public class PostJsonDetails {
    /**
     * A class that represents the structure of comments received from the server.
     */
    public static class CommentJsonDetails {
        public PostJsonDetails.UserJsonDetails author;
        public String contents;
        public PostJsonDetails.UserJsonDetails[] likes;
        public long timestamp;
    }

    /**
     * A class that represents the structure of user objects received from the server.
     */
    public static class UserJsonDetails {
        public String displayName;
        public String profileImage;
        public String username;
    }

    public UserJsonDetails author;
    public CommentJsonDetails[] comments;
    public String contents;
    public String[] images;
    public UserJsonDetails[] likes;
    public UserJsonDetails[] shares;
    public long timestamp;
    public String id;
}
