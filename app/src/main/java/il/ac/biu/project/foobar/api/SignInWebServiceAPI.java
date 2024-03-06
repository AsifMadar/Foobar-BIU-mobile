package il.ac.biu.project.foobar.api;

import il.ac.biu.project.foobar.entities.SignInRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInWebServiceAPI {

    @POST("tokens")
    Call<String> signIn(@Body SignInRequest request);

}
