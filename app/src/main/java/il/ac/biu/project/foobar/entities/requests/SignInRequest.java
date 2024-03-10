package il.ac.biu.project.foobar.entities.requests;

public class SignInRequest {

    private String username;
    private String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}