package com.smartcampus.resources;

import com.smartcampus.models.Room;
import com.smartcampus.exceptions.SmartException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/rooms")
public class RoomResource {
    private static Map<String, Room> database = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return new ArrayList<>(database.values());
    }

    // TASK: SEARCH BY ID (Full Mark requirement)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("id") String id) {
        if (!database.containsKey(id)) {
            // This triggers your SmartException (404)
            throw new SmartException("Room with ID " + id + " does not exist.", 404);
        }
        return Response.ok(database.get(id)).build();
    }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response addRoom(Room room) {
    if (room == null || room.getId() == null) {
        return Response.status(400).entity("Data is missing").build();
    }
    
    database.put(room.getId(), room);
    
    // We return a simple String here to stop the 500 error
    return Response.status(201).entity("Room " + room.getId() + " added successfully").build();
 }
}