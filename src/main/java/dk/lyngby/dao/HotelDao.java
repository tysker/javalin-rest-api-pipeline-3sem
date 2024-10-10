package dk.lyngby.dao;

import dk.lyngby.model.Hotel;

import java.util.List;

public interface HotelDao {
    List<Hotel> getAllHotels();
    Hotel getHotelById(long id);
    Hotel createHotel(Hotel hotel);
    boolean updateHotel(long hotelId, Hotel hotelUpdate);
    void deleteHotel(long id);
}
