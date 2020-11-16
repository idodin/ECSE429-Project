package uk.co.compendiumdev.acceptance.features;

import cucumber.api.java.en.Then;
import org.apache.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StatusAssertionStepDefinitions {

    @Then("^I should receive a confirmation that my operation was successful$")
    public void iShouldReceiveAConfirmationThatMyOperationWasSuccessful() {
        assertTrue(CommonStepDefinitions.lastResponse.getFirst().statusCode() >= 200,
                "Expected success status code!");
    }

    @Then("^I should receive an error informing me that the requested resource was not found$")
    public void iShouldReceiveAnErrorInformingMeThatTheRequestedResourceWasNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, CommonStepDefinitions.lastResponse.getFirst().statusCode());
    }

    @Then("^I should receive an error informing me that the passed information was invalid$")
    public void iShouldReceiveAnErrorInformingMeThatThePassedInformationWasInvalid() {
        assertEquals(HttpStatus.SC_BAD_REQUEST, CommonStepDefinitions.lastResponse.getFirst().statusCode());
    }
}
