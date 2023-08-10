package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import   io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

public class SchemaTest {

	@Test
	public void deveValidarSchemaXml() {
		given().log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")	
		.then().log().all()
			.statusCode(HttpStatus.SC_OK)
			
			//CRUCUAL: the underlying string value conveys the path below src/main/resources/
			//pointing to the .xsd validator file.
		.body(RestAssuredMatchers.matchesXsdInClasspath("xml/UsersSchema.xsd"))
			
			//it doesn't work: matchesXsdInClasspath takes as shortened path only from scr/main/resources!
			//even though the string path were a full one!
			//.body(RestAssuredMatchers.matchesXsdInClasspath("src/main/resources/xml/Users.xsd"))
			;
	}
	
	@Test(expected = SAXParseException.class)
	public void naoDeveValidarSchemaXmlInvalido() {
		given().log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidUsersXML/")
		.then().log().all()
			.statusCode(HttpStatus.SC_OK)
			.body(RestAssuredMatchers.matchesXsdInClasspath("xml/UsersSchema.xsd"))
		;
	}
	
	@Test()
	public void deveValidarSchemaJsonValido() {
		given().log().all()
		.when()
		.get("https://restapi.wcaquino.me/users")
		.then().log().all()
		.statusCode(HttpStatus.SC_OK)
		.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/UsersSchema.json"))
		;
	}
}
