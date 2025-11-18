Feature: OrangeHRM - Open source human resource management system

  Background: Login to OrangeHRM
    Given I navigate to the OrangeHRM Login page
    And I enter username "Admin"
    And I enter password "admin123"
    And I click the Login button
    Then I should be logged in successfully

  @Smoke
  Scenario Outline: Add candidate to Recruitment Page
    When I navigate to section "Recruitment"
    And I click the Add Candidate button
    And I enter the candidate first name '<firstName>'
    And I enter the candidate last name '<lastName>'
    And I set the vacancy to '<vacancy>'
    And I enter the candidate email address '<emailAddress>'
    And I enter the candidate contact number '<contactNumber>'
    And I set the date of application to '<dateOfApplication>'
    And I enter the notes '<vacancy>'
    And I click on the save button

    Examples:
      | firstName | lastName | vacancy                  | emailAddress           | contactNumber   | dateOfApplication |
      | John      | Williams | Junior Account Assistant | johnwilliams@gmail.com | 24349732342     | 2024-06-15        |
      | Jane      | Doe      | Sales Representative     | janedoe@gmail.com      | 23874676324     | 2024-06-16        |
      | Bob       | Smith    | Senior QA Lead           | bobsmith@gmail.com     | 23784623876     | 2024-06-17        |

#  @Faker @Smoke
#  Scenario: Valid Contact Us Form Submission with Faker Library
#    And I type a first name
#    And I type a last name
#    And I enter an email address
#    And I type a comment
#    And I click on the submit button
#    Then I should be presented with a successful contact us submission message "Thank You for your Message!"