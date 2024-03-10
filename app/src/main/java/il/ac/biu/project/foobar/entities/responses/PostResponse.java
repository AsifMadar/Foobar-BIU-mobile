package il.ac.biu.project.foobar.entities.responses;

import java.util.Date;
import java.util.List;

public class PostResponse {
    private String id;
    private AuthorResponse author;
    private String contents;
    private List<ImageResponse> images;
    private List<String> likes;
    private List<String> shares;
    private Date timestamp;


    // Constructor
    public PostResponse(String id, AuthorResponse author, String contents, List<ImageResponse> images, List<String> likes, List<String> shares, Date timestamp) {
        this.id = id;
        this.author = author;
        this.contents = contents;
        this.images = images;
        this.likes = likes;
        this.shares = shares;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

}
