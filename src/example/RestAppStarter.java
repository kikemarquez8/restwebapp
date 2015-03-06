package example;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.IOException;

/**
 * Created by Bonsanto on 2/6/2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
public class RestAppStarter {
	public static DataSource dataSource;
	private static PoolProperties p;

	private static final String BASE_URI = "http://localhost:9998/";


	public static void main(String[] args) throws IOException {
		p = new PoolProperties();
		p.setUrl("jdbc:postgresql://localhost:5432/rest_db");
		p.setDriverClassName("org.postgresql.Driver");
		p.setUsername("postgres");
		p.setPassword("7413246");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(60000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors(
				"org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
						"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		dataSource = new DataSource();
		dataSource.setPoolProperties(p);

		HttpServer server = HttpServerFactory.create(BASE_URI);
		server.start();

		System.out.println("Server running");
		System.out.println(BASE_URI+ "client/1");
		System.out.println("Hit return to stop...");
		System.in.read();
		System.out.println("Stopping server");
		server.stop(0);
		System.out.println("Server stopped");
	}
}
