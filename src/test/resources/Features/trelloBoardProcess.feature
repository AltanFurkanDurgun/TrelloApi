Feature: Trello board process

  @test
  Scenario: Create,update and delete board process
    When Create board.
    And Create a list on board.
    And Create a new card.
    And Update card.
    And Delete card.
    And Delete board.
