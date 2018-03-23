package com.garuda.hcmobile;

//import com.wrun.drivemehome.customer.model.AddBookmarkRequest;
//import com.wrun.drivemehome.customer.model.AddRatingRequest;
//import com.wrun.drivemehome.customer.model.AuthenticateResponse;
//import com.wrun.drivemehome.customer.model.BookingHistoryRequest;
//import com.wrun.drivemehome.customer.model.BookingHistoryResponse;
//import com.wrun.drivemehome.customer.model.BookingRequest;
//import com.wrun.drivemehome.customer.model.BookingResponse;
//import com.wrun.drivemehome.customer.model.BookingStatusRequest;
//import com.wrun.drivemehome.customer.model.BookingStatusResponse;
//import com.wrun.drivemehome.customer.model.BookingTrackingRequest;
//import com.wrun.drivemehome.customer.model.BookingTrackingResponse;
//import com.wrun.drivemehome.customer.model.BookmarkRequest;
//import com.wrun.drivemehome.customer.model.CancelBookingRequest;
//import com.wrun.drivemehome.customer.model.ChannelRequest;

import com.garuda.hcmobile.model.AuthenticateResponse;
import com.garuda.hcmobile.model.ClockInOutRequest;
import com.garuda.hcmobile.model.CommonRequest;
import com.garuda.hcmobile.model.CommonResponse;
import com.garuda.hcmobile.model.EmpRequest;
import com.garuda.hcmobile.model.FCMTokenRequest;
import com.garuda.hcmobile.model.GCMTokenRequest;
import com.garuda.hcmobile.model.GetSupportRequest;
import com.garuda.hcmobile.model.HCNewsResponse;
import com.garuda.hcmobile.model.LaunchResponse;
import com.garuda.hcmobile.model.LeaveCancelRequest;
import com.garuda.hcmobile.model.LeaveDataRequest;
import com.garuda.hcmobile.model.LeaveDataResponse;
import com.garuda.hcmobile.model.LeaveProposeRequest;
import com.garuda.hcmobile.model.LeaveResponseRequest;
import com.garuda.hcmobile.model.LocationCheckResponse;
import com.garuda.hcmobile.model.ManAttCancelRequest;
import com.garuda.hcmobile.model.ManAttDataRequest;
import com.garuda.hcmobile.model.ManAttDataResponse;
import com.garuda.hcmobile.model.ManAttProposeRequest;
import com.garuda.hcmobile.model.ManAttResponseRequest;
import com.garuda.hcmobile.model.OvertimeDataRequest;
import com.garuda.hcmobile.model.OvertimeDataResponse;
import com.garuda.hcmobile.model.OvertimeProposeRequest;
import com.garuda.hcmobile.model.OvertimeResponseDataRequest;
import com.garuda.hcmobile.model.RegisterRequest;
import com.garuda.hcmobile.model.RegisterResponse;
import com.garuda.hcmobile.model.SlipCheckSpcResponse;
import com.garuda.hcmobile.model.SlipContentRequest;
import com.garuda.hcmobile.model.SlipContentResponse;
import com.garuda.hcmobile.model.SlipKeyDataRequest;
import com.garuda.hcmobile.model.SlipMonthDataRequest;
import com.garuda.hcmobile.model.SlipMonthResponse;
import com.garuda.hcmobile.model.SlipYearResponse;
import com.garuda.hcmobile.model.SubordinatResponse;
import com.garuda.hcmobile.model.TMSInitResponse;
import com.garuda.hcmobile.model.TimeDataRequest;
import com.garuda.hcmobile.model.TimeDataResponse;
import com.garuda.hcmobile.model.VerifiedResponse;
import com.garuda.hcmobile.model.VerifyRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

//import com.wrun.drivemehome.customer.model.FindDriverRequest;
//import com.wrun.drivemehome.customer.model.GetPriceRequest;
//import com.wrun.drivemehome.customer.model.GetPriceResponse;
//import com.wrun.drivemehome.customer.model.GetProfileRequest;
//import com.wrun.drivemehome.customer.model.GetProfileResponse;
//import com.wrun.drivemehome.customer.model.GlobalContentRequest;
//import com.wrun.drivemehome.customer.model.GlobalContentResponse;
//import com.wrun.drivemehome.customer.model.NearbyDriverRequest;
//import com.wrun.drivemehome.customer.model.NearbyDriverResponse;
//import com.wrun.drivemehome.customer.model.PaymentMethodRequest;
//import com.wrun.drivemehome.customer.model.ProfileRequest;
//import com.wrun.drivemehome.customer.model.RegisterRequest;
//import com.wrun.drivemehome.customer.model.RegisterResponse;
//import com.wrun.drivemehome.customer.model.UpdatePictureRequest;
//import com.wrun.drivemehome.customer.model.UpdateProfileRequest;
//import com.wrun.drivemehome.customer.model.VerifiedResponse;
//import com.wrun.drivemehome.customer.model.VerifyRequest;


public interface Api {


    @POST("/register")
    void register(@Body RegisterRequest registerRequest,
                  Callback<RegisterResponse> responseCallback);

