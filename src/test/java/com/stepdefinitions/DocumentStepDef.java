package com.stepdefinitions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.pages.OAMan;
import com.utilities.BrowserUtilities;

import static com.utilities.PlaywrightFactory.*;

import com.utilities.PlaywrightFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DocumentStepDef {

    Page page;
    OAMan oaMan;
    Logger logger = LoggerFactory.getLogger(DocumentStepDef.class);

    public DocumentStepDef() {
        this.page = PlaywrightFactory.getPage();
        oaMan = new OAMan(page);
    }

    @And("user logs in OAMan")
    public void userLogsInOAMan() {
        page.navigate("https://enroll.oaman-test.computacenter.io/login");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("LOGIN MIT SERVICEKONTO")).click();
//        assertThat(page).hasURL("https://auth.oaman-dev.computacenter.io/auth/realms/oaman-ext-broker/protocol/openid-connect/auth?client_id=broker&redirect_uri=https://enroll-api.oaman-dev.computacenter.io/oaman&scope=openid%20profile%20email%20offline_access&response_type=code&loginType=servicekonto&authnContextClassRef=EIDAS_HIGH");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Service Konto Mock")).click();
//        assertThat(page).hasURL("https://auth.oaman-dev.computacenter.io/auth/realms/oaman-ext-provider/protocol/openid-connect/auth?scope=openid&state=y7tm961MD2hJYpVTzF7VkxIkPuF_ngU4kicVa3V6bk8.mlv37rzPh6E.broker&response_type=code&client_id=broker&redirect_uri=https%3A%2F%2Fauth.oaman-dev.computacenter.io%2Fauth%2Frealms%2Foaman-ext-broker%2Fbroker%2Fkeycloak-oidc%2Fendpoint&nonce=mqf4L_DVeeX2hJiOsK7x0Q");
        page.getByLabel("Username or email").fill("");
        page.getByLabel("Password").fill("ga?B=Zvi");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        logger.warn("Erika has logged in to OAMan");
    }

    @And("User clicks VORHABEN")
    public void userClicks() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("VORHABEN")).click();
        logger.info("User clicked on VORHABEN");
    }

    @And("user clicks on {string} to select an option")
    public void userClicksOnToSelectAnOption(String label) {
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName(label + " Alle")).locator("div").nth(3).click();
        logger.info("User clicked on {}", label);
    }

    @And("user selects {string} from displayed options")
    public void userSelectsFromDisplayedOptions(String option) {
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(option)).getByText(option).click();
        logger.info("User selected {}", option);
    }

    @And("user clicks on the card of project {string}")
    public void userClicksOnTheCardOfProject(String projectName) {
        page.locator(oaMan.locateProjectWithName(projectName)).getByText("Öffnen").click();
        logAndAssert().log("User clicked on the project '{}' to open");
//        logger.info("User clicked on the project '{}' to open", projectName);
    }

    @And("user navigates {string} tab")
    public void userNavigatesTab(String tabName) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(tabName)).click();
        logAndAssert().log("User navigates {}");
//        logger.info("User navigates {}", tabName);
    }

    @When("user clicks on {string} button on the page")
    public void userClicksOnButtonOnThePage(String buttonName) {
        page.getByText(buttonName).click();
        logAndAssert().log("User clicks on '{}' button on the page");
//        logger.info("User clicks on '{}' button on the page", buttonName);
    }

    @And("user fills {string} as {string}")
    public void userFillsAs(String label, String value) {
        String dateAndTime = BrowserUtilities.getDateAndTime();
        String valueWithTime = value + "_" + dateAndTime;
        getThreadMap().put("valueWithTime", valueWithTime);
        page.getByLabel(label).fill(valueWithTime);
        logAndAssert().log("User entered '{}' to the field '{}'");
//        logger.info("User entered '{}' to the field '{}'", value, label);
    }

    @And("user clicks {string} button on the dialog")
    public void userClicksButtonOnTheDialog(String buttonname) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonname)).click();
        logAndAssert().log("User clicked on '{}' button on the dialog");
//        logger.info("User clicked on '{}' button on the dialog", buttonname);
    }

    @Then("created document should be displayed on the page")
    public void createdDocumentShouldBeDisplayedOnThePage() {
        String valueWithTime = (String) getThreadMap().get("valueWithTime");
        oaMan.verifyDocument(valueWithTime);
    }

    @And("user selects {string} project with name {string}, state {string} and applicant {string}")
    public void userSelectsProjectWithNameStateAndApplicant(String thProject, String projectName, String state,
                                                            String applicant) {
        int projetNum = Integer.parseInt(thProject);
        page.locator(oaMan.searchField).fill(projectName);
        page.locator(oaMan.searchField).press("Enter");
        logger.info("User entered '{}' into search field and pressed enter button", projectName);

        //sort by Status
        page.locator("#state-form").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(state)).click();

        //get project size
        Locator applicants =
                page.getByText("Antragsteller:in").locator("//following-sibling::span").filter(new Locator.FilterOptions().setHasText(applicant));
        List<String> applicantNames = applicants.allInnerTexts();
        int projectSize = applicantNames.size();

        //select nth project
        if (projectSize >= projetNum) {
            List<String> projectNames = applicants.locator("//ancestor::mat-card//mat-card-title").allInnerTexts();
            String selectedProject = projectNames.get(projetNum - 1);
            getThreadMap().put("selectedProject", selectedProject);
            logger.info("Selected project: {}", selectedProject);
        } else if (projectSize > 0) {
            Assert.fail("There is just " + projectSize + " projects on the page, smaller than " + projetNum);
        } else {
            Assert.fail("There is NO projects on the page");
        }
    }

    @When("user clicks on selected project")
    public void userClicksOnSelectedProject() {
        String selectedProject = (String) getThreadMap().get("selectedProject");
        oaMan.projectOpenButtonWithName(selectedProject).click();
        logger.info("User clicked on : {}", selectedProject);
    }

    @Then("{string} should be equal to {string}")
    public void shouldBeEqualTo(String value1, String value2) {
        logAndAssert().assertThat(value1).as("Values are NOT verified!").isEqualTo(value2);
        logAndAssert().log("Values are verified as equal: "+value1 +"="+ value2);
    }

//    @After(order = 2)
    @And("all softAssertions are executed")
    public void allSoftAssertionsAreExecuted() {
        logAndAssert().assertAll();
    }
}
