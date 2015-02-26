package example;

import data_models.Product;
import org.json.JSONException;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by administrator on 2/8/15.
 */
@Path("/product")
public class ProductResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("id") int id) {
		JSONBuilder rspnse = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM product WHERE id_product = " + id);
			if (!resultSet.next()) {
				rspnse.addProperty("message", "Not Found");
			} else {
				rspnse.addProperty(resultSet, "productinf");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			rspnse.build();
			return Response.status(200).entity(rspnse.JSON()).build();
		}
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProducts() {
		JSONBuilder rspnse = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * FROM product");
			if (!resultSet.next()) {
				rspnse.addProperty("message", "Not Found");
			} else {
				rspnse.addProperty(resultSet, "products");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			rspnse.build();
			System.out.print(rspnse.JSON());
			return Response.status(200).entity(rspnse.JSON()).build();
		}
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProduct(String info) {
		System.out.println(info);
		Product novo = new Product();
		try {
			JSONObject a = new JSONObject(info);
			novo.setId((Integer) a.get("id_product"));
			novo.setName((String) a.get("na_product"));
			novo.setPurchasePrice((Double) a.get("pp_product"));
			novo.setSalePrice((Double) a.get("sp_product"));
			novo.setQuantity((Integer) a.get("qt_product"));
		} catch (JSONException e) {
			JSONBuilder a = new JSONBuilder();
			a.addProperty("message", "bad formatted JSON");
			a.build();
			return Response.status(200).entity(a.JSON()).build();
		}
		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			sta.execute("INSERT INTO product VALUES(" + novo.getId() + "," + "'" + novo.getName() + "'" + "," + novo.getPurchasePrice() + "," + novo.getSalePrice() + "," + novo.getQuantity() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("success").build();

	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProduct(String info) {
		JSONBuilder a = new JSONBuilder();
		Product product = new Product();

		try {
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			JSONObject b = new JSONObject(info);
			product.setId((Integer) b.get("id_product"));
			product.setName((String) b.get("na_product"));
			product.setPurchasePrice((Double) b.get("pp_product"));
			product.setSalePrice((Double) b.get("sp_product"));
			product.setQuantity((Integer) b.get("qt_product"));
			sta.executeUpdate("UPDATE product SET id_product=" + product.getId() + "," + "na_product=" + "'" + product.getName() + "'" + "," + "pp_product=" + product.getPurchasePrice() + "," + "sp_product=" + product.getSalePrice() + "," + "qt_product=" + product.getQuantity() + " WHERE id_product=" + product.getId());
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
	public Response deleteProduct(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();
		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryresponse = statement.executeUpdate("DELETE FROM product WHERE id_product = " + id);
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


