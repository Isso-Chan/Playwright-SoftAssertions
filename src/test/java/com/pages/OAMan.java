package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.utilities.BrowserUtilities;
import static com.utilities.PlaywrightFactory.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OAMan {

    Logger logger=LoggerFactory.getLogger(OAMan.class);

    Page page;

    public String searchField="#project-search";
    public String getTab(String tabName){
        return "//oaman-funding-project-details//div[@class='mat-tab-links']/a[text()=' "+tabName+" ']";
    }

    public String locateProjectWithName(String projectName) {
        return "mat-card-content:has-text('"+projectName+"Öffnen')";
    }

    public Locator projectOpenButtonWithName(String projectName) {
        return page.locator("mat-card-content:has-text('"+projectName+"Öffnen')").getByText("Öffnen");
    }

    public String documentNames="//mat-row/mat-cell/span";

    public OAMan(Page page) {
        this.page = page;
    }

    public void verifyDocument(String expectedName){
        BrowserUtilities.waitFor(1000);
        List<String> documents = page.locator(documentNames).allTextContents();
        logAndAssert().assertThat(documents.contains(expectedName)).as("'"+expectedName + "' is NOT verified on the " +
                "page!").isTrue();

//        logger.info("Verified that "+expectedName+" is present on the page");
        logAndAssert().log("Verified that "+expectedName+" is present on the page");
    }


}
