package dk.lyngby.service;

import dk.lyngby.dao.HotelDao;
import dk.lyngby.dto.HotelDto;
import dk.lyngby.model.Hotel;

import java.util.List;

public class HotelServiceImpl implements HotelService {

    private final HotelDao hotelDao;

    public HotelServiceImpl(HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }

    @Override
    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelDao.getAllHotels();
        return HotelDto.toDogDTOList(hotels);
    }

    @Override
    public HotelDto getHotelById(long id) {
        Hotel hotel = hotelDao.getHotelById(id);
        return new HotelDto(hotel);
    }

    @Override
    public void createHotel(HotelDto hotelDto) {
        hotelDao.createHotel(new Hotel(hotelDto));
    }

    @Override
    public boolean updateHotel(long hotelId, HotelDto hotelUpdate) {
        return hotelDao.updateHotel(hotelId, new Hotel(hotelUpdate));
    }

    @Override
    public void deleteHotel(long id) {
        hotelDao.deleteHotel(id);
    }
}
