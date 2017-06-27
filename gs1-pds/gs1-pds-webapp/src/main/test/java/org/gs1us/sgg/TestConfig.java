package org.gs1us.sgg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gs1us.sgg.account.AccountManager;
import org.gs1us.sgg.app.AppManager;
import org.gs1us.sgg.app.dwcode.DigimarcOptions;
import org.gs1us.sgg.app.dwcode.DigimarcService;
import org.gs1us.sgg.app.dwcode.MockDigimarcClient;
import org.gs1us.sgg.clockservice.ClockService;
import org.gs1us.sgg.commerce.CommerceManager;
import org.gs1us.sgg.dao.GBDao;
import org.gs1us.sgg.dao.mock.MockGBDao;
import org.gs1us.sgg.gbservice.impl.GlobalBrokerServiceImpl;
import org.gs1us.sgg.gbservice.impl.ImportManager;
import org.gs1us.sgg.gbservice.impl.ProductOpsManager;
import org.gs1us.sgg.gbservice.test.TestManager;
import org.gs1us.sgg.product.ProductManager;
import org.gs1us.sgg.util.VersionOracle;
import org.gs1us.sgg.webapi.ApiController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class TestConfig {

  @Bean
  public ApiController apiController() {
    return new ApiController();
  }

  @Bean
  public GlobalBrokerServiceImpl globalBrokerService() {
    return new GlobalBrokerServiceImpl("test");
  }
  @Bean
  public ClockService clockService() {
    return new ClockService();
  }
  @Bean
  public AccountManager accountManager() {
    return new AccountManager();
  }
  @Bean
  public ProductOpsManager productOpsManager() {
    return new ProductOpsManager();
  }
  @Bean
  public ProductManager productManager() {
    return new ProductManager();
  }
  @Bean
  public AppManager appManager() {
    return new AppManager();
  }
  @Bean
  public CommerceManager commerceManager() {
    return new CommerceManager();
  }
  @Bean
  public ImportManager importManager() {
    return new ImportManager();
  }
  @Bean
  public TestManager testManager() {
    return new TestManager();
  }
  @Bean
  public VersionOracle versionOracle() {
    return new VersionOracle("mock/path");
  }

  @Bean
  public GBDao gbDao() {
    return new MockGBDao();
  }
  @Bean
  public DigimarcService digimarcService() {
    return new MockDigimarcClient();
  }

  @Bean
  public DigimarcOptions digimarcOptions() {
    return new DigimarcOptions();
  }
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}

