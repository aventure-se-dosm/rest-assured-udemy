package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {
	
	private final  String URL_BASE = "https://restapi.wcaquino.me";
	private final  String URL_RESOURCE_USERS = "/users";
	private final String URL_RESOURCE_USERSXML = "/usersXML";
	
	private String getUsersResource() {
		return this.URL_RESOURCE_USERS;
	}
	
	private String getUrlBase() {
		return this.URL_BASE;
	}
	
	private String getUsersXMLResource() {
				return this.URL_RESOURCE_USERSXML;
	}
	
	private String getUsersEndpoint() {
		return getUrlBase() + getUsersResource();
	}
	private String getUsersEndpoint(Integer index) {
		return getUsersEndpoint() +"/"+ index;
	}
	
	private String getUsersXMLEndpoint() {
		return getUrlBase() + getUsersXMLResource();
	}

	private String getUserJson(String name, Integer age) {

		//it works: oneline simplest Hard-coded json
		// String body =  String.format("{\"name\":\"%s\",\"age\":%d}", name, age);
		//String body =  String.format("{\"name\":\"%s\",\"age\":%s}", name, age.toString()); 
		
		 String	body =  String.format("{\n"
				+ "\t" + "\"name\":\t\"%s\"\n"+","
				+ "\t" + "\"age\"\t:%d\n"
				+ "}",
				name,age);
		//It also works well:
		return body;
	}
	
	@BeforeClass
	public static void setUTF8Encoding()
	{
		given().log().all()
		.contentType("application/XML; charset=utf-8");
	}
	
	@Test
	public void deveSalvarUmUsuarioJson() {
		given().log().all()
			.contentType(ContentType.JSON.withCharset("UTF-8"))
			.body(getUserJson("José", 50))
		.when()
			.post(getUsersEndpoint())
		.then().log().all().assertThat()
			.statusCode(HttpStatus.SC_CREATED)
			.body("id", is(notNullValue()))
			.body("name", is("José"))
			.body("age", greaterThan(new Integer(0)))
			.body("age", is(50))
		;
	}
	@Test 
	public void deveSalvarUmUsuarioXML() {
		given().log().all()
		.contentType(ContentType.XML.withCharset(CharEncoding.UTF_8))
		.body(getUserXML("José", 50))
		.when()
		.post(getUsersXMLEndpoint())
		.then().log().all().assertThat()
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("José")) //we have faced issues with encoding
		.body("user.age", is("50"))
		;
	}
	@Test
	public void deveAlterarUmUsuarioJson() {
		given().log().all()
		.contentType(ContentType.JSON)
		.body(getUserJson("José Nomya Uterado", 50))
		.when()
		.put(getUsersResource(1))
		.then().log().all().assertThat()
		.body("id", is(notNullValue()))
		.body("name", is("José Nomya Uterado")) //no encoding issues w/ json and utf-8
		.body("age", greaterThan(new Integer(0)))
		.body("age", is(50))
		;
	}
	
	private String getUsersResource(Integer index) {
				return String.format("%s%s/%s", getUrlBase(), getUsersResource(), index);
	}

	private String getUserXML(String name, int age) {
				return "<user>"
				+ String.format("<name>%s</name>", name)
				+ String.format("<age>%d</age>", age)
			+"</user>";
	}

	@Test
	public void naoDeveSalvarUmUsuarioNomeStrVazia() {
		given().log().all()
			.contentType(ContentType.JSON)
			.body(getUserJson("", 50)) // it works: null is interpreted as if the String "null";
		.when()
			.post(getUsersEndpoint())
		.then().log().all()
			.assertThat()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("id", Matchers.is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))			
		;
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioSemNome() {
		given().log().all()
			.contentType(ContentType.XML.withCharset(CharEncoding.UTF_8))
			.body("{\"age\": 50}") //it fails, no setup for attribute "name".
		.when()
			.post(
					getUsersEndpoint()
		)
		.then().log().all()
			.assertThat()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))		
		;
	}

	
	@Test
	public void devoCustomizarUrl() {
		given().log().all()
			.contentType(ContentType.JSON)
			.body(getUserJson("Usuário Alterado", 30)) //it fails, no setup for attribute "name".
		.when()
			.put(
					getUrlBase() + "/{entidade}/{id}", "users", 1
					//getUsersEndpoint() + "/{id}",  1 // Same-effect code snippet
		)
		.then().log().all()
			.assertThat()
			.body("id", is(notNullValue()))
			.body("name", is("Usuário Alterado")) //no encoding issues w/ json and utf-8
			.body("age", greaterThan(new Integer(0)))
			.body("age", is(30))
			;
	}
	
	@Test
	public void devoRemoverUsuario() {
		given().log().all()
		.when()
			.delete(getUsersEndpoint(1))
		.then().log().all()
			.assertThat()
			.statusCode(HttpStatus.SC_NO_CONTENT)
			;
	}
	@Test
	public void naoDevoRemoverUsuarioUnexistente() {
		given().log().all()
		.when()
			.delete(getUsersEndpoint(9))
		.then().log().all()
		.assertThat()
			.statusCode(HttpStatus.SC_BAD_REQUEST) 
			//this status hides the fact we don't have a resource /user/1.
			.body("error", is("Registro inexistente"))
		;
	}

}
