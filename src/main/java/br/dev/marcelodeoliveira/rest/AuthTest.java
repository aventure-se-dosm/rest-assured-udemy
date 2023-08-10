package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

//import br.dev.marcelodeoliveira.rest.model.StarWarsCharacter;


public class AuthTest {
	
	private final String starWarsAPIUrl = "https://swapi.dev/api/";

	@Test
	public void deveAcessarSwapi() {
		
		//StarWarsCharacter character1 = new StarWarsCharacter();
		
		given().log().all()
			.when()
			.get(starWarsAPIUrl+"people/1")
		.then().log().all()
			.assertThat()
			.statusCode(HttpStatus.SC_OK)
			.body("name", is("Luke Skywalker"))
			.body("height", is("172"))
			.body("hair_color", Matchers.oneOf("fair", "blond"))
		;
	}

}
