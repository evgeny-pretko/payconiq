package tests;

import api.RestWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import utils.ConfigProvider;

public class BaseTest {

    protected static RestWrapper api;

    @BeforeAll
    public static void setup() {
        Assertions.assertTrue(RestWrapper.isHealthy());

        api = RestWrapper.createToken(ConfigProvider.ADMIN_LOGIN, ConfigProvider.ADMIN_PASSWORD);
    }

}