package at.htl.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class KeycloakResource implements QuarkusTestResourceLifecycleManager {

    KeycloakContainer keycloak;

    @Override
    public Map<String, String> start() {
        keycloak = new KeycloakContainer()
                .withRealmImportFile("/quarkus-realm.json");
        keycloak.start();

        return Map.of("quarkus.oidc.auth-server-url", keycloak.getAuthServerUrl() + "/realms/quarkus");
    }

    @Override
    public void stop() {
        if (keycloak != null) {
            keycloak.stop();
        }
    }
}
