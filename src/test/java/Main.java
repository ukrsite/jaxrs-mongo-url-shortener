import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

import static java.lang.System.in;

public class Main {
//  Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8081/api";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * @return Grizzly HTTP server
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("com.ukrsite.resources");

    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) throws IOException {
    final HttpServer httpServer = startServer();
//    out.printf("Jersey app started with WADL available at" +
//              "%app.wadl\nHit enter to stop it...", BASE_URI);
    in.read();
    httpServer.stop();

  }
}
