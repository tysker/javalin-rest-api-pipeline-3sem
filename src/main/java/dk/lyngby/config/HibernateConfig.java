package dk.lyngby.config;


import dk.lyngby.model.Hotel;
import dk.lyngby.security.model.Role;
import dk.lyngby.security.model.User;
import dk.lyngby.util.ApiProps;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HibernateConfig {

    // ==== VARIABLES ====
    private static boolean IS_TEST = false;
    private static EntityManagerFactory entityManagerFactory;

    // ==== PUBLIC METHODS ====
    public static void setTest(boolean isTest) {
        IS_TEST = isTest;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (IS_TEST) return getEntityManagerFactoryConfigTest();
        if (System.getenv("PRODUCTION") != null) return getEntityManagerFactoryConfigProduction();
        return getEntityManagerFactoryConfigDevelopment();
    }

    // ==== PRIVATE METHODS ====
    private static EntityManagerFactory getEntityManagerFactoryConfigTest() {
        if (entityManagerFactory == null) entityManagerFactory = setupHibernateConfigurationForTesting();
        return entityManagerFactory;
    }

    private static EntityManagerFactory getEntityManagerFactoryConfigDevelopment() {
        if (entityManagerFactory == null) entityManagerFactory = setupHibernateConfigurationForDevelopment();
        return entityManagerFactory;
    }

    private static EntityManagerFactory getEntityManagerFactoryConfigProduction() {
        if (entityManagerFactory == null) entityManagerFactory = setupHibernateConfigurationForProduction();
        return entityManagerFactory;
    }

    private static EntityManagerFactory setupHibernateConfigurationForProduction() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            hibernateProductionConfiguration(props);
            hibernateBasicConfiguration(props);
            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static EntityManagerFactory setupHibernateConfigurationForDevelopment() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            hibernateDevelopmentConfiguration(props);
            hibernateBasicConfiguration(props);
            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static EntityManagerFactory setupHibernateConfigurationForTesting() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            props.put("hibernate.dialect", ApiProps.DATABASE_DIALECT);
            props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
            props.put("hibernate.connection.url", ApiProps.DATABASE_TEST_CONTAINER_IMAGE);
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "postgres");
            props.put("hibernate.archive.autodetection", "class");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.hbm2ddl.auto", "create-drop");
            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void hibernateDevelopmentConfiguration(Properties props) throws IOException {
        props.put("hibernate.connection.url", ApiProps.DATABASE_URL);
        props.put("hibernate.connection.username", ApiProps.DATABASE_USERNAME);
        props.put("hibernate.connection.password", ApiProps.DATABASE_PASSWORD);
    }

    private static void hibernateProductionConfiguration(Properties props) {
        props.put("hibernate.connection.url", System.getenv("DATABASE_URL"));
        props.put("hibernate.connection.username", System.getenv("DATABASE_USERNAME"));
        props.put("hibernate.connection.password", System.getenv("DATABASE_PASSWORD"));
    }


    private static void hibernateBasicConfiguration(Properties props) {
        props.put("hibernate.show_sql", "false"); // show sql in console
        props.put("hibernate.format_sql", "false"); // format sql in console
        props.put("hibernate.use_sql_comments", "false"); // show sql comments in console
        props.put("hibernate.dialect", ApiProps.DATABASE_DIALECT); // dialect for postgresql
        props.put("hibernate.connection.driver_class", ApiProps.DATABASE_DRIVER); // driver class for postgresql
        props.put("hibernate.archive.autodetection", "class"); // hibernate scans for annotated classes
        props.put("hibernate.current_session_context_class", "thread"); // hibernate current session context
        props.put("hibernate.hbm2ddl.auto", "update"); // hibernate creates tables based on entities
    }

    private static EntityManagerFactory getEntityManagerFactory(Configuration configuration, Properties props) {
        configuration.setProperties(props);
        getAnnotationConfiguration(configuration);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
        return sf.unwrap(EntityManagerFactory.class);
    }

    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(Hotel.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(User.class);
    }

}
