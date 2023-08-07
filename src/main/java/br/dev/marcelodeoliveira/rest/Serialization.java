package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.DecoderConfig.decoderConfig;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import br.dev.marcelodeoliveira.rest.model.User;
import io.restassured.http.ContentType;
import io.restassured.http.Method;

//anotações para des-/serialização com XML

public class Serialization {

	private final Float MINIMUM_SALARY = 1320.00F;
	private final String URL_BASE = "https://restapi.wcaquino.me";
	private final String URL_RESOURCE_USERS = "/users";
	private final String URL_RESOURCE_USERSXML = "/usersXML";

	@BeforeClass
	public static void setupTest() {
		config().decoderConfig(decoderConfig().defaultContentCharset("UTF-8"));
	}

	private String getUsersResource() {
		return this.URL_RESOURCE_USERS;
	}

	private String getUrlBase() {
		return this.URL_BASE;
	}

	private String getUsersXMLResource() {
		// TODO Auto-generated method stub
		return this.URL_RESOURCE_USERSXML;
	}

	private String getUsersEndpoint() {
		return getUrlBase() + getUsersResource();
	}

	private String getUsersEndpoint(Integer index) {
		return getUsersEndpoint() + "/" + index;
	}

	private String getUsersXMLEndpoint() {
		return getUrlBase() + getUsersXMLResource();
	}

	private void someSwitchByVerb(Method method) {
		// TODO: any method which some arg is supposded to be a REST verb enum.
		// look at this as a template.
	}

	// na verdade precisamos gerar uma DTO
	// na arquitetura maior
	private String getUserJson(String name, Integer age) {

		// it works: oneline simplest Hard-coded json
		// String body = String.format("{\"name\":\"%s\",\"age\":%d}", name, age);
		// String body = String.format("{\"name\":\"%s\",\"age\":%s}", name,
		// age.toString());

		String body = String.format("{\n" + "\t" + "\"name\":\t\"%s\"\n" + "," + "\t" + "\"age\"\t:%d\n" + "}", name,
				age);
		// It also works well:
		return body;
	}

	@Test
	public void deveSalvarUmUsuarioJson() {
		given().log().all().contentType(ContentType.JSON).body(getUserJson("José", 50)).when().post(getUsersEndpoint())
				.then().log().all().assertThat().body("id", is(notNullValue())).body("name", is("José"))
				.body("age", greaterThan(new Integer(0))).body("age", is(50));
	}

	@Test
	public void deveSalvarUmUsuarioJsonUsandoMap() {

		/**
		 * [1] Dependência para serialização: GSON
		 * 
		 * [2] Exceção por ausência: java.lang.IllegalStateException: Cannot serialize
		 * object because no JSON serializer found in classpath. Please put either
		 * Jackson (Databind) or Gson in the classpath.
		 * 
		 */
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("name", "Maria Mapper");
		params.put("age", 29);
		given().log().all().contentType(ContentType.JSON).body(params).when().post(getUsersEndpoint()).then().log()
				.all().assertThat().body("id", is(notNullValue())).body("name", is("Maria Mapper"))
				.body("age", greaterThan(new Integer(0))).body("age", is(29));
	}

	@Test
	public void deveSalvarUmUsuarioJsonUsandoModelObject() {
		/**
		 * [1] Dependência para serialização: GSON
		 * 
		 * [2] Exceção por ausência: java.lang.IllegalStateException: Cannot serialize
		 * object because no JSON serializer found in classpath. Please put either
		 * Jackson (Databind) or Gson in the classpath.
		 * 
		 */
		User user = new User("Maria Mapper", 29, 3504.00F);

		given().log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
			.post(getUsersEndpoint())
		.then().log().all()
			.assertThat()
			.body("id", is(notNullValue()))
			.body("name", is("Maria Mapper"))
			.body("age", greaterThan(new Integer(0))).body("age", is(29))				.body("salary", Matchers.greaterThan(MINIMUM_SALARY.intValue()));
	}

	@Test
	public void deveDesserializarumModelObjectAoSalvarUmUsuario() {

		User user = new User("Usuario Desserializado", 29, 3504.00F);

		given().log().all().contentType(ContentType.JSON).body(user).when().post(getUsersEndpoint()).then().log().all()
				.statusCode(HttpStatus.SC_CREATED).extract().body().as(User.class);

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getName(), "Usuario Desserializado");
		;
	}

	@Test
	public void deveSalvarUmUsuarioXMLUsandoModelObject() {

		/*
		 * [1]Solução para serialização: @XmlRootPath e @XlmAcce
		 *
		 * [2] Exceção por ausência javax.xml.bind.MarshalException - with linked
		 * exception: [com.sun.istack.SAXException2: não é possível fazer marshalling do
		 * tipo "java.util.HashMap" como um elemento porque ele não foi encontrado em
		 * uma anotação @XmlRootElement]
		 * 
		 */

		User user = new User("Usuario Desserializado", new Integer(40), 2500.00f);
		given().log().all().contentType(ContentType.XML).body(user).when().post(getUsersXMLEndpoint()).then().log()
				.all()
				// .assertThat()
				.statusCode(201).body("user.@id", is(notNullValue())).body("user.name", is("Usuario Desserializado"))
				.body("user.age", is("40"));
	}

	@Test
	@Ignore
	public void deveSalvarUmUsuarioXMLUsandoModelObjectComUTF8() {

		/*
		 * If is there any encoding issue then this single test will actually fail, and
		 * further sollution should be sought.
		 */
		// config().decoderConfig(decoderConfig().defaultContentCharset("UTF-8"));

		User user = new User("Usuário Desserializado", new Integer(40), 2500.00f);
		given().log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post(getUsersXMLEndpoint())
		.then().log().all()
			.assertThat()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuário Desserializado"))
			.body("user.age", is("40"));
	}

}
