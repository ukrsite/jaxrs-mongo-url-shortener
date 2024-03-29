package com.ukrsite.resourses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;




@Path("links")
public class LinkResourceTest {

  private static final AtomicInteger currentId = new AtomicInteger();
  private static final Map<String, String> links = new ConcurrentHashMap<>();

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("{id}")
  public Response getUrlById(final @PathParam("id") String id) {
    if (id == null || id.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    final String url = links.get(id);
    if (url == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(url).build();
  }

  @PUT
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response shortUrl(final String longUrl) {

    final int id = currentId.getAndIncrement();
    links.put(String.valueOf(id), longUrl.toString());
    return Response.ok(id).build();
  }
}
