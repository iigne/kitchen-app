package com.kitchenapp.kitchenappapi.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;


@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String email;
    //UNIX epoch seconds
    private long expiry;

    public JwtResponse(String accessToken, int id, String username, String email, int expiryMs) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        //checking expiry in server time
        this.expiry = LocalDateTime.now().plus(expiryMs, ChronoField.MILLI_OF_DAY.getBaseUnit())
                .atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

}
