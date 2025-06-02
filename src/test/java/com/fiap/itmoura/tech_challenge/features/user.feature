Feature: User

  Scenario: Creating a new user as a default client.
    When I create a new user with the following details:
    """
      {
        "name": "Ítalo Moura",
        "email": "italomoura@meuemail.com",
        "password": "123456",
        "roles": ["CLIENT"],
        "address": {
          "zipCode": "57072-420",
          "street": "Rua tal",
          "number": "123",
          "complement": "Apto 45",
          "neighborhood": "Centro",
          "city": "Santa Rita do Sapucaí",
          "state": "MG"
        }
      }
    """
    Then the user with email "italomoura@meuemail.com" should be created successfully