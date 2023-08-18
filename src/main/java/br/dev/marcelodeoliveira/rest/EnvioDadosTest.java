package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.net.URI;

import org.apache.commons.codec.CharEncoding;
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
		.then()
			.log().all().assertThat()
		.statusCode(HttpStatus.SC_OK)
		.contentType(ContentType.JSON)
		;
	}
	@Test 
	public void deveEnviarValorViaQueryVaParams() {
		given().log().all()
		.contentType(ContentType.XML.withCharset(CharEncoding.UTF_8))
		.queryParams("format", "xml")

		//inutile queryParams
		.queryParams("Server", "MICHIGAN")
		.queryParams("colour", "teal")
		.queryParams("dogRace", "Schnauzer")
		
		.when()
		.get(getUsersXMLEndpoint())
		.then()
		.log().all().assertThat()
		.statusCode(HttpStatus.SC_OK)
		.contentType(containsString("utf-8"))
		;
	}

	private String getParamEqualsToValueString(String parameter, String value) {
		return String.format("?%s=%s", parameter, value);
	}
	
	@Test 
	public void deveEnviarValorViaQueryViaParams() {
		given().log().all()
		.contentType(ContentType.ANY.withCharset(CharEncoding.UTF_8.toLowerCase()))
		.queryParams("format", "application/xml")
		
		//inutile queryParams
		.queryParams("Server", "MICHIGAN")
		.queryParams("colour", "teal")
		.queryParams("dogRace", "Schnauzer")
		
		.when()
		.get(getUsersXMLEndpoint())
		.then()
		.log().all().assertThat()
		.statusCode(HttpStatus.SC_OK)
		//.contentType(containsString("utf-8"))
		;
	}
	
	@Test 
	public void deveEnviarValorViaHeader() {
		given().log().all()
			
			//.accept: response format to accept
			.accept(ContentType.XML)
			.accept(ContentType.JSON)
			.queryParams("format", "json")
			
			//inutile queryParams
			.queryParams("Server", "MICHIGAN")
			.queryParams("colour", "teal")
			.queryParams("dogRace", "Schnauzer")
			
		.when()
			.get(getUsersXMLEndpoint())
		.then()
			.log().all().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.contentType(ContentType.JSON)
			.contentType(containsString("utf-8"))
		;
	}
	
	
	private String getUsersXMLEndpoint() {
				return "https://restapi.wcaquino.me/v2/users";
	}
	private URI getUsersXMLEndpoint(String parameter, String value) {
				return URI.create(getUsersXMLEndpoint().concat(getParamEqualsToValueString(parameter, value)));
	}
}
