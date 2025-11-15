Feature: WebdriverUniversity.com - Contact Us Page
  Scenario Outline: : Valid Contact Us Form Submission
    Given I navigate to the webdriveruniversity homepage
    When I click on the contact us button
    And I type a first name <firstName>
    And I type a last name <lastName>
    And I enter an email address '<emailAddress>'
    And I type a comment '<comment>'
    And I click on the submit button
    Then I should be presented with a successful contact us submission message '<message>'

    Examples:
      | firstName | lastName | emailAddress           | comment   | message                     |
      | John      | Williams | johnwilliams@gmail.com | I am John | Thank You for your Message! |
      | Jane      | Doe      | janedoe@gmail.com      | I am Jane | Thank You for your Message! |
      | Bob       | Smith    | bobsmith@gmail.com     | I am Bob  | Thank You for your Message! |

  Scenario: Valid Contact Us Form Submission with Faker Library
    Given I navigate to the webdriveruniversity homepage
    When I click on the contact us button
    And I type a first name
    And I type a last name
    And I enter an email address
    And I type a comment
    And I click on the submit button
    Then I should be presented with a successful contact us submission message "Thank You for your Message!"