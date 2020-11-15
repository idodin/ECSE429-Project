package uk.co.compendiumdev.acceptance.glue;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.junit.jupiter.api.Assumptions;
import uk.co.compendiumdev.Environment;

public class CommonPreconditionStepDefinitions {

    @Given("^The following tasks exist with their respective statuses, courses and priority levels:$")
    public void theFollowingTasksExistWithTheirRespectiveStatusesCoursesAndPriorityLevels(DataTable table) {

    }

    @Given("^The application is running$")
    public void theApplicationIsRunning() {
        Assumptions.assumeTrue(Environment.getBaseUri() != null);
    }
}
