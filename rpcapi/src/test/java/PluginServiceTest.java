import com.enjin.core.EnjinServices;
import com.enjin.rpc.EnjinRPC;
import com.enjin.rpc.mappings.mappings.general.RPCData;
import com.enjin.rpc.mappings.mappings.plugin.PlayerInfo;
import com.enjin.rpc.mappings.mappings.plugin.Status;
import com.enjin.rpc.mappings.mappings.plugin.SyncResponse;
import com.enjin.rpc.mappings.mappings.plugin.TagData;
import com.enjin.rpc.mappings.services.PluginService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class PluginServiceTest {
    private static final String API_URL = "http://api.enjin.com/api/v1/";
    private static final String KEY = "cfc9718c515f63e26804af7f56b1c966de13501ecdad1ad41e";
    private static final int PORT = 25565;
    private static final String PLAYER = "Favorlock";

    @Test
    public void test1Auth() {
        PluginService service = EnjinServices.getService(PluginService.class);
        RPCData<Boolean> data = service.auth(KEY, PORT, true);

        Assert.assertNotNull("data is null", data);
        Assert.assertNotNull("result is null", data.getResult());
        Assert.assertTrue("result is not true", data.getResult());

        System.out.println("Successfully authenticated: " + data.getResult().booleanValue());
    }

    @Test
    public void test2Sync() {
        Status status = new Status(true,
                "2.8.2-bukkit",
                new ArrayList<String>() {{
                    add("world");
                    add("end");
                    add("nether");
                }},
                new ArrayList<String>() {{
                    add("default");
                    add("creeper");
                }},
                50,
                2,
                new ArrayList<PlayerInfo>(){{
                    add(new PlayerInfo("Favorlock", UUID.fromString("8b7a881c-6ccb-4ada-8f6a-60cc99e6aa20")));
                    add(new PlayerInfo("AlmightyToaster", UUID.fromString("5b6cf5cd-d1c8-4f54-a06e-9c4462095706")));
                }},
                null,
                null,
                null);
        PluginService service = EnjinServices.getService(PluginService.class);
        RPCData<SyncResponse> data = service.sync(KEY, status);

        Assert.assertNotNull("data is null", data);
        Assert.assertNotNull("result is null", data.getResult());

        System.out.println(data.getResult().toString());
    }

    @Test
    public void test3GetTags() {
        PluginService service = EnjinServices.getService(PluginService.class);
        RPCData<List<TagData>> data = service.getTags(KEY, PLAYER);

        Assert.assertNotNull("data is null", data);
        Assert.assertNotNull("result is null", data.getResult());

        System.out.println("# of tags: " + data.getResult().size());
    }

    @BeforeClass
    public static void prepare() {
        EnjinRPC.setDebug(true);
        EnjinRPC.setHttps(false);
        EnjinRPC.setApiUrl(API_URL);
    }
}
