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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rspnse.build();
		}
		return Response.status(200).entity(rspnse.JSON()).build();
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rspnse.build();
		}
		return Response.status(200).entity(rspnse.JSON()).build();
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProduct(String info) {
		Product novo = new Product();
		JSONBuilder rs = new JSONBuilder();

		try {
			JSONObject a = new JSONObject(info);
			System.out.println(a.toString());
			novo.setId(Integer.parseInt(a.getString("id_product")));
			novo.setName(a.getString("na_product"));
			novo.setPurchasePrice(Double.parseDouble(a.getString("pp_product")));
			novo.setSalePrice(Double.parseDouble(a.getString("sp_product")));
			novo.setQuantity(Integer.parseInt(a.getString("qt_product")));
			Connection con = RestAppStarter.dataSource.getConnection();
			Statement sta = con.createStatement();
			sta.execute("INSERT INTO product VALUES(" + novo.getId() + "," + "'" + novo.getName() + "'" + "," + novo.getPurchasePrice() + "," + novo.getSalePrice() + "," + novo.getQuantity() + ")");
			rs.addProperty("message", "success");
			rs.build();
			con.close();
		} catch (SQLException | JSONException e) {
			if (e instanceof JSONException) {
				rs.addProperty("message", "bad formatted JSON");
				rs.build();
			} else {
				rs.addProperty("message", "SQL Exception");
				rs.build();
			}
		}
		return Response.status(200).entity(rs.JSON()).build();
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
			product.setId(Integer.parseInt(b.getString("id_product")));
			product.setName(b.getString("na_product"));
			product.setPurchasePrice(Double.parseDouble(b.getString("pp_product")));
			product.setSalePrice(Double.parseDouble(b.getString("sp_product")));
			product.setQuantity(Integer.parseInt(b.getString("qt_product")));
			sta.executeUpdate("UPDATE product SET id_product=" + product.getId() + "," + "na_product=" + "'" + product.getName() + "'" + "," + "pp_product=" + product.getPurchasePrice() + "," + "sp_product=" + product.getSalePrice() + "," + "qt_product=" + product.getQuantity() + " WHERE id_product=" + product.getId());
			a.addProperty("message", "success");
			con.close();
		} catch (JSONException e) {
			e.printStackTrace();
			a.addProperty("message", "Bad formatted JSON");
		} catch (SQLException e) {
			e.printStackTrace();
			a.addProperty("message", "SQL exception");
		} finally {
			a.build();
		}
		return Response.status(200).entity(a.JSON()).build();
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProduct(@PathParam("id") int id) {
		JSONBuilder res = new JSONBuilder();

		try {
			Connection connection = RestAppStarter.dataSource.getConnection();
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Integer queryResponse = statement.executeUpdate("DELETE FROM product WHERE id_product = " + id);
			if (queryResponse == 1) res.addProperty("message", "Success");
			else res.addProperty("message", "Not found");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			res.addProperty("message", "SQL exception");
		} finally {
			res.build();
		}
		return Response.status(200).entity(res.JSON()).build();
	}
}


