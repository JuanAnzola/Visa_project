package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static stepdefinition.Hooks.getDriver;

public class WebDriverUtils {

    private static int x = 0;

    public static WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

    public static void scrollToElement(WebElement element) throws InterruptedException {
        try {
            if(element.isDisplayed()) {
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].scrollIntoView(true);", element);
            }
        }catch(Exception e){
            System.out.println("Error scrolling element in: " + element + ".");
        }
    }

    public static void takeScreenshot(String testName) {
        try {
            // Formato de fecha para evitar caracteres inválidos
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String screenshotName = testName + "_" + timestamp + ".png";

            // Ruta donde se guardarán los screenshots
            Path destination = Path.of(
                    System.getProperty("user.dir"),
                    "screenshots",
                    screenshotName
            );

            // Crear carpeta si no existe
            Files.createDirectories(destination.getParent());

            File source = ((TakesScreenshot) getDriver())
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(source.toPath(), destination);

            System.out.println("📸 Screenshot guardado en: " + destination);

        } catch (IOException e) {
            System.out.println("❌ Error al tomar screenshot: " + e.getMessage());
        }
    }

}



