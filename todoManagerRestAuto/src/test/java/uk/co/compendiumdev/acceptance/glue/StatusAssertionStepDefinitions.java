package uk.co.compendiumdev.acceptance.glue;

import cucumber.api.java.en.Then;

public class StatusAssertionStepDefinitions {

    @Then("^I should receive a confirmation that my operation was successful$")
    public void iShouldReceiveAConfirmationThatMyOperationWasSuccessful() {
    }

    @Then("^I should receive an error informing me that the requested resource was not found$")
    public void iShouldReceiveAnErrorInformingMeThatTheRequestedResourceWasNotFound() {
    }

    @Then("^I should receive an error informing me that the passed information was invalid$")
    public void iShouldReceiveAnErrorInformingMeThatThePassedInformationWasInvalid() {
    }
}
