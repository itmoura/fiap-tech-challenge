Feature: User

  Scenario: Creating a new user as a default client.
    When I create a new user with the following details:
    """
      {
        "name": "Ítalo Moura",
        "email": "italomoura@meuemail.com",
        "password": "123456",
        "role": "CLIENT",
        "address": {
          "cep": "57072-420"
        }
    """
    Then the user should be created successfully