package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.http.ContentType;

public class EnvioDadosTest {
	@Test 
	public void deveEnviarValorViaQuery() {
		given().log().all()
		.contentType(ContentType.JSON)
		.when()
		.get(getUsersXMLEndpoint("format", "json"))
		.then().log().all().assertThat()
		.statusCode(HttpStatus.SC_OK)
		.contentType(ContentType.JSON)
		//.body("user.@id", is(notNullValue()))
		//.body("user.name", is("Jose")) //we have faced issues with encoding
		//when we use utf-8 belonged chars, such as in 'José'.
		
		//.body("user.age", greaterThan(new Integer(0)))
		//.body("age", is(50)) //it doesn't work: xlm numeric values will be send as String	
		//.body("user.age", is("50"))
		;
	}

	private String getParamEqualsToValueString(String parameter, String value) {
		return String.format("?%s=%s", parameter, value);
	}
	
	private String getUsersXMLEndpoint() {
		// TODO Auto-generated method stub
		return "https://restapi.wcaquino.me/v2/users";
	}
	private URI getUsersXMLEndpoint(String parameter, String value) {
		// TODO Auto-generated method stub
		return URI.create(getUsersXMLEndpoint().concat(getParamEqualsToValueString(parameter, value)));
	}
}
