Feature: OrangeHRM - Open source human resource management system - Recruitment Module

  # ==============================================================================
  # STEP 1: Run this scenario ONCE to save authentication state
  # Command: mvn test -Dcucumber.filter.tags="@SaveAuth"
  # This creates: src/test/test-output/storage-states/auth.json
  # ==============================================================================
  @Setup @SaveAuth
  Scenario: Save authentication state for admin tests
    Given I navigate to the OrangeHRM Login page
    And I enter username
    And I enter password
    And I click the Login button
    Then I should be logged in successfully

  #================================================================================
  # STEP 2: Run these scenarios - they will use saved authentication (NO LOGIN!)
  # Command: mvn test -Dcucumber.filter.tags="@Smoke"
  # These tests skip login and start already authenticated - 10x faster!
  # ==============================================================================
  @Smoke @RequiresAuth
  Scenario Outline: Add user in Admin Page
    # No login needed - @RequiresAuth tag loads saved authentication!
    Given I am on the dashboard
    When I navigate to section "Admin"
    And I click the Add User button
    And I set the user role to '<userRole>'
    And I enter the employee name '<employeeName>'
    And I set the status to '<status>'
    And I enter the username '<username>'
    And I set the password '<password>'
    And I confirm the password '<confirmPassword>'
    And I click on the save button

    Examples:
      | userRole        | employeeName   | status   | username      | password   | confirmPassword |
      | Admin           | Linda Anderson | Enabled  | lindaanderson | Pass1234!  | Pass1234!       |
      | ESS             | Kevin Brown    | Disabled | kevinbrown    | Pass1234!  | Pass1234!       |
      | Admin           | Sarah Davis    | Enabled  | sarahdavis    | Pass1234!  | Pass1234!       |
      | ESS             | Michael Scott  | Disabled | michaelscott  | Pass1234!  | Pass1234!       |
      | Admin           | Emma Wilson    | Enabled  | emmawilson    | Pass1234!  | Pass1234!       |
      | ESS             | David Lee      | Disabled | davidlee      | Pass1234!  | Pass1234!       |