    @POST("/request_otp")
    void requestOTP(@Body RegisterRequest registerRequest,
                    Callback<RegisterResponse> responseCallback);

    @POST("/verify_otp")
    void verifiedOTP(@Body VerifyRequest verifyRequest,
                     Callback<VerifiedResponse> responseCallback);

    @FormUrlEncoded
    @POST("/authenticate")
    void authenticate(@Field("device_type") String device_type,
                      @Field("device_id") String device_id,
                      @Field("password") String password,
                      @Field("nopeg") String nopeg,
                      @Field("date_time") String date_time,
                      Callback<AuthenticateResponse> responseCallback);

    @POST("/profile/launch_data")
    void getLaunchData(@Body EmpRequest request, @Query("token") String token,
                       Callback<LaunchResponse> responseCallback) ;

    @POST("/tms/check_location")
    void checkLocationClockInOut(@Body ClockInOutRequest request, @Query("token") String token,
                       Callback<LocationCheckResponse> responseCallback) ;

    @POST("/tms/self_clock_in_out")
    void setClockInOut(@Body ClockInOutRequest request, @Query("token") String token,
                       Callback<CommonResponse> responseCallback) ;

    @POST("/set_token_gcm")
    void setTokenGCM(@Body GCMTokenRequest request, @Query("token") String token,
                     Callback<CommonResponse> responseCallback) ;

    @POST("/set_token_fcm")
    void setTokenFCM(@Body FCMTokenRequest request, @Query("token") String token,
                     Callback<CommonResponse> responseCallback) ;

    @POST("/tms/initiate")
    void getTMSInitData(@Body CommonRequest request, @Query("token") String token,
                        Callback<TMSInitResponse> responseCallback) ;

    @POST("/tms/time_data")
    void getSelfTimeData(@Body TimeDataRequest request, @Query("token") String token,
                         Callback<TimeDataResponse> responseCallback) ;
    @POST("/tms/leave")
    void getListLeaveRequest(@Body LeaveDataRequest request, @Query("token") String token,
                             Callback<LeaveDataResponse> responseCallback) ;
    @POST("/tms/propose_leave")
    void proposeLeaveRequest(@Body LeaveProposeRequest request, @Query("token") String token,
                             Callback<CommonResponse> responseCallback) ;
    @POST("/tms/cancel_leave")
    void cancelLeaveRequest(@Body LeaveCancelRequest request, @Query("token") String token,
                            Callback<CommonResponse> responseCallback) ;




    @POST("/tms/manatt")
    void getListManAttRequest(@Body ManAttDataRequest request, @Query("token") String token,
                              Callback<ManAttDataResponse> responseCallback) ;
    @POST("/tms/cancel_manatt")
    void cancelManAttRequest(@Body ManAttCancelRequest request, @Query("token") String token,
                             Callback<CommonResponse> responseCallback);
    @POST("/tms/propose_manatt")
    void proposeManAttRequest(@Body ManAttProposeRequest request, @Query("token") String token,
                              Callback<CommonResponse> responseCallback);
    @POST("/tms/overtime")
    void getListOvertimeRequest(@Body OvertimeDataRequest request, @Query("token") String token,
                                Callback<OvertimeDataResponse> responseCallback) ;
    @POST("/tms/propose_overtime")
    void proposeOvertimeRequest(@Body OvertimeProposeRequest request, @Query("token") String token,
                                Callback<CommonResponse> responseCallback) ;




    //SUPERIOR
    @POST("/tms/subordinat")
    void getSubordinat(@Body CommonRequest request, @Query("token") String token,
                       Callback<SubordinatResponse> responseCallback) ;

    @POST("/tms/subs_time_data")
    void getSubTimeData(@Body TimeDataRequest request, @Query("token") String token,
                        Callback<TimeDataResponse> responseCallback) ;
    @POST("/tms/subs_overtime")
    void getSubOvertimeRequest(@Body OvertimeDataRequest request, @Query("token") String token,
                               Callback<OvertimeDataResponse> responseCallback) ;
    @POST("/tms/response_overtime")
    void overtimeResponseRequest(@Body OvertimeResponseDataRequest request, @Query("token") String token,
                                 Callback<CommonResponse> responseCallback) ;
    @POST("/tms/subs_leave")
    void getSubsLeaveData(@Body LeaveDataRequest request, @Query("token") String token,
                          Callback<LeaveDataResponse> responseCallback) ;
    @POST("/tms/response_leave")
    void leaveResponseRequest(@Body LeaveResponseRequest request, @Query("token") String token,
                              Callback<CommonResponse> responseCallback) ;
    @POST("/tms/subs_manatt")
    void getSubsManAttData(@Body ManAttDataRequest request, @Query("token") String token,
                           Callback<ManAttDataResponse> responseCallback) ;
    @POST("/tms/response_manatt")
    void manattResponseRequest(@Body ManAttResponseRequest request, @Query("token") String token,
                               Callback<CommonResponse> responseCallback) ;

    //NEWS
    @POST("/hc/news")
    void getHCNews(@Body CommonRequest request, @Query("token") String token,
                        Callback<HCNewsResponse> responseCallback) ;


