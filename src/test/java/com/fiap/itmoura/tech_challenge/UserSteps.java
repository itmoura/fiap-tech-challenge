package com.fiap.itmoura.tech_challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TechChallengeApplication.class, loader = SpringBootContextLoader.class)
@CucumberContextConfiguration
@AutoConfigureMockMvc
public class UserSteps {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private LocalDate date;

    protected MockMvc getMockMvc() {
        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        }
        return this.mockMvc;
    }

    @When("I create a new user with the following details:")
    public void i_create_a_new_user_with_the_following_details(String docString) {
        var objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.convertValue(docString, UserDTO.class);

        throw new io.cucumber.java.PendingException();
    }
    @Then("the user should be created successfully")
    public void the_user_should_be_created_successfully() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
