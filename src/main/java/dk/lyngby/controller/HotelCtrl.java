package dk.lyngby.controller;

import io.javalin.http.Context;

public interface HotelCtrl {
    void getHotelById(Context ctx);
    void createHotel(Context ctx);
    void updateHotel(Context ctx);
    void deleteHotel(Context ctx);
    void getAllHotels(Context ctx);

}
