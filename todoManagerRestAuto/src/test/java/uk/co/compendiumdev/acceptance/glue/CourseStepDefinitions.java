package uk.co.compendiumdev.acceptance.glue;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CourseStepDefinitions {
    @Given("^Projects exist for the following courses:$")
    public void projectsExistForTheFollowingCourses(DataTable table) {
    }

    @Given("^The following tasks exist for their respective courses:$")
    public void theFollowingTasksExistForTheirRespectiveCourses(DataTable table) {
    }

    @Given("^No tasks exist for each course$")
    public void noTasksExistForEachCourse() {
    }

    @When("^I add the task \"([^\"]*)\" to the \"([^\"]*)\" course to do list$")
    public void iAddTheTaskToTheCourseToDoList(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I remove the \"([^\"]*)\" course to do list$")
    public void iRemoveTheCourseToDoList(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a course to do list for course \"([^\"]*)\" with active status \"([^\"]*)\" and completion status \"([^\"]*)\"$")
    public void iCreateACourseToDoListForCourseWithActiveStatusAndCompletionStatus(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a course to do list for course \"([^\"]*)\" with active status \"([^\"]*)\", completion status \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateACourseToDoListForCourseWithActiveStatusCompletionStatusAndDescription(String arg0, String arg1, String arg2, String arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^\"([^\"]*)\" course to do list should contain (\\d+) tasks$")
    public void courseToDoListShouldContainTasks(String arg0, int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Course to do list \"([^\"]*)\" should not exist in the system$")
    public void courseToDoListShouldNotExistInTheSystem(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^\"([^\"]*)\" course to do list should contain task \"([^\"]*)\"$")
    public void courseToDoListShouldContainTask(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^\"([^\"]*)\" course to do list should not contain task \"([^\"]*)\"$")
    public void courseToDoListShouldNotContainTask(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^A course to do list for course \"([^\"]*)\" should exist$")
    public void aCourseToDoListForCourseShouldExist(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The course to do list for course \"([^\"]*)\" should have active status \"([^\"]*)\"$")
    public void theCourseToDoListForCourseShouldHaveActiveStatus(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The course to do list for course \"([^\"]*)\" should have completion status \"([^\"]*)\"$")
    public void theCourseToDoListForCourseShouldHaveCompletionStatus(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The list of course to do lists should include a to do list for course \"([^\"]*)\"$")
    public void theListOfCourseToDoListsShouldIncludeAToDoListForCourse(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^A course to do list for course \"([^\"]*)\" should not exist$")
    public void aCourseToDoListForCourseShouldNotExist(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
