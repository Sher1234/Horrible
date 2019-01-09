package info.horriblesubs.sher.api.nyaa;

import java.util.Map;

import info.horriblesubs.sher.api.nyaa.response.Item;
import info.horriblesubs.sher.api.nyaa.response.Lists;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Npi {
    String LINK = "https://sher1234.000webhostapp.com/api/nyaa/";

    @FormUrlEncoded
    @POST("list/null")
    Call<Lists> getList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("list/null")
    Call<Item> getItem(@Field("i") String id);
}