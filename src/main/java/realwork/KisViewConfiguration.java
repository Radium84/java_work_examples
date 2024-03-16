package realwork;

import com.zaxxer.hikari.HikariDataSource;
import idm.kis.entity.KisEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "idm.kis",
        entityManagerFactoryRef = "kisEntityManagerFactory",
        transactionManagerRef= "kisTransactionManager"
)

public class KisViewConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties kisDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.configuration")
    public DataSource kisDataSource() {
        return kisDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }
    @Primary
    @Bean(name = "kisEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean kisEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(kisDataSource())
                .packages(KisEntity.class)
                .build();
    }
    @Primary
    @Bean
    public PlatformTransactionManager kisTransactionManager(
            final @Qualifier("kisEntityManagerFactory") LocalContainerEntityManagerFactoryBean kisEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(kisEntityManagerFactory.getObject()));
    }


}