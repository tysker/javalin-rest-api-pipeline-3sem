package dk.lyngby.dto;

import dk.lyngby.model.HotelCategory;
import dk.lyngby.model.Hotel;
import dk.lyngby.model.HotelType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class HotelDto {

    private String hotelName;
    private String town;
    private HotelCategory hotelCategory;
    private HotelType hotelType;

    public HotelDto(Hotel hotel) {
        this.hotelName = hotel.getHotelName();
        this.town = hotel.getTown();
        this.hotelCategory = hotel.getHotelCategory();
        this.hotelType = hotel.getHotelType();
    }

    public static List<HotelDto> toDogDTOList(List<Hotel> hotels) {
        return hotels.stream().map(HotelDto::new).toList();
    }
}
