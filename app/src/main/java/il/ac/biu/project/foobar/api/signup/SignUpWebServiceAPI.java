package il.ac.biu.project.foobar.api.signup;

import il.ac.biu.project.foobar.entities.requests.SignUpRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpWebServiceAPI {
    @POST("/api/users")
    Call<Void> signUp(@Body SignUpRequest request);
}
