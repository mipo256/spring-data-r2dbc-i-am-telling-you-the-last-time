package io.mpolivakha.streaming_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories(basePackageClasses = MyEntityJpaRepository.class)
@SpringBootApplication(scanBasePackageClasses = {Config.class})
//@Import(value = {
//    HibernateJpaAutoConfiguration.class,
//    JpaRepositoriesAutoConfiguration.class,
//    DataSourceAutoConfiguration.class
//})
@EnableAutoConfiguration(exclude = R2dbcAutoConfiguration.class)
public class Config {

  public static void main(String[] args) {
    SpringApplication.run(Config.class, args);
  }
}
