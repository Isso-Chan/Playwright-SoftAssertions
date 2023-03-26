package com.stepdefinitions;

import static com.utilities.PlaywrightFactory.*;

import com.microsoft.playwright.Page;
import com.pages.HomePage;
import com.pages.LoginPage;
import com.utilities.PlaywrightFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class LoginCucumberTest {

    Page page;
    protected HomePage homePage;
    protected LoginPage loginPage;

    public LoginCucumberTest() {
        this.page = PlaywrightFactory.getPage();
        homePage = new HomePage(page);
        loginPage = new LoginPage(page);
    }

    @Given("User launched SwagLabs application")
    public void user_launched_swag_labs_application() {
//        page.navigate("https://www.saucedemo.com/");

    }

    @When("User verify the Page title")
    public void user_verify_the_page_title() {
        String title = loginPage.verifyTitle();
        logOrAssert().assertThat(title).as("Title is NOT verified!").isEqualTo("Swag Labs");
    }

    @When("User logged in the app using username {string} and password {string}")
    public void user_logged_in_the_app_using_username_and_password(String username, String password) {
        loginPage.loginIntoApplication(username, password);
    }

    @Then("User verify the product name {string}")
    public void user_verify_the_product_name(String expected) {
        String productName = homePage.getProductName();
        logOrAssert().assertThat(expected).as("Verification is NOT done!").isEqualTo(productName);
    }

    @Then("User logout from the application")
    public void user_logout_from_the_application() {
        loginPage.logoutApplication();
    }


}
