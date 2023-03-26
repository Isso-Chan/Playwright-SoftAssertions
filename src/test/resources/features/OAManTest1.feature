@all @TC001
Feature: OAMan-1

  @TC002
  Scenario: TC001_1 User should be able add Unterlagen-1
    Given user logs in OAMan
    And User clicks VORHABEN
    And user clicks on "Status" to select an option
    And user selects "Eingereicht" from displayed options
    Then "Ben" should be equal to "Sen"
    And user clicks on the card of project "D - Förderung von Klammern (Klammer)"
    And user navigates "Unterlagen" tab
    When user clicks on "Unterlage erstellen" button on the page
    And user fills "Titel *" as "Created by Playwright"
    And user clicks "Speichernnn" button on the dialog
    Then created document should be displayed on the page
    And all softAssertions are executed

  @TC003
  Scenario: TC001_2 Select project with name, state and applicant
    Given user logs in OAMan
    And User clicks VORHABEN
    And user selects "1" project with name "", state "Entwurf" and applicant "Erika"
    When user clicks on selected project
    And all softAssertions are executed