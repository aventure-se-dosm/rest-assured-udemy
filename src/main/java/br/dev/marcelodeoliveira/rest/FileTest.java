package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpStatus;
import org.junit.Test;

public class FileTest {

	
	
	
	//usar basepath... seria melhor, não?
	
	private final String UrlFileUpload = "https://restapi.wcaquino.me/upload";
	private final String PathUploadedFile = "src/main/resources/users.pdf";
	private final String PathTooMuchLargeUploadedFile = "src/main/resources/chromedriver_win32_111.zip";
	private final String UrlFileDownload = "https://restapi.wcaquino.me/download";
//	private final String UrlFileUpload = "https://restapi.wcaquino.me/upload";
//	private final String PathUploadedFile = "src/main/resources/users.pdf";
//	private final String PathTooMuchLargeUploadedFile = "src/main/resources/chromedriver_win32_111.zip";

	@Test
	public void deveObrigarEnvioArquivo() {
		given().log().all()
			//.contentType("multipart/form-data") --> optional
		.when()
			.post(UrlFileUpload)
		.then().log().all()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void deveFazerEnvioDeArquivo() {
		//.contentType("multipart/form-data") --> optional
		
		//	'Content-type' has an attribute called name.
		//	"arquivo" is set for this endpoint, standing for files to upload
		// which Content-type is 'multipart/form-data'
		given().log().all()
			.multiPart("arquivo", new File(PathUploadedFile))
		.when()
			.post(UrlFileUpload)
		.then().log().all()
				.statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void naoDeveFazerUploadDeArquivoMuitoGrande() {
		given().log().all()
			.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
		.when()
			.post(UrlFileUpload)
		.then().log().all()
			.statusCode(HttpStatus.SC_REQUEST_TOO_LONG);
	}

	@Test
	public void naoDeveFazerUploadDeArquivoTimeoutException() {
		given().log().all()
		.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
		.when()
		.post(UrlFileUpload)
		.then().log().all()
		.time(lessThan(10000L))
		.statusCode(HttpStatus.SC_REQUEST_TOO_LONG);
	}
	@Test
	public void DeveFazerDownloadDeArquivoDeImagem() throws IOException {
		
		String pathDestination = "src/main/resources/imagem.jpg";
		
		//  No subfolder could be created if not existent
		//	String pathDestination = "src/main/resources/downloads/imagem.jpg";
		
		byte[] imgByteArray = given().log().all()
		.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
		.when()
			.get(UrlFileDownload)
		.then()
		.statusCode(HttpStatus.SC_REQUEST_TOO_LONG)
		.extract().asByteArray();
		
		OutputStream imgStream = new FileOutputStream(new File(pathDestination));
		imgStream.write(imgByteArray);
		imgStream.close();
		
	}
	@Test
	public void DeveFazerDownloadDeArquivoDeImagemSeDiretorioNaoExiste() throws IOException {
		
		String pathDestination = "src/main/resources/imagem.jpg";
		
		//  No subfolder could be created if not existent
		//	String pathDestination = "src/main/resources/downloads/imagem.jpg";
		
		byte[] imgByteArray = given().log().all()
				.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
				.when()
				.get(UrlFileDownload)
				.then()
				.statusCode(HttpStatus.SC_REQUEST_TOO_LONG)
				.extract().asByteArray();
		
		File imgFile = new File(pathDestination);
		imgFile.createNewFile();
		
		//'false' is the value of 'append', i.e., bytes won't be written
		// after the end of the file.
		// Overwriting is suppose to happen if append is set 'true', rather.
		// IO, FileNotFound, and such expected exceptions could still happen so.
		OutputStream imgStream = new FileOutputStream(imgFile, false);
		imgStream.write(imgByteArray);
		imgStream.close();
		
	}
	
	
}