package com.adcoretechnologies.golfbudz.utils.api;


import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.auth.Register.PojoCity;
import com.adcoretechnologies.golfbudz.blog.PojoBlog;
import com.adcoretechnologies.golfbudz.blog.PojoBlogs;
import com.adcoretechnologies.golfbudz.event.PojoEvent;
import com.adcoretechnologies.golfbudz.friends.PojoFriend;
import com.adcoretechnologies.golfbudz.group.PojoGroup;
import com.adcoretechnologies.golfbudz.home.comment.PojoComment;
import com.adcoretechnologies.golfbudz.home.model.PojoPost;
import com.adcoretechnologies.golfbudz.home.model.PojoService;
import com.adcoretechnologies.golfbudz.items.PojoItems;
import com.adcoretechnologies.golfbudz.more.PojoContact;
import com.adcoretechnologies.golfbudz.more.PojoFaq;
import com.adcoretechnologies.golfbudz.notification.PojoNoti;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoDropValues;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoStatusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Irfan on 2/02/17.
 */
public interface IApiService {
    //Done
    @FormUrlEncoded
    @POST("login")
    Call<PojoUser> login(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType,
                         @Field("imeiNo") String imeiNo, @Field("deviceId") String deviceId,@Field("socialToken") String socialToken);

    @FormUrlEncoded
    @POST("users/social/auth")
    Call<PojoUser> loginSocial(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType,
                         @Field("imeiNo") String imeiNo, @Field("deviceId") String deviceId,@Field("socialToken") String socialToken);

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<PojoUser> getpassword(@Field("email") String email);
    @GET("users")
    Call<PojoUser> getAllUsers();
    @GET("countries")
    Call<PojoCity> getAllCountry();

    @GET("categories/{id}/services")
    Call<PojoService> getServicesByCategoryId(@Path("id") String categoryId);

    @POST("register")
    Call<PojoUser> register(@Body BoUser boIvReg);

    @PUT("users/{userId}")
    Call<PojoUser> updateProfile(@Path("userId") String userId, @Body BoUser boIvReg);

    @FormUrlEncoded
    @PUT("changepassword")
    Call<PojoUser> updatePassword(@Field("oldPass") String oldPass, @Field("newPass") String newPass, @Field("confirmPass") String confirmPass);

    @GET("users/{userId}")
    Call<PojoUser> getProfile(@Path("userId") String userId);

    @FormUrlEncoded
    @POST("events")
    Call<PojoUser> createEvent(@Field("userName") String userName, @Field("title") String title, @Field("description") String description,
                               @Field("date") String date, @Field("time") String time, @Field("image") String image, @Field("video") String video, @Field("userId") String userId);

    @GET("events")
    Call<PojoEvent> getEvents();

    @GET("events/{id}")
    Call<PojoEvent> getEventDetail(@Path("id") String id);

    @FormUrlEncoded
    @POST("events/like")
    Call<PojoEvent> likeEvent(@Field("userName") String userName, @Field("userId") String userId, @Field("eventId") String eventId);

    @FormUrlEncoded
    @POST("items")
    Call<PojoItems> sellItem(@Field("userId") String userId, @Field("title") String title, @Field("price") String price, @Field("image") String image, @Field("description") String description);


    @GET("items")
    Call<PojoItems> buyItem();

    @FormUrlEncoded
    @POST("pages")
    Call<PojoUser> createGroup(@Field("userName") String userName, @Field("title") String title, @Field("description") String description,
                               @Field("category") String category, @Field("operatingHours") String operatingHours, @Field("image") String image, @Field("video") String video, @Field("userId") String userId, @Field("friendId") String friendId);

//    @GET("blogs")
//    Call<PojoBlog> getAllBlogs();
//    getBlog?blogtype=1

//  @GET("getBlog?blogtype{blogid}")
//    Call<PojoBlog> getAllBlogs(@Path("blogId") String blogId);

    @GET("getBlog?blogtype")
    Call<PojoBlogs> getAllBlog(@Query("blogtype") String blogId);

    @FormUrlEncoded
    @POST("posts")
    Call<PojoPost> addPost(@Field("userId") String userId, @Field("text") String text, @Field("image") String image,  @Field("postType") String postType, @Field("thumbUrl") String thumbUrl);

    @GET("users/{userId}/posts")
    Call<PojoPost> getAllPostByUserId(@Path("userId") String userId);

    @GET("clubs/{userId}")
    Call<PojoUser> getAllClub(@Path("userId") String userId);

    @GET("users/{userId}/posts")
    Call<PojoItems> butItems(@Path("userId") String userId);

    @FormUrlEncoded
    @POST("items")
    Call<PojoContact> contactUs(@Field("userId") String userId, @Field("title") String title, @Field("message") String message, @Field("email") String email);

    @GET("users/{userId}/notifications")
    Call<PojoNoti> getNotifications(@Path("userId") String userId);

