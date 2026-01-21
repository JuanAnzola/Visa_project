package pageobjects;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.PropertyUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static stepdefinition.Hooks.getDriver;
import static utilities.WebDriverUtils.scrollToElement;
import static utilities.WebDriverUtils.takeScreenshot;

public class HomePage {

    @FindBy(how = How.XPATH, using = "//a[text()='Continuar']")
    private WebElement btn_Continuar;

    @FindBy(how = How.XPATH, using = "//h3[text()='Tomar medidas para este grupo de solicitantes:']")
    private WebElement lbl_Title;

    @FindBy(how = How.XPATH, using = "//h5[contains(normalize-space(.), 'Reprogramar cita')]")
    private WebElement lbl_ReprogramarCita;

    @FindBy(how = How.XPATH, using = "//a[normalize-space(text())='Reprogramar cita']")
    private WebElement btn_Reprogramar;

    @FindBy(how = How.XPATH, using = "//div[text()='Reprogramar cita']")
    private WebElement lbl_Reprogramar_Section;

    @FindBy(how = How.XPATH, using = "//input[@value=\"Continuar\"]")
    private WebElement btn_Continuar_Section;

    @FindBy(how = How.XPATH, using = "//legend[text()='Cita en la Sección Consular']")
    private WebElement lbl_cita;

    @FindBy(how = How.XPATH, using = "//select[@name=\"appointments[consulate_appointment][facility_id]\"]")
    private WebElement sel_ciudad;

    @FindBy(how = How.XPATH, using = "//option[text()='Bogota']")
    private WebElement opt_Bogota;

    @FindBy(how = How.XPATH, using = "//li[@class=\"yatri_date input required stringish\" and @id=\"appointments_consulate_appointment_date_input\"]")
    private WebElement opt_Date;

    @FindBy(how = How.XPATH, using = "//div[@class=\"ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-datepicker-multi ui-datepicker-multi-2\"]")
    private WebElement div_Date;

    @FindBy(how = How.XPATH, using = "//span[@class=\"ui-icon ui-icon-circle-triangle-e\"]")
    private WebElement btn_siguiente;

    @FindBy(how = How.XPATH, using = "//div[@id=\"consulate_date_time_not_available\"]//small[text()='El sistema está ocupado. Por favor, inténtelo de nuevo más tarde.']")
    private WebElement lbl_consulateBusy;

    @FindBy(how = How.XPATH, using = "//div[@id=\"asc_date_time_not_available\"]//small[text()='El sistema está ocupado. Por favor, inténtelo de nuevo más tarde.']")
    private WebElement lbl_casBusy;

    @FindBy(how = How.XPATH, using = "//select[@name=\"appointments[consulate_appointment][time]\"]")
    private WebElement sel_Hora;

    @FindBy(how = How.XPATH, using = "//li[@class=\"yatri_date input required stringish\" and @id=\"appointments_asc_appointment_date_input\"]")
    private WebElement opt_DateCas;

    @FindBy(how = How.XPATH, using = "//select[@name=\"appointments[asc_appointment][facility_id]\" and @id=\"appointments_asc_appointment_facility_id\"]")
    private WebElement sel_LocationCas;

    @FindBy(how = How.XPATH, using = "//select[@name=\"appointments[asc_appointment][time]\"]")
    private WebElement sel_HoraCas;

    @FindBy(how = How.XPATH, using = "//input[@value=\"Reprogramar\"]")
    private WebElement btn_ReprogramarAppoiment;

    @FindBy(how = How.XPATH, using = "//div[@data-header=\"Dirección Consular\"]")
    private WebElement div_Adress;

    public HomePage() {
        PageFactory.initElements(getDriver(), this);
    }

    WebElement selectElement;
    Select select;
    int diaDisponble;
    LocalDate cita = LocalDate.parse(PropertyUtils.getProperty("app.date"));
    LocalDate fechaDisponible;
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
    ArrayList<LocalDate> fechas = new ArrayList<LocalDate>();
    List<WebElement> opciones;

    public void isInTheHomePage() {
        try {
            isPresent(btn_Continuar);
            btn_Continuar.click();
        } catch (Exception e) {
            takeScreenshot("Error_HomePage");
            System.out.println("-Error: " + e);
            throw e;
        }
    }

    public void reschedule_appointment(){
        try {
            isPresent(lbl_ReprogramarCita);
            lbl_ReprogramarCita.click();
            isPresent(btn_Reprogramar);
            btn_Reprogramar.click();
            isPresent(lbl_Reprogramar_Section);
            isPresent(btn_Continuar_Section);
            btn_Continuar_Section.click();
            isPresent(lbl_cita);
            scrollToElement(lbl_cita);
            sel_ciudad.click();
            isPresent(opt_Bogota);
            opt_Bogota.click();
            Thread.sleep(1000);
            opt_Date.click();
            getAvailableDays();
            Assert.assertTrue("No hay fechas anteriores disponibles",isBefore());
            selectDate("Consulado");
            sel_LocationCas.click();
            isPresent(opt_Bogota);
            opt_Bogota.click();
            Thread.sleep(5000);
            isDateEnabledCAS();
            opt_DateCas.click();
            getAvailableDays();
            Assert.assertTrue("No hay fechas anteriores disponibles en el CAS",isBefore());
            selectDate("CAS");
            takeScreenshot("Confirmacion de las fechas y horas");
        } catch (AssertionError | TimeoutException | NoSuchElementException | StaleElementReferenceException | InterruptedException e) {
            takeScreenshot("Error_AgendandoCita");
            System.out.println("-Error: " + e);
        }
    }

