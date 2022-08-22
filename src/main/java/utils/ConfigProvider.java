package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {

    Config config = readConfig();

    static Config readConfig() {
        return ConfigFactory.systemProperties().hasPath("testProfile")
                ? ConfigFactory.load(ConfigFactory.systemProperties().getString("testProfile"))
                : ConfigFactory.load("application.conf");
    }

    String BASE_URL = readConfig().getString("urls.base");
    String ADMIN_LOGIN = readConfig().getString("users.admin.login");
    String ADMIN_PASSWORD = readConfig().getString("users.admin.password");
}