    @GET("users/{userId}/friends")
    Call<PojoFriend> getMyFriends(@Path("userId") String userId);

    @FormUrlEncoded
    @POST("clubs/join")
    Call<PojoUser> joinClub(@Field("userId") String userId, @Field("clubId") String clubId);

    @FormUrlEncoded
    @PUT("items")
    Call<PojoItems> buyItems(@Field("userId") String userId, @Field("itemId") String itemId);

    @GET("questions")
    Call<PojoFaq> getFAQs();

    @POST("playrequests")
    Call<PojoPlay> craeteRequest(@Body BoPlay boPlay);

    @GET("playrequests/{userId}")
    Call<PojoPlay> getAllPlayReq(@Path("userId") String userId);

    @FormUrlEncoded
    @POST("users/{userId}/upcomming/games")
    Call<PojoPlay> getAllApprovedUpcomingGames(@Path("userId") String userId,@Field("currentDate") String currentDate);
    @GET("users/{userId}/requests")
    Call<PojoStatusRequest> getAllUpcomingGames(@Path("userId") String userId);

    @FormUrlEncoded
    @PUT("request/status")
    Call<PojoFriend> friendStatus(@Field("status") String status, @Field("friendId") String friendId, @Field("userId") String userId,@Field("notificationId") String notificationId);

    @FormUrlEncoded
    @PUT("requests")
    Call<PojoFriend> joinStatus(@Field("status") String status, @Field("requestId") String requestId, @Field("userId") String userId,@Field("notificationId") String notificationId);


    @GET("users/{userId}/friends/{text}/search")
    Call<PojoFriend> getSearchFriends(@Path("userId") String userId, @Path("text") String text);

    @FormUrlEncoded
    @POST("friend/request")
    Call<PojoFriend> addFriend(@Field("friendId") String friendId, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("posts/like")
    Call<PojoEvent> postLike(@Field("userName") String userName, @Field("userId") String userId, @Field("postId") String postId);

    @GET("posts/comments/{postId}")
    Call<PojoComment> getCommentsByPostId(@Path("postId") String postId);

    @FormUrlEncoded
    @POST("posts/comment")
    Call<PojoUser> commentByUserId(@Field("userId") String userId, @Field("userName") String userName
            , @Field("postId") String postId, @Field("comment") String comment, @Field("userImgUrl") String userImgUrl);

    @FormUrlEncoded
    @POST("send/playrequest")
    Call<PojoFriend> sendPlayReqByuserId(@Field("userId") String userId, @Field("requestId") String requestId);

    /*   @GET("posts/comments/{postId}")
       Call<PojoFriend> getFriendsByPostId(@Path("userId") String userId);*/
    @GET("users/{userId}/groups")
    Call<PojoGroup> getMyGroupsNyuserId(@Path("userId") String userId);

    @GET("playrequests/{keyword}")
    Call<PojoPlay> getSearchJoinReq(@Path("keyword") String keyword);
    @GET("playrquest/search")
    Call<PojoPlay> getSearchJoinReqest(@Query("text") String keyword);
    @FormUrlEncoded
    @POST("users/requests/accept")
    Call<PojoNoti> acceptByuserid(@Field("userId") String userId,@Field("friendId") String friendId,@Field("requestId") String requestId);

    @FormUrlEncoded
    @POST("users/requests/cancel")
    Call<PojoNoti> cancelByuserid(@Field("userId") String userId,@Field("friendId") String friendId,@Field("requestId") String requestId);
    @FormUrlEncoded
    @POST("events/attendees")
    Call<PojoEvent> attendByuserid(@Field("userName") String userName, @Field("userId") String userId, @Field("eventId") String eventId);

    @FormUrlEncoded
    @POST("join/playrequest")
    Call<PojoPlay> joinPlayReq(@Field("requestId") int requestId, @Field("userId") String userId);
    @FormUrlEncoded
    @PUT("requests")
    Call<PojoPlay> cancelPlayReq(@Field("requestId") int requestId, @Field("userId") String userId, @Field("status") String status);
    @FormUrlEncoded
    @POST("playrequests/filter/{userId}")
    Call<PojoPlay> getAllFilterPlayReq(@Path("userId") String userId, @Field("filterType") String filterType,@Field("filterValue") String filterValue);

    @GET("profession")
    Call<PojoDropValues> getAllProfession();
    @GET("region")
    Call<PojoDropValues> getAllRegions();
    @GET("industry")
    Call<PojoDropValues> getAllIndustry();
    @GET("categories")
    Call<PojoDropValues> getAllVenues();

    @FormUrlEncoded
    @PUT("token")
    Call<PojoUser> saveUpdatedPushId(@Header("userId") String userId, @Field("token") String token);
    @DELETE("posts/{id}")
    Call<PojoPost> deletePostBypostId(@Header("userId") String userId, @Path("id") String id);

}
