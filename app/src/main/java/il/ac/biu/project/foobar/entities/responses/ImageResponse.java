package il.ac.biu.project.foobar.entities.responses;

public class ImageResponse {
    private String contents; // Or any other relevant fields

    // Constructors
    public ImageResponse(String contents) {
        this.contents = contents;
    }

    // Getters and Setters
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}

