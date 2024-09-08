import fr.traqueur.shared.api.SharedAPI;
import fr.traqueur.shared.api.SharedClient;
import fr.traqueur.shared.api.requests.EndPoints;

public class Test {

    public static void main(String[] args) {
        var endpoint = EndPoints.AUTH.get("server", "server").get("plugin", "plugin");
        System.out.println(SharedAPI.API_URL.resolve(endpoint));
    }

}
