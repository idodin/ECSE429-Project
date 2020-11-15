package uk.co.compendiumdev.acceptance.glue;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TaskStepDefinitions {

    @Given("^The following tasks exist with their respective statuses and courses:$")
    public void theFollowingTasksExistWithTheirRespectiveStatusesAndCourses(DataTable table) {
    }

    @Given("^The following tasks exist with their respective courses and descriptions:$")
    public void theFollowingTasksExistWithTheirRespectiveCoursesAndDescriptions(DataTable table) {
    }

    @Given("^Task \"([^\"]*)\" exists for course \"([^\"]*)\" with completion status \"([^\"]*)\" and priority \"([^\"]*)\"$")
    public void taskExistsForCourseWithCompletionStatusAndPriority(String arg0, String arg1, String arg2, String arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a task \"([^\"]*)\"$")
    public void iCreateATask(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I mark task \"([^\"]*)\"'s completion status as \"([^\"]*)\"$")
    public void iMarkTaskSCompletionStatusAs(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list$")
    public void iRemoveTheTaskFromTheCourseToDoList(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list again$")
    public void iRemoveTheTaskFromTheCourseToDoListAgain(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I query all incomplete tasks for the \"([^\"]*)\" course to do list$")
    public void iQueryAllIncompleteTasksForTheCourseToDoList(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I query all incomplete HIGH priority tasks for all courses$")
    public void iQueryAllIncompleteHIGHPriorityTasksForAllCourses() {
    }

    @When("^I change the description of task \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iChangeTheDescriptionOfTaskTo(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should have description \"([^\"]*)\"$")
    public void taskShouldHaveDescription(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should not be under the \"([^\"]*)\" course to do list$")
    public void taskShouldNotBeUnderTheCourseToDoList(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should be under the \"([^\"]*)\" course to do list$")
    public void taskShouldBeUnderTheCourseToDoList(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should not be categorized as any priority$")
    public void taskShouldNotBeCategorizedAsAnyPriority(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should be categorized as \"([^\"]*)\" priority$")
    public void taskShouldBeCategorizedAsPriority(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The completion status of task \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void theCompletionStatusOfTaskShouldBe(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The completion status of task \"([^\"]*)\" should be false$")
    public void theCompletionStatusOfTaskShouldBeFalse(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Tasks that were under the \"([^\"]*)\" course to do list should not exist in the system$")
    public void tasksThatWereUnderTheCourseToDoListShouldNotExistInTheSystem(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the task \"([^\"]*)\" should be returned$")
    public void theTaskShouldBeReturned(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^No tasks should be returned$")
    public void noTasksShouldBeReturned() {
    }

    @Then("^I should receive a confirmation that the operation was successful$")
    public void iShouldReceiveAConfirmationThatTheOperationWasSuccessful() {
    }

    @Then("^I should receive a list of all incomplete tasks from all courses as a response$")
    public void iShouldReceiveAListOfAllIncompleteTasksFromAllCoursesAsAResponse() {
    }
}
