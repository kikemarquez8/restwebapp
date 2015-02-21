package example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by Bonsanto on 2/7/2015.
 */
public class RsXMLConverter {
	public static Document toDocument(ResultSet resultSet, String tagName) throws ParserConfigurationException, SQLException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element result = doc.createElement(tagName);
		doc.appendChild(result);

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columns = rsmd.getColumnCount();

		while (resultSet.next()){
			for (int i = 1; i <= columns; i++) {
				Element node = doc.createElement(rsmd.getColumnName(i));
				node.appendChild(doc.createTextNode(resultSet.getObject(i).toString()));
				doc.appendChild(node);
			}
		}
		return doc;
	}
}
