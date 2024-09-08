import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.shared.api.SharedAPI;

import fr.traqueur.shared.api.domain.AuthBody;
import fr.traqueur.shared.api.requests.EndPoints;

import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        var endpoint = EndPoints.AUTH.get("server", "server").get("plugin", "plugin");
        System.out.println(SharedAPI.API_URL.resolve(endpoint));

        Gson gson = new GsonBuilder().create();
        AuthBody authBody = new AuthBody(UUID.randomUUID(), UUID.randomUUID());
        System.out.println(gson.toJson(authBody, AuthBody.class));
    }

}
