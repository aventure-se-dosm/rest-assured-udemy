package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {

	private static final Float MINIMUM_MONTHLY_REMUNERATION = 1320.00F;
	private String REQUEST_USER_INSECURE = "http://restapi.wcaquino.me/users/";
	private String REQUEST_USER_SECURE = "https://restapi.wcaquino.me/users/";
	private boolean secure = false;

	private String getUserUrlById(Integer index) {
		return isSecure() ? (REQUEST_USER_SECURE + index) : (REQUEST_USER_INSECURE + index);
	}

	private boolean isSecure() {
		return this.secure;
	}

	@Test
	public void deveVerificarJsonPrimeiroNivel() {
		given().when().get(getUserUrlById(1)).then().assertThat().statusCode(HttpStatus.SC_OK).body("id", is(not(2)))
				.body("name", containsString("João")).body("age", greaterThan(18))
				.body("salary", lessThanOrEqualTo(getMinimumMonthlyRemoneration()));
	}

	@Test
	public void deveVerificarJsonSegundoNivel() {
		given().when().get(getUserUrlById(2)).then().assertThat().statusCode(HttpStatus.SC_OK).body("id", is(2))
				.body("name", containsString("Maria")).body("endereco.rua", is("Rua dos bobos"))
				.body("salary", greaterThanOrEqualTo(getMinimumMonthlyRemoneration().intValue()));
	}
	@Test
	public void deveVerificarJsonTerceiroNivel() {
		given().
		when()
			.get(getUserUrlById(3))
		.then().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.body("name", containsString("Ana"))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Luizinho"))
			.body("filhos.name", not(hasItem("Qualquer")))
			.body("filhos.name", hasSize(2))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"));
	}

	@Test
	public void deveVerificarJsonPrimeiroNivelOutraForma() {

		Response response = RestAssured.request(Method.GET, getUserUrlById(1));

		/**
		 * path():
		 * 
		 * redireciona obtém o par escolhendo automaticamente o tipo do arquivo da
		 * 
		 * response (JSON, XML, &c)
		 */
		Assert.assertEquals(new Integer(1), response.path("id"));

		// especificando o tipo da chave (string), passando via parâmetro
		Assert.assertEquals(new Integer(1), response.path("%s", "id"));

		/**
		 * jsonPath()
		 * 
		 * dedicado a somente obter pares somente por responses JSON
		 */

		JsonPath jsPath = new JsonPath(response.body().asString());
		Assert.assertEquals(1, jsPath.getInt("id"));

		/**
		 * jsonPath() com from()
		 * 
		 * desobriga a instanciação de um objeto JsonPath()
		 */
		JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, jsPath.getInt("id"));

//		Assert.assertEquals(new Integer(1), response.path("id"));
//		Assert.assertEquals(new Integer(1), response.path("id"));
	}

	public static Float getMinimumMonthlyRemoneration() {
		return MINIMUM_MONTHLY_REMUNERATION;
	}
}