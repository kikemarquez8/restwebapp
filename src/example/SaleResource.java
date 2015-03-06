package example;

import com.sun.scenario.animation.shared.CurrentTime;
import data_models.Sale;
import data_models.SaleProduct;
import org.json.JSONArray;
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
import java.util.Calendar;
import java.util.Iterator;

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
		// First, adding new sale.
		JSONBuilder res = new JSONBuilder();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String dat = date.format(cal.getTime());
		Sale novo = new Sale();
		SaleProduct sale = new SaleProduct();
		try {
			// Getting client JSON and id_client
			JSONObject a = new JSONObject(info);
			novo.setIdClient(a.getInt("id_client"));
			Connection con1 = RestAppStarter.dataSource.getConnection();
			Statement sta = con1.createStatement();
			String querysale = "INSERT INTO sale (da_sale,id_client) VALUES('" + dat + "'" + "," + novo.getIdClient() + ") RETURNING id_sale ; ";
			ResultSet ber = sta.executeQuery(querysale);
			if(ber.next())
				sale.setIdSale((Integer) ber.getObject(1));
			// No need for id_sale (It should be sequence value) currval gets the id value of the inserted record
			//Updating product quantities and sale product added
			JSONObject update_pro = new JSONObject(info);
			update_pro = update_pro.getJSONObject("products");
			Integer [] quantcode = new Integer[3];
			Iterator <?> itr = update_pro.keys();
			while(itr.hasNext()){
				String key = (String) itr.next();
				if(update_pro.get(key) instanceof JSONArray){
					//updating each product quantities  and inserting new record in sale_product table.
					quantcode[0] = (Integer)  update_pro.getJSONArray(key).getInt(0);
					quantcode[1] = (Integer)  update_pro.getJSONArray(key).getInt(1);
					quantcode[2] = (Integer)  update_pro.getJSONArray(key).getInt(2);
					System.out.print("AFTER ASSIGNMENTS");
					Connection con = RestAppStarter.dataSource.getConnection();
					String query = "INSERT INTO sale_product (id_product, id_sale, qt_product) VALUES("+quantcode[1]+","+ sale.getIdSale() +","+quantcode[2]+");";
					query += "UPDATE product SET qt_product="+quantcode[0]+" WHERE id_product="+quantcode[1];
					System.out.print(query);
					Statement saleproductstatement = con.createStatement();
					saleproductstatement.executeUpdate(query);
				}
			}
			res.addProperty("message","succes");
			res.build();
		}catch (JSONException | SQLException e){
			if(e instanceof  JSONException){
				res.addProperty("message","bad formatted JSON");
				res.build();
			}else{
				res.addProperty("message",e.getMessage());
				res.build();
			}
		}finally {
			return Response.status(200).entity(res.JSON()).build();
		}

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
			sale.setId(Integer.parseInt(b.getString("id_sale")));
			sale.setDate(date.parse((String) b.get("da_sale")));
			sale.setIdClient(Integer.parseInt(b.getString("id_client")));
			sta.executeUpdate("UPDATE sale SET id_sale=" + sale.getId() + "," + "da_sale=" + "'" + sale.getDate() + "'" + "," + "id_client=" + sale.getIdClient() + " WHERE id_sale=" + sale.getId());
			a.addProperty("message", "success");
			con.close();
		} catch (JSONException | SQLException e) {
			if(e instanceof  JSONException)
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
