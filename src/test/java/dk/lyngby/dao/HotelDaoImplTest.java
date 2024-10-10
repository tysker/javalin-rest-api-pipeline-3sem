package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.HotelCategory;
import dk.lyngby.model.Hotel;
import dk.lyngby.model.HotelType;
import dk.lyngby.util.ApiProps;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelDaoImplTest {

    private static EntityManagerFactory emf;
    private static HotelDao hotelDao;

    @BeforeAll
    static void setUp() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactory();
        hotelDao = new HotelDaoImpl(emf);
    }

    @BeforeEach
    void init() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery(ApiProps.SQL_INSERT_HOTELS).executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown() {
        HibernateConfig.setTest(false);
    }

    @Test
    void getAllHotels() {
        // given
        int expected = 10;
        // when
        List<Hotel> allHotels = hotelDao.getAllHotels();
        // then
        assertEquals(expected, allHotels.size());
    }

    @Test
    void getHotelById() {
        // given
        int id = 1000;
        Hotel expected = new Hotel("Sunset Resort", "Copenhagen", HotelCategory.LUXURY, HotelType.FIVE_STAR);
        // when
        Hotel actual = hotelDao.getHotelById(id);
        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getHotelName(), actual.getHotelName()),
                () -> assertEquals(expected.getTown(), actual.getTown()),
                () -> assertEquals(expected.getHotelCategory(), actual.getHotelCategory()),
                () -> assertEquals(expected.getHotelType(), actual.getHotelType())
        );
    }

    @Test
    void createHotel() {
        // given
        Hotel expected = new Hotel("Sunset Resort", "Copenhagen", HotelCategory.LUXURY, HotelType.FIVE_STAR);
        // when
        Hotel actual = hotelDao.createHotel(expected);
        // then

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getHotelName(), actual.getHotelName()),
                () -> assertEquals(expected.getTown(), actual.getTown()),
                () -> assertEquals(expected.getHotelCategory(), actual.getHotelCategory()),
                () -> assertEquals(expected.getHotelType(), actual.getHotelType())
        );
    }

    @Test
    void updateHotel() {
        // given
        Hotel hotelUpdate = new Hotel("UPDATE", "UPDATE", HotelCategory.BUDGET, HotelType.ONE_STAR);
        // when
        long oldHotelId = 1001;
        boolean isUpdated = hotelDao.updateHotel(oldHotelId, hotelUpdate);
        // then
        assertTrue(isUpdated);
    }

    @Test
    void deleteHotel() {
        // given
        int id = 1002;
        // when
        hotelDao.deleteHotel(id);
        // then
        assertNull(emf.createEntityManager().find(Hotel.class, id));
    }
}