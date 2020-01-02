package com.app.bet.Network_APIs.Network_Calls;

import android.content.Context;
import android.util.Log;

import com.app.bet.Network_APIs.Interfaces.EventsInterface;
import com.app.bet.Utilities.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsCalls {

    private static final String TAG = "EventsCalls";

    public static void getUpcomingEvents(final Context context, String sportID, final List<String> teamList, final Utilities.Callback callback) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.betsapi.com")
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        EventsInterface eventsInterface = retrofit.create(EventsInterface.class);
        Call<JsonObject> call = eventsInterface.getUpcomingEvents(sportID, "25636-0ZqBEnRcRZpOJj");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Response: " + response.body().toString());

                int status = response.body().getAsJsonPrimitive("success").getAsInt();
                List<String> foundTeams = new ArrayList<>();

                if(status == 1) {
                    JsonArray resultsArray = response.body().getAsJsonArray("results");

                    for(int i=0; i<resultsArray.size(); i++) {
                        JsonElement league = resultsArray.get(i).getAsJsonObject().get("league");
                        JsonElement homeTeam = resultsArray.get(i).getAsJsonObject().get("home");
                        JsonElement awayTeam = resultsArray.get(i).getAsJsonObject().get("away");
                        String homeTeamName = homeTeam.getAsJsonObject().get("name").getAsString().toLowerCase();
                        String awayTeamName = awayTeam.getAsJsonObject().get("name").getAsString().toLowerCase();
                        System.out.println("League ID: " + league.getAsJsonObject().get("id"));
                        System.out.println("League Name: " + league.getAsJsonObject().get("name"));

                        System.out.println("Home Team Name: " + homeTeamName);
                        System.out.println("Away Team Name: " + awayTeamName);

                        for(String teamName: teamList) {
                            if(homeTeamName.contains(teamName)) {
                                Log.w(TAG, teamName + " found!");
                                Utilities.createNotification(context, homeTeamName + " found!");
                                foundTeams.add(teamName);
                            }
                            else if(awayTeamName.contains(teamName)) {
                                Log.w(TAG, teamName + " found!");
                                Utilities.createNotification(context, awayTeamName + " found!");
                                foundTeams.add(teamName);
                            }
                        }

                        if(foundTeams.size() > 0) {
                            for(String teamName: foundTeams) {
                                teamList.remove(teamName);
                            }
                        }



                    }
                    callback.onResult("success");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Failed: " + t.getMessage());
            }
        });

    }

}
