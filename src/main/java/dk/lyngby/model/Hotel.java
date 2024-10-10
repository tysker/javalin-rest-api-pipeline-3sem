package dk.lyngby.model;

import dk.lyngby.dto.HotelDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "hotel")
@NamedQuery(name = "hotel.deleteAllRows", query = "DELETE from Hotel")
@SequenceGenerator(name = "hotel_seq", sequenceName = "hotel_id_seq", allocationSize = 1, initialValue = 1000)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "hotel_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @Column(name = "town", nullable = false)
    private String town;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private HotelCategory hotelCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HotelType hotelType;

    public Hotel(String hotelName, String town, HotelCategory hotelCategory, HotelType hotelType) {
        this.hotelName = hotelName;
        this.town = town;
        this.hotelCategory = hotelCategory;
        this.hotelType = hotelType;
    }

    public Hotel(HotelDto hotelDto) {
        this.hotelName = hotelDto.getHotelName();
        this.town = hotelDto.getTown();
        this.hotelCategory = hotelDto.getHotelCategory();
        this.hotelType = hotelDto.getHotelType();
    }
}
