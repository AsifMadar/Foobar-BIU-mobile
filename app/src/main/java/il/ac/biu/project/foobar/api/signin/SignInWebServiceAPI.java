package il.ac.biu.project.foobar.api.signin;

import il.ac.biu.project.foobar.entities.requests.SignInRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInWebServiceAPI {

    @POST("/api/tokens")
    Call<String> signIn(@Body SignInRequest request);

}
