package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @Test
    public void whenAddUserThenUserIsAdded() {
        var user = sql2oUserRepository.save(new User(1, "asdfasdfg", "alsidhjfb", "sdfg"));
        var result = sql2oUserRepository.findByEmailAndPassword("asdfasdfg", "sdfg");
        assertThat(result.get().getName()).isEqualTo("alsidhjfb");
    }

    @Test
    public void whenAddTwoUsersThenNothing() {
        sql2oUserRepository.save(new User(2, "asdfasdfg", "OLEG", "sdfg"));
        var result = sql2oUserRepository.findByEmailAndPassword("asdfasdfg", "sdfg");
        assertThat(result.get().getName()).isEqualTo("alsidhjfb");
    }
}