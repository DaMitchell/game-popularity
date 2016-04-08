package com.damitchell.tgp.service;

import com.damitchell.tgp.model.TopGames;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitchGameService
{
    @GET("games/top")
    Call<TopGames> topGames(@Query("limit") int limit, @Query("offset") int offset);
}
