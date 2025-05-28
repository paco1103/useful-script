import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// "Enum Singleton pattern" for ConfigUtil
// This pattern ensures that the ConfigUtil instance is created only once and is thread-safe.
public enum ConfigUtil {
    INSTANCE();

    // Pat: src/main/resources/config/mtcaptcha_config.json
    static final String MTCAPTCHA_CONFIG = "/config/mtcaptcha_config.json";

    public static final String ENV_MTCAPTCHA_PRIVATE_KEY = "MTCAPTCHA_PRIVATE_KEY";
    public static final String ENV_MTCAPTCHA_PUBLIC_KEY = "MTCAPTCHA_PUBLIC_KEY";

    private final JsonObject json;

    // Initialize with default configuration file
    ConfigUtil() {
        this.json = loadJsonFromResource(MTCAPTCHA_CONFIG);
    }

    public String getValueByKey(String key) {
        return json.get(key).getAsString();
    }

    // Overloaded method to load from a provided configuration file
    public String getValueByKey(String configPath, String key) {
        JsonObject customJson = loadJsonFromResource(configPath);
        return customJson.get(key).getAsString();
    }

    private JsonObject loadJsonFromResource(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath);
                InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader)
                    .getAsJsonObject();
        } catch (IOException | NullPointerException e) {
            throw new UncheckedIOException(new IOException("Failed to load resource: " + resourcePath, e));
        }
    }

    public String getSystemEnv(String key) {
        return System.getenv(key);
    }
}