    //SLIP GAJI
    @POST("/slip/check")
    void getCheckSlipSpc(@Body CommonRequest request, @Query("token") String token,
                   Callback<SlipCheckSpcResponse> responseCallback) ;
    @POST("/slip/check_key")
    void getCheckSlipKey(@Body SlipKeyDataRequest request, @Query("token") String token,
                         Callback<CommonResponse> responseCallback) ;
    @POST("/slip/get_year")
    void getSlipYear(@Body SlipKeyDataRequest request, @Query("token") String token,
                         Callback<SlipYearResponse> responseCallback) ;
    @POST("/slip/get_month")
    void getSlipMonth(@Body SlipMonthDataRequest request, @Query("token") String token,
                      Callback<SlipMonthResponse> responseCallback) ;

    @POST("/slip/get_content")
    void getSlipContent(@Body SlipContentRequest request, @Query("token") String token,
                        Callback<SlipContentResponse> responseCallback) ;
    //SLIP FATA

    @POST("/slip_fata/get_year")
    void getSlipFATAYear(@Body SlipKeyDataRequest request, @Query("token") String token,
                     Callback<SlipYearResponse> responseCallback) ;
    @POST("/slip_fata/get_month")
    void getSlipFATAMonth(@Body SlipMonthDataRequest request, @Query("token") String token,
                      Callback<SlipMonthResponse> responseCallback) ;

    @POST("/slip_fata/get_content")
    void getSlipFATAContent(@Body SlipContentRequest request, @Query("token") String token,
                        Callback<SlipContentResponse> responseCallback) ;

//getSubsManAttData
//    @POST("/customer/booking")
//    public void bookingDriver(@Body BookingRequest request, @Query("token") String token,
//                              Callback<BookingResponse> responseCallback) ;
//
//    @POST("/customer/booking_payment_method")
//    public void setPaymentMethod(@Body PaymentMethodRequest request, @Query("token") String token, Callback<CommonResponse> commonResponseCallback);
//
//    @POST("/customer/booking_find_driver")
//    public void findDriver(@Body FindDriverRequest findDriverRequest, @Query("token") String token, Callback<CommonResponse> responseCallback);
//
//    @POST("/customer/get_price")
//    public void getPrice(@Body GetPriceRequest request, @Query("token") String token,
//                         Callback<GetPriceResponse> responseCallback) ;
//
//    @POST("/customer/update_channel")
//    public void updateChannel(@Body ChannelRequest request, @Query("token") String token, Callback<CommonResponse> responseCallback);
//
//    @POST("/customer/booking_status")
//    public void bookingStatus(@Body BookingStatusRequest request, @Query("token") String token, Callback<BookingStatusResponse> callback);
//
//    @POST("/customer/booking_tracking_driver")
//    public void bookTrackingDriver(@Body BookingTrackingRequest bookingTrackingRequest, @Query("token") String token, Callback<BookingTrackingResponse> responseCallback);
//
//
//    @POST("/customer/nearby_driver")
//    public void getNearbyDriver(@Body NearbyDriverRequest nearbyDriverRequest, @Query("token") String token, Callback<NearbyDriverResponse> callback);
//
//    @POST("/customer/add_favloc")
//    public void bookmarkLocation(@Body AddBookmarkRequest request, @Query("token") String token, Callback<CommonResponse> commonResponseCallback);
//
//    @POST("/global/get_content")
//    public void getGLobalContent(@Body GlobalContentRequest globalContentRequest, Callback<GlobalContentResponse> callback);
//
//    //TODO FIX AFTER SERVER FIX (bagian responsenya)
//    @POST("/customer/get_all_favloc")
//    public void getBookmark(@Body BookmarkRequest bookmarkRequest, @Query("token") String token, Callback<CommonResponse> commonResponseCallback);
//
//
//    @POST("/customer/add_rating")
//    public void addRating(@Body AddRatingRequest ratingRequest, @Query("token") String token, Callback<CommonResponse> responseCallback);
//
//
//    @POST("/customer/booking_history")
//    public void bookingHistory(@Body BookingHistoryRequest request, @Query("token") String token, Callback<BookingHistoryResponse> responseCallback);
//
//    @POST("/customer/booking_cancel")
//    public void bookingCancel(@Body CancelBookingRequest cancelBookingRequest, @Query("token") String token, Callback<CommonResponse> commonResponseCancelBookingRequest);
//
//
//    @POST("/customer/update_profil")
//    public void updateProfile(@Body UpdateProfileRequest request, @Query("token") String token, Callback<CommonResponse> responseCallback);
//
//
//    @POST("/customer/get_profil")
//    public void getProfile(@Body GetProfileRequest request, @Query("token") String token, Callback<GetProfileResponse> responseCallback);
//
//    @POST("/customer/update_pic")
//    public void updatePicture(@Body UpdatePictureRequest request, @Query("token") String token, Callback<CommonResponse> responseCallback);
//
    @POST("/driver/ver_get_support")
    void getSupport(@Body GetSupportRequest request, Callback<CommonResponse> callback);

}
