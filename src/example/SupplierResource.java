package example;

import data_models.Supplier;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import example.JSONBuilder;
/**
 * Created by administrator on 2/8/15.
 */
@Path("/supplier")
public class SupplierResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSupplier(@PathParam("id") int id) {
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM supplier WHERE id_supplier = " + id);
			JSONBuilder rspnse = new JSONBuilder();
			if (!resultSet.next()) {
				rspnse.addProperty("message", "Not Found");
				rspnse.build();
				return Response.status(200).entity(rspnse.JSON()).build();
			} else {
				rspnse.addProperty(resultSet, "supplierinf");
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
	public Response setSupplier(String info) {
		System.out.println(info);
		Supplier novo = new Supplier();
		try {
			JSONObject a = new JSONObject(info);
			novo.setId((Integer) a.get("id_supplier"));
			novo.setName((String) a.get("na_supplier"));
			novo.setZip((Integer) a.get("zip_supplier"));
		} catch(JSONException e){
			JSONBuilder a = new JSONBuilder();
			a.addProperty("message","bad formatted JSON");
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			sta.execute("INSERT INTO supplier VALUES(" + novo.getId() + "," +"'"+ novo.getName() +"'"+ "," + novo.getZip() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("succes").build();

	}
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSupplier(String info) {
		JSONBuilder a = new JSONBuilder();
		Supplier supplier = new Supplier();
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject b = new JSONObject(info);
			supplier.setId((Integer) b.get("id_supplier"));
			supplier.setName((String) b.get("na_supplier"));
			supplier.setZip((Integer) b.get("zip_supplier"));
			sta.executeUpdate("UPDATE supplier SET id_supplier=" + supplier.getId() + "," + "na_supplier=" + "'" + supplier.getName() + "'" + "," + "zip_supplier=" + supplier.getZip() + " WHERE id_supplier=" + supplier.getId());
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
	public Response deleteSupplier(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryresponse = statement.executeUpdate("DELETE FROM supplier WHERE id_supplier = " + id);
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