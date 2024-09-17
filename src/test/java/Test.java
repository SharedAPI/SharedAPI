import fr.traqueur.shared.api.SharedAPI;
import fr.traqueur.shared.api.domain.Table;

import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        /*
        var endpoint = EndPoints.AUTH.get("server", "server").get("plugin", "plugin");
        System.out.println(SharedAPI.API_URL.resolve(endpoint));

        Gson gson = new GsonBuilder().create();
        AuthBody authBody = new AuthBody(UUID.randomUUID(), UUID.randomUUID());
        System.out.println(gson.toJson(authBody, AuthBody.class));
        */

        SharedAPI api = new SharedAPI(null,
                UUID.fromString("fec25486-5d82-42ff-b5fe-691432b42f21"),
                UUID.fromString("42545d71-0f5b-46cd-9f04-77f82a52f296"));

        /*
        * api.registerTable(<table_name>);
        api.registerTable(<table_name>);



        api.createTables();
        * */

        api.registerTable("table_test", Table.class);
        api.registerTable("table_test2", Table2.class);
        api.loadSchema();

    }

    public static record Table(String name, String lastName, double money) implements fr.traqueur.shared.api.domain.Table {

        @Override
        public String getName() {
            return "table_test";
        }
    }

    public static record Table2(UUID uuid, String test, float testfloat) implements fr.traqueur.shared.api.domain.Table {

        @Override
        public String getName() {
            return "table_test2";
        }
    }

}
