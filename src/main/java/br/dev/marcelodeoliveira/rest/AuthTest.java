package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

//import br.dev.marcelodeoliveira.rest.model.StarWarsCharacter;

public class AuthTest {

	private final String starWarsApiUrl = "https://swapi.dev/api";
	private final String openWeatherMapApiUrl = "https://api.openweathermap.org/data/2.5/weather";
	private final String wcAquinoRestApiUrl = "https://restapi.wcaquino.me";
	private final String barrigaRestApiUrl = "http://barrigarest.wcaquino.me";
	/*
	 * private String secureHttpProtocol = "https://"; private String
	 * notSecureHttpProtocol = "http://";
	 */

	private boolean isSecure = true;

	private boolean isSecure() {
		return isSecure;
	}

	private String getPartialQuery() {
		return String.format("?q=%s,%s&unit=%s&appid=%s", city, countryIsoAlpha2, unit, appIdKey);
	}

	private String getWcAquinoRestApi() {
		return wcAquinoRestApiUrl;
	}

	private String getBarrigarestApiUrl(String resource) {
		return barrigaRestApiUrl + resource;
	}

	private String getOpenWeatherMap() {
		return this.openWeatherMapApiUrl;
	}

	/*
	 * private String getProcol() { return isSecure() ? secureHttpProtocol :
	 * notSecureHttpProtocol; }
	 */
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
		 * variable creation and attribution
		 */

		given().log().all()
		.when()
			.get(getStarWarsApiUrl() + "/people/1")
		.then().log().all().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.body("name", is("Luke Skywalker")).body("height", is("172"))
			.body("hair_color", Matchers.oneOf("fair", "blond"));
	}

	private String getStarWarsApiUrl() {
		return starWarsApiUrl;
	}

	@Test
	public void deveAcessarCidadeComQueryParam() {

		given().log().all().queryParam("q", String.join(",", "Weingarten", "DE")).queryParam("units", "metric")
				.queryParam("appid", appIdKey)

				.when().get(getOpenWeatherMap())

				.then().log().all().assertThat().statusCode(HttpStatus.SC_OK)

				.body("coord.lon", is(9.6333F)).body("coord.lat", is(47.8F)).body("main.temp", lessThan(50.0F)) // 'F'
																												// stands
																												// for
																												// 'float'
																												// here.
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
		given().log().all().when().get(getOpenWeatherMap() + getPartialQuery())
				// SAME AS: get("?q=São
				// Paulo,BR&unit=metrics&appid=c47d4fc91b865630668a1be01db21a05")
				.then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void naoDeveAcessarSemSenha() {
		given().log().all().when().get(getWcAquinoRestApi() + "/basicauth").then().log().all().assertThat()
				.statusCode(HttpStatus.SC_UNAUTHORIZED);
	}

	@Test
	public void deveFazerAutenticacaoBasica1() {
		given().log().all().when().get("https://admin:senha@restapi.wcaquino.me/basicauth").then().log().all()
				.assertThat().body("status", is("logado")).statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void deveFazerAutenticacaoBasica2() {
		String username = "admin";
		String password = "senha";

		given().log().all().auth().basic(username, password).when().get(getWcAquinoRestApi() + "/basicauth").then()
				.log().all().assertThat().statusCode(HttpStatus.SC_OK).body("status", is("logado"));
	}

	public String getSeuBarrigaLoginJWT() {

		Map<String, String> login = new HashMap<>();
		login.put("email", "automation.dvmrkolv@gmail.com");
		login.put("senha", "wXY2AUQXYy3gbeq");

		return given().log().all()
				.body(login).contentType(ContentType.JSON)
				.when()
				.post(getBarrigarestApiUrl("/signin")).then().log().all().extract().path("token").toString();
	}

	@Test
	public void deveFazerAutenticacaoComJwt() {
		given().log().all().contentType(ContentType.JSON)
				.header("Authorization", String.join(" ", "JWT", getSeuBarrigaLoginJWT())).when()
				.get(getBarrigarestApiUrl("/contas")).then().log().all().assertThat().statusCode(HttpStatus.SC_OK)
				.statusCode(200)
				//.body("nome", hasItems("Conta de Teste Jwt"));
		;
	}

	@Test
	public void deveFazerAutenticacaoBasicaComChallenge() {
		String username = "admin";
		String password = "senha";

		given().log().all().auth().preemptive().basic(username, password).when()
				.get(getWcAquinoRestApi() + "/basicauth2").then().log().all().assertThat().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void deveAcessarAplicacaoWeb() {
		// seubarriga
		String email = "email"; // <input id='email' ... name='email'>
		String senha = "senha"; // <input id='senha' ... name='senha'>
		String firstAccountXMLPathLocation = "html.body.table.tbody.tr[0].td[0]";

		String cookie = getCookieStringFromResponse(email, senha);

		given().log().all().cookie("connect.sid", getCookieValue(cookie)).when()
				.get("https://seubarriga.wcaquino.me/contas").then().log().all().statusCode(HttpStatus.SC_OK)
				.body(firstAccountXMLPathLocation, is("Conta de Teste Jwt"))
				.body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste Jwt")).extract()
				.path(firstAccountXMLPathLocation);
	}

	@Test
	public void deveAcessarAplicacaoWebExtraindoStringXmlPathECookies() {
		// It doesn't work if we're using the API 'barrigarest' instead of 'seubarriga'

		// seubarriga
		String email = "email"; // <input id='email' ... name='email'>
		String senha = "senha"; // <input id='senha' ... name='senha'>
		String firstAccountXMLPathLocation = "html.body.table.tbody.tr[0].td[0]";

		String cookie = getCookieStringFromResponse(email, senha);

		String body = given().log().all().cookie("connect.sid", getCookieValue(cookie)).when()
				.get("http://seubarriga.wcaquino.me/contas").then().log().all().statusCode(HttpStatus.SC_OK)
				.body(firstAccountXMLPathLocation, is("Conta de Teste Jwt"))
				// .body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste Jwt"))
				// .extract().path(firstAccountXMLPathLocation)
				.extract().body().asString();
		XmlPath xmlcontaPathPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlcontaPathPath.getString(firstAccountXMLPathLocation));
	}

	private String getCookieStringFromResponse(String email, String senha) {
		String cookie = given().log().all().contentType(ContentType.URLENC.withCharset(CharEncoding.UTF_8))
				.formParam(email, "automation.dvmrkolv@gmail.com").formParam(senha, "wXY2AUQXYy3gbeq").when()
				// .post("/signin")
				.post("http://seubarriga.wcaquino.me/logar")// working well
				.then().log().all().extract().header("set-cookie");
		return cookie;
	}

	private String getCookieValue(String cookie) {
		return cookie.split("=")[1].split(";")[0];
	}
}
