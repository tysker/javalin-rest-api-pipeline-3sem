package dk.lyngby.routes;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.HotelCtrl;
import dk.lyngby.controller.HotelCtrlImpl;
import dk.lyngby.dao.HotelDao;
import dk.lyngby.dao.HotelDaoImpl;
import dk.lyngby.security.model.Role;
import dk.lyngby.service.HotelService;
import dk.lyngby.service.HotelServiceImpl;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoute {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final HotelDao hotelDao = new HotelDaoImpl(emf);
    private final HotelService hotelService = new HotelServiceImpl(hotelDao);
    private final HotelCtrl hotelCtrl = new HotelCtrlImpl(hotelService);

    public EndpointGroup getDogRoutes() {
        return () -> {
            path("/", () -> {
                get("/", hotelCtrl::getAllHotels, Role.RoleName.ANYONE);
                get("{id}", hotelCtrl::getHotelById, Role.RoleName.USER, Role.RoleName.MANAGER);
                post(hotelCtrl::createHotel, Role.RoleName.ADMIN);
                put("{id}", hotelCtrl::updateHotel, Role.RoleName.ADMIN);
                delete("{id}", hotelCtrl::deleteHotel, Role.RoleName.ADMIN);
            });
        };
    }
}
