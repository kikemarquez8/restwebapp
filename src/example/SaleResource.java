package example;

import data_models.Sale;
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
@Path("/sale")
public class SaleResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSale(@PathParam("id") int id) {
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM sale WHERE id_sale = " + id);
			JSONBuilder rspnse = new JSONBuilder();
			if (!resultSet.next()) {
				rspnse.addProperty("message", "Not Found");
				rspnse.build();
				return Response.status(200).entity(rspnse.JSON()).build();
			} else {
				rspnse.addProperty(resultSet, "saleinf");
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
	public Response setSale(String info) throws ParseException{
		System.out.println(info);
		SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
		Sale novo = new Sale();
		try {
			JSONObject a = new JSONObject(info);
			novo.setId((Integer) a.get("id_product"));
			novo.setDate(date.parse((String) a.get("da_sale")));
			novo.setIdClient((Integer) a.get("id_client"));
		} catch(JSONException e){
			JSONBuilder a = new JSONBuilder();
			a.addProperty("message","bad formatted JSON");
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			sta.execute("INSERT INTO sale VALUES(" + novo.getId() + "," +"'"+ novo.getDate() +"'"+ "," + novo.getIdClient() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("succes").build();

	}
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSale(String info) {
		JSONBuilder a = new JSONBuilder();
		Sale sale = new Sale();
		SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject b = new JSONObject(info);
			sale.setId((Integer) b.get("id_sale"));
			sale.setDate(date.parse((String) b.get("da_sale")));
			sale.setIdClient((Integer) b.get("id_client"));
			sta.executeUpdate("UPDATE sale SET id_sale=" + sale.getId() + "," + "da_sale=" + "'" + sale.getDate() + "'" + "," + "id_client=" + sale.getIdClient() + " WHERE id_sale=" + sale.getId());
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
	public Response deleteSale(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryresponse = statement.executeUpdate("DELETE FROM sale WHERE id_sale = " + id);
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
