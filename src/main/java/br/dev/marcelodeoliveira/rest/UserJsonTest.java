package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

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
		return getUsersUrl() + index;
	}

	private String getUsersUrl() {
		return isSecure() ? (REQUEST_USER_SECURE) : (REQUEST_USER_INSECURE);

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
		given().when().get(getUserUrlById(3)).then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("name", containsString("Ana")).body("filhos[0].name", is("Zezinho"))
				.body("filhos[1].name", is("Luizinho")).body("filhos.name", hasItem("Luizinho"))
				.body("filhos.name", not(hasItem("Qualquer"))).body("filhos.name", hasSize(2))
				.body("filhos.name", hasItems("Zezinho", "Luizinho"));
	}

	@Test
	public void deveRetornarUsuarioInexistente() {
		given().when().get(getUserUrlById(4)).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND).body("error",
				is("Usuário inexistente"));
	}

	@Test
	public void deveVerificarListaRaiz() {
		given().when().get(getUsersUrl()).then().assertThat().statusCode(HttpStatus.SC_OK).body("$", hasSize(3))
				.body("name", hasItem("João da Silva")).body("name[0]", is("João da Silva"))
				.body("name[1]", is("Maria Joaquina")).body("name[2]", is("Ana Júlia"))
				.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
				.body("salary", contains(1234.5677f, Integer.valueOf(2500), null))
				.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")));
		;
		// Convenção: '$' é escolhido para representar uma estrutura de lista;
	}

	@Test
	public void devoFazerVerificacoesAvancadas() {
		given().when().get(getUsersUrl()).then().assertThat().body("$", hasSize(3))
				.body("age.findAll{it <= 25}.size()", is(2)).body("age.findAll{it <= 25 && it > 20}.size()", is(1))
				.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
				.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
				
				.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
				.body("findAll{it.age <= 25}[1].name", is("Ana Júlia"))
				.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))
				.body("findAll{it.age <= 25}[-2].name", is("Maria Joaquina"))
				
				.body("find{it.age <= 25}.name", is("Maria Joaquina"))
				
				.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
				.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
				
				.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
				
				.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
				.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
				.body("age.collect{it*2}", hasItems(60, 50, 40))
				
				.body("id.max()", is(3))
				.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
				.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
				.body("salary.min()", is(1234.5678f));
		
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