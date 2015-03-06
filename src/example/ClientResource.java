package example;

import data_models.Client;
import org.json.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import example.JSONBuilder;

/**
 * Created by Bonsanto on 2/7/2015.
 */
@Path("/client")
public class ClientResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClient(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();

		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM client WHERE id_client = " + id);

			if (!resultSet.next()) res.addProperty("message", "Not Found");
			else res.addProperty(resultSet, "clientinf");

			connection.close();
		} catch (SQLException e) {
			res.addProperty("message", "sql exception");
			e.printStackTrace();
		} finally {
			res.build();
			return Response.status(200).entity(res.JSON()).build();
		}
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setClient(String info) {
		JSONBuilder a = new JSONBuilder();
		Client client = new Client();

		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject jsonObject = new JSONObject(info);
			client.setId(Integer.parseInt(jsonObject.getString("id_client")));
			client.setFirstMame(jsonObject.getString("na_client"));
			client.setLastName(jsonObject.getString("la_client"));
			client.setCi(Integer.parseInt(jsonObject.getString("ci_client")));
			sta.executeUpdate("INSERT INTO client VALUES(" + client.getId() + "," + "'" + client.getFirstMame() + "'" + "," + "'" + client.getLastName() + "'" + "," + client.getCi() + ")");
			con.close();
			a.addProperty("message", "success");
			a.build();
		}  catch (JSONException | SQLException e) {
			if(e instanceof JSONException)
				a.addProperty("message", "bad formatted JSON");
			else
				a.addProperty("message", "SQL exception");
		}  finally {
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateClient(String info) {
		JSONBuilder a = new JSONBuilder();
		Client client = new Client();

		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject b = new JSONObject(info);
			client.setId(Integer.parseInt(b.getString("id_client")));
			client.setFirstMame(b.getString("na_client"));
			client.setLastName(b.getString("la_client"));
			client.setCi(Integer.parseInt(b.getString("ci_client")));
			sta.executeUpdate("UPDATE client SET id_client=" + client.getId() + "," + "na_client=" + "'" + client.getFirstMame() + "'" + "," + "la_client=" + "'" + client.getLastName() + "'" + "," + "ci_client=" + client.getCi() + " WHERE id_client=" + client.getId());
			a.addProperty("message", "success");
			con.close();
		} catch (JSONException | SQLException e) {
			if(e instanceof JSONException)
				a.addProperty("message", "bad formatted JSON");
			else
				a.addProperty("message", "SQL exception");
		} finally {
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteClient(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryresponse = statement.executeUpdate("DELETE FROM client WHERE id_client = " + id);
			if (queryresponse==1) res.addProperty("message", "success"); else res.addProperty("message", "not found");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			res.addProperty("message", "sql exception");
		} finally {
			res.build();
			return Response.status(200).entity(res.JSON()).build();
		}
	}
}

