package dk.lyngby.dao;

import dk.lyngby.model.Hotel;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HotelDaoImpl implements HotelDao {

    private final EntityManagerFactory emf;

    public HotelDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Hotel> getAllHotels() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT d FROM Hotel d", Hotel.class).getResultList();
        }
    }

    @Override
    public Hotel getHotelById(long id) {
        try (var em = emf.createEntityManager()) {
            return em.find(Hotel.class, id);
        }
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(hotel);
            em.getTransaction().commit();
            return hotel;
        }
    }

    @Override
    public boolean updateHotel(long hotelId, Hotel hotelUpdate) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the managed entity
            Hotel hotel = em.find(Hotel.class, hotelId);

            if (hotel == null) {
                return false;
            }

            // Update the managed entity with the new values
            hotel.setHotelName(hotelUpdate.getHotelName());
            hotel.setTown(hotelUpdate.getTown());
            hotel.setHotelCategory(hotelUpdate.getHotelCategory());
            hotel.setHotelType(hotelUpdate.getHotelType());

            em.getTransaction().commit();
        }
        return true;
    }

    @Override
    public void deleteHotel(long id) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);
            em.remove(hotel);
            em.getTransaction().commit();
        }
    }
}
