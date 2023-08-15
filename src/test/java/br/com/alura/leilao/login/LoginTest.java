package br.com.alura.leilao.login;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class LoginTest {

	private static final String URL_LOGIN = "http://localhost:8080/login";
	private WebDriver browser;

	@BeforeAll
	public static void beforeAll() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");
	}

	@BeforeEach
	void beforeEach() {
		browser = new ChromeDriver();
		browser.navigate().to(URL_LOGIN);
	}

	@AfterEach
	void afterEach() {
		browser.quit();
	}

	@Test
	void deveriaEfeturarLoginComDadosValidos() {
		browser.findElement(By.id("username")).sendKeys("fulano");
		browser.findElement(By.id("password")).sendKeys("pass");
		browser.findElement(By.id("login-form")).submit();

		assertFalse(browser.getCurrentUrl().equals(URL_LOGIN));
		assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());
	}

	@Test
	void naoDeveriaEfeturarLoginComDadosInvalidos() {
		String loginErrorUrl = URL_LOGIN + "?error";

		browser.navigate().to(URL_LOGIN);
		browser.findElement(By.id("username")).sendKeys("usuario-invalido");
		browser.findElement(By.id("password")).sendKeys("senha-invalida");
		browser.findElement(By.id("login-form")).submit();

		assertTrue(browser.getCurrentUrl().equals(loginErrorUrl));
		assertTrue(browser.getPageSource().contains("Usuário e senha inválidos."));
		assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
	}

	@Test
	public void naoDeveriaAcessarPaginaRestritaSemEstarLogado() {
		browser.navigate().to("http://localhost:8080/leiloes/2");
		
		assertTrue(browser.getCurrentUrl().equals(URL_LOGIN));
		assertFalse(browser.getPageSource().contains("Dados do Leilão"));
	}
}
