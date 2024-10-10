package dk.lyngby.controller;

import dk.lyngby.dto.HotelDto;
import dk.lyngby.exception.ApiException;
import dk.lyngby.model.Message;
import dk.lyngby.service.HotelService;
import io.javalin.http.Context;

import java.util.List;


public class HotelCtrlImpl implements HotelCtrl {

    private final HotelService hotelService;

    public HotelCtrlImpl(HotelService _hotelService) {
        this.hotelService = _hotelService;
    }

    @Override
    public void getHotelById(Context ctx)  {
        // == request ==
        long id = Long.parseLong(ctx.pathParam("id"));

        // == querying ==
        HotelDto hotelDto = hotelService.getHotelById(id);

        if (hotelDto == null) {
            ctx.res().setStatus(404);
            ctx.json(new Message(404, "Hotel not found"), Message.class);
            return;
        }

        // == response ==
        ctx.res().setStatus(200);
        ctx.json(hotelDto, HotelDto.class);
    }

    @Override
    public void getAllHotels(Context ctx) {

        List<HotelDto> hotelDtos = hotelService.getAllHotels();

        if (hotelDtos.isEmpty()) {
            ctx.res().setStatus(404);
            ctx.json(new Message(404, "No hotels found"), Message.class);
            return;
        }

        // == response ==
        ctx.res().setStatus(200);
        ctx.json(hotelDtos, HotelDto.class);

    }

    @Override
    public void createHotel(Context ctx) {
        try {
            // == request ==
            HotelDto hotelDto = ctx.bodyAsClass(HotelDto.class);

            hotelService.createHotel(hotelDto);

            // == response ==
            ctx.res().setStatus(201);
            ctx.json(new Message(201, "Hotel created"), Message.class);

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void updateHotel(Context ctx) {
        // == request ==
        long hotelId = Long.parseLong(ctx.pathParam("id"));
        HotelDto hotelUpdate = ctx.bodyAsClass(HotelDto.class);

        boolean isUpdated = hotelService.updateHotel(hotelId, hotelUpdate);

        if (!isUpdated) {
            ctx.res().setStatus(404);
            ctx.json(new Message(404, "Hotel not found"), Message.class);
            return;
        }

        // == response ==
        ctx.res().setStatus(200);
        ctx.json(new Message(200, "Hotel updated"), Message.class);
    }

    @Override
    public void deleteHotel(Context ctx) {
        // == request ==
        long id = Long.parseLong(ctx.pathParam("id"));

        // == querying ==
        HotelDto dog = hotelService.getHotelById(id);

        if (dog == null) {
            ctx.res().setStatus(404);
            ctx.json(new Message(404, "Hotel not found"), Message.class);
            return;
        }

        hotelService.deleteHotel(id);

        // == response ==
        ctx.res().setStatus(204);
    }
}
