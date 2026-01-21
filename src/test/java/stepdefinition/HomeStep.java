package stepdefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pageobjects.HomePage;


public class HomeStep {

    private final HomePage homePage;

    public HomeStep() {
        this.homePage = new HomePage();}


    @Given("inicia la reprogramacion de la cita")
    public void selectStudyPlan() {
        homePage.isInTheHomePage();
        homePage.reschedule_appointment();
    }

    @Then("confirma la reprogramacion")
    public void verifyClassConfirmationMessage() {

    }
}