    public void confirm_appointment(){
        try {
            //isPresent(btn_ReprogramarAppoiment);
            //btn_ReprogramarAppoiment.click();
            Thread.sleep(1000);
        } catch (AssertionError| Exception e) {
            takeScreenshot("Error_Confirmando_Citas");
            System.out.println("-Error: " + e);
        }
    }

    public boolean isPresent(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Assert.assertTrue(element.isEnabled());
            return element.isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public WebElement waitToElementoClickeable(String locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
        } catch (Exception e) {
            System.out.println("Error en el metodo de espera con el elemto: " + locator);
            throw new RuntimeException(e);
        }
    }

    public String optionByText(String text) {
        try {
            String xpath = String.format("//option[text()='%s']", text);
            return xpath;
        } catch (Exception e) {
            System.out.println("Error en el metodo de obtener xpath por texto con el valor: ");
            throw new RuntimeException(e);
        }
    }

    public String availableDayByNumber(int dia) {
        String xpath = String.format("//a[@class=\"ui-state-default\" and text()='%d']", dia);
        return xpath;
    }

    public void getAvailableDays() {
        try {
            List<WebElement> diasHabilitados;
            int i = 0;
            int days = 0;
            fechas.clear();
            while (i==0 && days<24){
                diasHabilitados = getDriver().findElements(By.xpath("//td[@data-handler='selectDay']"));
                i = diasHabilitados.size();
                if (diasHabilitados.isEmpty()) {
                    Thread.sleep(1000);
                    btn_siguiente.click();
                    days++;
                }else  {
                    System.out.println(i);
                }
            }
            diasHabilitados = getDriver().findElements(By.xpath("//td[@data-handler='selectDay']"));
            for (WebElement dia : diasHabilitados) {
                int numeroDia = Integer.parseInt(dia.findElement(By.tagName("a")).getText()); // Ej: "12"
                int mes = Integer.parseInt(dia.getAttribute("data-month")); // Ej: "2" → marzo (recuerda: enero = 0)
                int año = Integer.parseInt(dia.getAttribute("data-year"));  // Ej: "2027"
                LocalDate fecha = LocalDate.of(año,(mes) + 1,numeroDia);
                fechas.add(fecha);
            }
            System.out.println(fechas);
        }catch (Exception e) {
            System.out.println("-Error: " + e);
        }
    }

    public Boolean isBefore(){
        try {
            for (LocalDate fecha : fechas) {
                if (fecha.isBefore(cita)){
                    System.out.println("Hay una fecha anterior disponible: " + fecha.getDayOfMonth());
                    diaDisponble = fecha.getDayOfMonth();
                    fechaDisponible = fecha;
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            System.out.println("- Error: "+ e);
        }
        return false;
    }

    public void selectDate(String entidad) {
        try {
            WebElement horaSelect;
            WebElement ciudadSelect;
            String idCitas;

            if (Objects.equals(entidad, "Consulado")) {
                horaSelect = sel_Hora;
                //ciudadSelect = sel_ciudad;
                idCitas = "appointments_consulate_appointment_time";
            } else {
                horaSelect = sel_HoraCas;
                //ciudadSelect = sel_LocationCas;
                idCitas = "appointments_asc_appointment_time";
            }

            // Seleccionar día
            WebElement dia = waitToElementoClickeable(availableDayByNumber(diaDisponble));
            dia.click();

            boolean horasCargadas = false;
            int intentos = 0;

            while (!horasCargadas && intentos < 3) {
                intentos++;
                Thread.sleep(1000);
                // Abrir select de horas
                wait.until(ExpectedConditions.elementToBeClickable(horaSelect)).click();

                // Esperar que tenga opciones reales
                horasCargadas = waitForSelectWithOptions(idCitas);

                if (!horasCargadas) {
                    System.out.println("⚠️ Horas no cargaron, refrescando ciudad (intento " + intentos + ")");
                }
                div_Adress.click();
            }

            if (!horasCargadas) {
                throw new RuntimeException("No fue posible cargar horas después de reintentos");
            }

            // Seleccionar primera hora válida
            selectElement = getDriver().findElement(By.id(idCitas));
            select = new Select(selectElement);

            for (WebElement opcion : select.getOptions()) {
                if (!opcion.getText().isBlank()) {
                    select.selectByVisibleText(opcion.getText());
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ No fue posible seleccionar fecha y hora para " + entidad);
        }
    }

    public boolean waitForSelectWithOptions(String selectId) {
        try {
            return wait.until(driver -> {
                Select select = new Select(driver.findElement(By.id(selectId)));
                return select.getOptions().size() > 1;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void refreshCity(WebElement selectCiudad, String ciudad) throws InterruptedException {
        Select select = new Select(selectCiudad);
        select.selectByIndex(0);// opción en blanco
        Thread.sleep(1000);
        select.selectByIndex(1);
        wait.until(ExpectedConditions.elementToBeClickable(selectCiudad));
        select.selectByVisibleText(ciudad);
    }

    public void isDateEnabledCAS() throws InterruptedException {
        try {
           Boolean flag = opt_DateCas.isDisplayed();
           int i;
           for (i=0; i<=10; i++){
               if(flag){
                   i = 10;
               }else {
                   refreshCity(sel_LocationCas, "Bogota ASC");
                   Thread.sleep(1000);
                   flag = opt_DateCas.isDisplayed();
                   System.out.println("No disponible: " + i);
               }
           }
            Assert.assertTrue("Fallo", flag);
        } catch (AssertionError | Exception e) {
            System.out.println("Error: " + e);
            throw e;
        }
    }



}



