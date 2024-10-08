package il.ac.biu.project.foobar.api.users;

import il.ac.biu.project.foobar.entities.requests.UpdateUserRequest;
import il.ac.biu.project.foobar.entities.responses.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersWebServiceAPI {
    @GET("/api/users/{id}")
    Call<UserDetailsResponse> getUserDetails(@Path("id") String userID,
                                             @Header("Authorization") String jwtToken);

    @PATCH("/api/users/{id}")
    Call<Void> editUserDetail(@Path("id") String userID,
                                             @Header("Authorization") String jwtToken,
                                             @Body UpdateUserRequest updateUserRequest);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUserDetail(@Path("id") String userID,
                              @Header("Authorization") String jwtToken);
}
