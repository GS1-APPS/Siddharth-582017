package org.gs1us.sgg.webapi;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.gs1us.sgg.TestConfig;
import org.gs1us.sgg.dao.AgentUser;
import org.gs1us.sgg.gbservice.impl.GlobalBrokerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class ApiControllerTest {

  // TODO: move to base test class
  @Resource
  private WebApplicationContext wac;

  protected MockMvc mockMvc;

  @Resource
  private ApiController apiController;

  @Mocked
  private GlobalBrokerServiceImpl globalBrokerService;

  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void versionGet() throws Exception {
    new Expectations() {{
      // mock the behavior of the object being used by ApiController
      globalBrokerService.getVersion((AgentUser)any);
      returns("1", null);
    }};

    mockMvc.perform(MockMvcRequestBuilders.get("/api/version")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.jsonPath("$").value(1))
        .andDo(print())
        .andReturn();

    new Verifications() {{
      globalBrokerService.getVersion((AgentUser)any);
      times=1;
    }};
  }


}