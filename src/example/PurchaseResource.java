package example;

import data_models.Purchase;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import example.JSONBuilder;
/**
 * Created by administrator on 2/8/15.
 */
@Path("/purchase")
public class PurchaseResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchase(@PathParam("id") int id) {
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM purchase WHERE id_purchase = " + id);
			JSONBuilder rspnse = new JSONBuilder();
			if (!resultSet.next()) {
				rspnse.addProperty("message", "Not Found");
				rspnse.build();
				return Response.status(200).entity(rspnse.JSON()).build();
			} else {
				rspnse.addProperty(resultSet, "purchaseinf");
				rspnse.build();
				return Response.status(200).entity(rspnse.JSON()).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setPurchase(String info) throws ParseException{
		System.out.println(info);
		SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
		Purchase novo = new Purchase();
		try {
			JSONObject a = new JSONObject(info);
			novo.setId((Integer) a.get("id_purchase"));
			novo.setDate(date.parse((String) a.get("da_sale")));
			novo.setIdSupplier((Integer) a.get("id_supplier"));
		} catch(JSONException e){
			JSONBuilder a = new JSONBuilder();
			a.addProperty("message","bad formatted JSON");
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			sta.execute("INSERT INTO purchase VALUES(" + novo.getId() + "," +"'"+ novo.getDate() +"'"+ "," + novo.getIdSupplier() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("succes").build();

	}
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePurchase(String info) {
		JSONBuilder a = new JSONBuilder();
		Purchase purchase = new Purchase();
		SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject b = new JSONObject(info);
			purchase.setId((Integer) b.get("id_purchase"));
			purchase.setDate(date.parse((String) b.get("da_purchase")));
			purchase.setIdSupplier((Integer) b.get("id_supplier"));
			sta.executeUpdate("UPDATE purchase SET id_purchase=" + purchase.getId() + "," + "da_purchase=" + "'" + purchase.getDate() + "'" + "," + "id_supplier=" + purchase.getIdSupplier() + " WHERE id_purchase=" + purchase.getId());
			a.addProperty("message", "success");
			con.close();
		} catch (JSONException e) {
			e.printStackTrace();
			a.addProperty("message", "bad formatted JSON");
		} catch (SQLException e) {
			e.printStackTrace();
			a.addProperty("message", "SQL exception");
		} finally {
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
	}
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePurchase(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryresponse = statement.executeUpdate("DELETE FROM purchase WHERE id_purchase = " + id);
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

