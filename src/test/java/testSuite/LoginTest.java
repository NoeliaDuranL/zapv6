
package testSuite;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.Epic;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;

    @BeforeEach
    public void openBrowserProxy() {

        // Configuramos el proxy de OWASP ZAP
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:9095");
        proxy.setSslProxy("localhost:9095");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setProxy(proxy);

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Abrimos la pagina de inicio de sesion de Todoist
        driver.get("https://www.todoist.com/auth/login?locale=es");
    }

    @Test
    @DisplayName("TC-SEC-01 - Rechazar inicio de sesion con credenciales incorrectas")
    @Description("Verifica que Todoist no permita iniciar sesion con credenciales incorrectas.")
    @Epic("Pruebas de seguridad")
    @Story("Validacion del inicio de sesion")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("UCB")
    public void verifyLoginWithInvalidCredentials() throws InterruptedException {

        // Esperamos para poder observar la pagina
        Thread.sleep(2000);

        // Introducimos el correo ficticio
        driver.findElement(By.id("element-0"))
                .sendKeys("qa.prueba.invalid@example.com");

        Thread.sleep(2000);

        // Introducimos una contraseña ficticia
        driver.findElement(By.id("element-2"))
                .sendKeys("ClaveIncorrecta987!");

        Thread.sleep(2000);

        // Enviamos el formulario
        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        // Esperamos para observar la respuesta de Todoist
        Thread.sleep(8000);

        // Comprobamos que seguimos en el formulario de inicio de sesion
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/auth/login"),
                "Se esperaba permanecer en el formulario al usar credenciales incorrectas."
        );
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
