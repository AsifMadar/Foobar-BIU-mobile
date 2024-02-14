package il.ac.biu.project.foobar;

/**
 * A class that represents the structure of posts received from the server.
 */
class PostJsonDetails {
    /**
     * A class that represents the structure of comments received from the server.
     */
    public static class CommentJsonDetails {
        PostJsonDetails.UserJsonDetails author;
        String contents;
        PostJsonDetails.UserJsonDetails[] likes;
        long timestamp;
    }

    /**
     * A class that represents the structure of user objects received from the server.
     */
    public static class UserJsonDetails {
        String displayName;
        String profileImage;
        String username;
    }

    UserJsonDetails author;
    CommentJsonDetails[] comments;
    String contents;
    String[] images;
    UserJsonDetails[] likes;
    UserJsonDetails[] shares;
    long timestamp;
}
