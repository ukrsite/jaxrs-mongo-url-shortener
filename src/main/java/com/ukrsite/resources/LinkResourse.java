package com.ukrsite.resources;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

@Path("links")
public class LinkResourse {

  private static final MongoCollection<Document> LINK_COLLECTION;

  static {
    final MongoClient mongo = new MongoClient();
    final MongoDatabase db = mongo.getDatabase("links");
    LINK_COLLECTION = db.getCollection("links");
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("{id}")
  public Response getUrlById(final @PathParam("id") String id) {
    if (id == null || id.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
//    final String url = links.get(id);
    final FindIterable<Document> iterable =
            LINK_COLLECTION.find(new Document("id", id));
    final MongoCursor<Document> iterator = iterable.iterator();
    if (!iterator.hasNext()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    final String url = iterator.next().getString("url");
    if (url == null || url == "") {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(url).build();
  }

  @PUT
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response shortUrl(final String longUrl) {
    int attemp = 0;
    while (attemp<5) {

      String id = getRandomId();
      Document newShortDoc = new Document("id", id);
      newShortDoc.put("url", longUrl);
      try {
        LINK_COLLECTION.insertOne(newShortDoc);
        return Response.ok(id).build();
      } catch (MongoWriteException e) {
//        attempt to write failed, ID - exists
      }

      attemp++;
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }

  private static String getRandomId() {
    String passibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder idBuilder = new StringBuilder();
    Random rnd = new Random();
    while (idBuilder.length()<5) {
      int index = (int)(rnd.nextInt() * idBuilder.length());
      idBuilder.append(passibleCharacters.charAt(index));
    }
    return idBuilder.toString();
  }

}
