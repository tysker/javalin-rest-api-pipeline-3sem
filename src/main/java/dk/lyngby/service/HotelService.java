package dk.lyngby.service;

import dk.lyngby.dto.HotelDto;

import java.util.List;

public interface HotelService {

    List<HotelDto> getAllHotels();
    HotelDto getHotelById(long id);
    void createHotel(HotelDto hotelDto);
    boolean updateHotel(long hotelId, HotelDto hotelUpdate);
    void deleteHotel(long id);
}
