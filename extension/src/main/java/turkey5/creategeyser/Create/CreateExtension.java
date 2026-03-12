package turkey5.creategeyser.Create;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;

public final class CreateExtension implements Extension { 
    @Subscribe
    public void onPreInit(GeyserPreInitializeEvent event) {
        logger().info("Create: Geyser Initializing");
    }
    @Subscribe
    public void onPostInit(GeyserPostInitializeEvent event) {
        logger().info("~~~~~~~~~~~~~~~~~~~~~");
        logger().info("Create: Geyser Loaded");
        logger().info("~~~~~~~~~~~~~~~~~~~~~");
        
    }
    @Subscribe
    public void onGeyserDefineCustomItems(GeyserDefineCustomItemsEvent event) {
        logger().info("Defining items");
        ItemRegistry itemRegistry = new ItemRegistry(this);
        logger().info("Hydraulic Detected: " + String.valueOf(itemRegistry.detectHydraulic()));
        itemRegistry.loadJson();
        itemRegistry.DefineCustomItems(event);
    }
}
