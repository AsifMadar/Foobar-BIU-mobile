package il.ac.biu.project.foobar.entities.requests;

// ApproveFriendRequest.java

public class AddFriendRequest {
    private String friendUsername;

    public AddFriendRequest(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }
}
