package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;

//import br.dev.marcelodeoliveira.rest.model.StarWarsCharacter;

public class AuthTest {

	private final String starWarsApiUrl = "swapi.dev/api";
	private final String openWeatherMapApiUrl = "api.openweathermap.org/data/2.5/weather";
	private final String wcAquinoRestApiUrl = "restapi.wcaquino.me";
	private final String barrigaRestApiUrl = "barrigarest.wcaquino.me";
	private final String seuBarrigaRestApiUrl = "seubarriga.wcaquino.me";
	private String secureHttpProtocol = "https://";
	private String notSecureHttpProtocol = "http://";

	private boolean isSecure = true;

	private boolean isSecure() {
		return isSecure;
	}

	private void setSecure() {
		isSecure = true;
		;
	}

	private void setUnsecure() {
		isSecure = false;
	}

	private String getPartialQuery() {
		return String.format("?q=%s,%s&unit=%s&appid=%s", city, countryIsoAlpha2, unit, appIdKey);
	}

	private String getWcAquinoRestApi() {
		return getProcol() + wcAquinoRestApiUrl;
	}

	private String getBarrigarestApiUrl(String resource) {
		return notSecureHttpProtocol + barrigaRestApiUrl + resource;
	}

	private String getBarrigarestApiUrl() {
		return getBarrigarestApiUrl("");
	}

	private String getSeuBarrigaApiUrl() {
		return getProcol() + seuBarrigaRestApiUrl;
	}

	private String getOpenWeatherMap() {
		return getProcol() + this.openWeatherMapApiUrl;
	}

	private String getProcol() {
		return isSecure() ? secureHttpProtocol : notSecureHttpProtocol;
	}
	/*
	 * //keep it up for your end-of-course rethinks! private String
	 * getPartialQuery(String city, String contry, String appId) { return
	 * String.format("q=%s,%s&appid=%s", city, appId ); }
	 */

	// whilst you're not that refactor guru, let's make it simpler here:
	String city = "São Paulo";
	String countryIsoAlpha2 = "BR";
	String unit = "BR";
	String appIdKey = "c47d4fc91b865630668a1be01db21a05";

	@Test
	public void deveAcessarSwapi() {
		// StarWarsCharacter character1 = new StarWarsCharacter();

		/**
		 * REFACTOR POINT: Use a Json with all of underlying attribute-values, so we
		 * simply rely on serialization of models, avoiding declaration or too much
		 * variable creation and attribuitons.
		 */

		given().log().all().when().get(getStarWarsApiUrl() + "/people/1").then().log().all().assertThat()
				.statusCode(HttpStatus.SC_OK).body("name", is("Luke Skywalker")).body("height", is("172"))
				.body("hair_color", Matchers.oneOf("fair", "blond"));
	}

	private String getStarWarsApiUrl() {
		// TODO Auto-generated method stub
		return getProcol() + starWarsApiUrl;
	}

	@Test
	public void deveAcessarCidadeComQueryParam() {

		given().log().all().queryParam("q", String.join(",", "Weingarten", "DE")).queryParam("units", "metric")
				.queryParam("appid", appIdKey).when().get(getOpenWeatherMap()).then().log().all().assertThat()
				.statusCode(HttpStatus.SC_OK).body("coord.lon", is(9.6333F)).body("coord.lat", is(47.8F))
				.body("main.temp", lessThan(50.0F)) // 'F' stands for 'float' here.
		;
		// Although global warming could make this wrong glimpse reality soon!

		// .body("coord.lat", is(47.8f)) // (as supposed to): it also works pretty
		// .body("coord.lat", is(Float.valueOf(47.8f))) // it also works pretty
		// .body("coord.lat", is(new Float(47.8f))) // it also works pretty

		// .body("coord.lat", is(47.8D)) // Assertion error (wrong type): not a String
		// to be expected, a float value rather
		// .body("coord.lon", is(9.6333)) // Assertion error (wrong type): no 'F'
		// standing for type marking mess the assertion up (double inferred as its type)
		// .body("coord.lat", is("47.8")) // Assertion error (wrong type): not a String
		// to be expected, a float value rather
		// WARNING: Values such as 'temp', 'visibility' and 'humidity' are all
		// volatile: it may change as quick as Open Weather Map API could inform us.
		// Hence, those variables are not mapped on this sprecific test.
	}

	@Test
	public void deveAcessarCidadeSemQueryParam() {
		// With queryParam() we do not need at first glance
		// to create any model representation such as below
		// for building it after a query for method get().

		/*
		 * "coord" { "lon": 9.6333, "lat": 47.8 }
		 */
		given().log().all().when().get(getOpenWeatherMap() + getPartialQuery()).then().log().all().assertThat()
				.statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void naoDeveAcessarSemSenha() {
		given().log().all().when().get(getWcAquinoRestApi() + "/basicauth").then().log().all().assertThat()
				.statusCode(HttpStatus.SC_UNAUTHORIZED);
	}

	@Test
	public void deveFazerAutenticacaoBasica1() {

		String username = "admin";
		String password = "senha";

		given().log().all().when()
				// same as get("https://admin:senha@restapi.wcaquino.me/basicauth")
				.get(getAuthUrl(wcAquinoRestApiUrl, username, password)).then().log().all().assertThat()
				.body("status", is("logado")).statusCode(HttpStatus.SC_OK);
	}

	private String getAuthUrl(CharSequence apiUrl, CharSequence username, CharSequence password) {
		return String.join("", getProcol(), username, ":", password, "@", apiUrl, "/basicauth");
	}

	@Test
	public void deveFazerAutenticacaoBasica2() {
		String username = "admin";
		String password = "senha";

		given().log().all()
			.auth().basic(username, password).
		when()
			.get(getWcAquinoRestApi() + "/basicauth")
		.then().log().all().
			assertThat().statusCode(HttpStatus.SC_OK)
			.body("status", is("logado"));
	}

	public String getSeuBarrigaLoginJWT() {

		Map<String, String> login = new HashMap<>();
		login.put("email", "automation.dvmrkolv@gmail.com");
		login.put("senha", "wXY2AUQXYy3gbeq");

		return given().log().all()
				.body(login)
				.contentType(ContentType.JSON)
			.when()
				.post(getBarrigarestApiUrl("/signin"))
			.then().log().all().extract().path("token").toString();
	}
	

	@Test
	public void deveFazerAutenticacaoComJwt() {
		// String token = getSeuBarrigaLoginJWT();

		given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization",  String.join(" ", "JWT", getSeuBarrigaLoginJWT()))
			.when()
				.get(getBarrigarestApiUrl("/contas"))
			.then().log().all().assertThat()
				.statusCode(HttpStatus.SC_OK).body("nome", hasItems("Conta de Teste Jwt"));
			;
	}

	@Test
	public void deveFazerAutenticacaoBasicaComChallenge() {
		String username = "admin";
		String password = "senha";

		given().log().all().auth().preemptive().basic(username, password).when()
				.get(getWcAquinoRestApi() + "/basicauth2").then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}
}
