package turkey5.creategeyser.Create;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public final class CreateExtension implements Extension { 
    @Subscribe
    public void onPostInit(GeyserPostInitializeEvent event) {
        logger().info("Create: Geyser Loaded");
    }
}
