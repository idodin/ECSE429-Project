package uk.co.compendiumdev.acceptance.glue;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PrioritiesStepDefinitions {

    @Given("^Categories exist for the following priority levels:$")
    public void categoriesExistForTheFollowingPriorityLevels(DataTable table) {
    }

    @Given("^No tasks exist for each priority level$")
    public void noTasksExistForEachPriorityLevel() {
    }

    @When("^I categorize task \"([^\"]*)\" as \"([^\"]*)\" priority$")
    public void iCategorizeTaskAsPriority(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Category \"([^\"]*)\" should contain task \"([^\"]*)\"$")
    public void categoryShouldContainTask(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Category \"([^\"]*)\" should still contain its original tasks$")
    public void categoryShouldStillContainItsOriginalTasks(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^Category \"([^\"]*)\" should not contain task \"([^\"]*)\"$")
    public void categoryShouldNotContainTask(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
