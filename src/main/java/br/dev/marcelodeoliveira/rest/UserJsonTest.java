package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class UserJsonTest {

	private static final Float MINIMUM_MONTHLY_REMONERATION = 1320.00F;
	private final String REQUEST_USER_1 = "https://restapi.wcaquino.me/users/1";

	@Test
	public void deveVerificarJsonPrimeiroNivel() {
		given().when().get(REQUEST_USER_1).then().assertThat().statusCode(HttpStatus.SC_OK).body("id", is(1))
				.body("name", containsString("Jo�o")).body("age", greaterThan(18))
				.body("salary", lessThanOrEqualTo(getMinimumMonthlyRemoneration()));
	}

	@Test
	public void deveVerificarJsonPrimeiroNivelOutraForma() {

		Response response = RestAssured.request(Method.GET, "https://wcaquinos.me/users/1");

		/**
		 * path():
		 * 
		 * redireciona obt�m o par escolhendo automaticamente o tipo do arquivo da
		 * response (JSon, XML, &c)
		 */
		Assert.assertEquals(new Integer(1), response.path("id"));

		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("id"));
	}

	public static Float getMinimumMonthlyRemoneration() {
		return MINIMUM_MONTHLY_REMONERATION;
	}
}