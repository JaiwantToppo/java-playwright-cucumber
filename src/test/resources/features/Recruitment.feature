Feature: OrangeHRM - Open source human resource management system - Recruitment Module

  # ==============================================================================
  # STEP 1: Run this scenario ONCE to save authentication state
  # Command: mvn test -Dcucumber.filter.tags="@SaveAuth"
  # This creates: src/test/test-output/storage-states/auth.json
  # ==============================================================================
  @Setup @SaveAuth
  Scenario: Save authentication state for recruitment tests
    Given I navigate to the OrangeHRM Login page
    And I enter username
    And I enter password
    And I click the Login button
    Then I should be logged in successfully

  # ==============================================================================
  # STEP 2: Run these scenarios - they will use saved authentication (NO LOGIN!)
  # Command: mvn test -Dcucumber.filter.tags="@Smoke"
  # These tests skip login and start already authenticated - 10x faster!
  # ==============================================================================
  @Smoke @RequiresAuth
  Scenario Outline: Add candidate to Recruitment Page
    # No login needed - @RequiresAuth tag loads saved authentication!
    Given I am on the dashboard
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
      | firstName | lastName | vacancy                  | emailAddress           | contactNumber | dateOfApplication |
      | John      | Williams | Junior Account Assistant | johnwilliams@gmail.com | 24349732342   | 2024-06-15        |
      | Jane      | Doe      | Sales Representative     | janedoe@gmail.com      | 23874676324   | 2024-06-16        |
      | Bob       | Smith    | Senior QA Lead           | bobsmith@gmail.com     | 23784623876   | 2024-06-17        |
      | Alice     | Knight   | Junior Account Assistant | aliceknight@gmail.com  | 2343432342    | 2024-06-15        |
      | Geoff     | Ray      | Sales Representative     | geoffray@gmail.com     | 2398787324    | 2024-06-16        |
      | Conner    | Gus      | Senior QA Lead           | connergus@gmail.com    | 2323645596    | 2024-06-17        |

  # ==============================================================================
  # ALTERNATIVE: Scenarios that still use traditional login (for comparison)
  # Use this if you want to test with fresh login each time
  # ==============================================================================
#  @Regression @WithLogin
#  Scenario Outline: Add candidate with fresh login
#    Given I navigate to the OrangeHRM Login page
#    And I enter username "Admin"
#    And I enter password "admin123"
#    And I click the Login button
#    Then I should be logged in successfully
#    When I navigate to section "Recruitment"
#    And I click the Add Candidate button
#    And I enter the candidate first name '<firstName>'
#    And I enter the candidate last name '<lastName>'
#    And I set the vacancy to '<vacancy>'
#    And I enter the candidate email address '<emailAddress>'
#    And I enter the candidate contact number '<contactNumber>'
#    And I set the date of application to '<dateOfApplication>'
#    And I enter the notes '<vacancy>'
#    And I click on the save button
#
#    Examples:
#      | firstName | lastName | vacancy                  | emailAddress           | contactNumber | dateOfApplication |
#      | John      | Williams | Junior Account Assistant | johnwilliams@gmail.com | 24349732342   | 2024-06-15        |
#      | Jane      | Doe      | Sales Representative     | janedoe@gmail.com      | 23874676324   | 2024-06-16        |
#      | Bob       | Smith    | Senior QA Lead           | bobsmith@gmail.com     | 23784623876   | 2024-06-17        |