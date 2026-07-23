Feature: Fabric Material and Neck Pattern Filter on WowFashions

  Background:
    Given user opens the Jalabiya products page

  # ── Fabric Material ────────────────────────────────────

  Scenario: Fabric Material section is collapsed by default
    Then the "Fabric Material" filter section should be collapsed by default


  Scenario: User selects single fabric - Chiffon Zari
    When user clicks on fabric material checkbox "Chiffon Zari"
    Then the checkbox "Chiffon Zari" should be selected
    And  the product grid should refresh


  Scenario: User selects single fabric - Cotton Italy
    When user clicks on fabric material checkbox "Cotton Italy"
    Then the checkbox "Cotton Italy" should be selected
    And  the product grid should refresh

  Scenario: User selects single fabric - Linen
    When user clicks on fabric material checkbox "Linen"
    Then the checkbox "Linen" should be selected
    And  the product grid should refresh

  Scenario: User selects multiple fabrics
    When user clicks on fabric material checkbox "Chiffon Zari"
    And  user clicks on fabric material checkbox "Linen"
    Then the checkbox "Chiffon Zari" should be selected
    And  the checkbox "Linen" should be selected
    And  the product grid should refresh

  Scenario: User deselects a fabric after selecting it
    When user clicks on fabric material checkbox "Crepe"
    Then the checkbox "Crepe" should be selected
    When user clicks on fabric material checkbox "Crepe"
    Then the checkbox "Crepe" should be deselected
    And  the product grid should refresh

  Scenario Outline: All fabric materials can be selected
    When user clicks on fabric material checkbox "<fabric>"
    Then the checkbox "<fabric>" should be selected
    And  the product grid should refresh

    Examples:
      | fabric          |
      | Chiffon Zari    |
      | Cotton Italy    |
      | Cotton Jacquard |
      | Cotton Spony    |
      | Crepe           |
      | Crepe Soft      |
      | Jacquard        |
      | Jacquard Namri  |
      | Silk Jacquard   |

  # ── Neck Pattern ───────────────────────────────────────

  Scenario: Neck Pattern section is collapsed by default
    Then the Neck Pattern filter section should be collapsed

  Scenario: User expands Neck Pattern section
    When user clicks to expand the "Neck Pattern" filter section
    Then the "Neck Pattern" filter options should be visible

  # ── Combined Filter ────────────────────────────────────

  Scenario: User applies both Fabric Material and Neck Pattern filters
    When user clicks on fabric material checkbox "Jacquard"
    And  user clicks to expand the "Neck Pattern" filter section
    And  user selects any first option in "Neck Pattern"
    Then the product grid should refresh

  # ── Reset ──────────────────────────────────────────────

  Scenario: All checkboxes are cleared after reset
    When user clicks on fabric material checkbox "Linen"
    And  user clicks on fabric material checkbox "Crepe"
    And  user clicks the Reset or Clear Filters button
    Then the checkbox "Linen" should be deselected
    And  the checkbox "Crepe" should be deselected