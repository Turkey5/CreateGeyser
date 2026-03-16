package turkey5.creategeyser.Create;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path mods = Paths.get("mods");
        ItemRegistry itemRegistry = new ItemRegistry(this);
        boolean hydraulicPresent = itemRegistry.detectHydraulic(mods);
        logger().info("Hydraulic Detected: " + String.valueOf(hydraulicPresent));
        itemRegistry.loadJson();
        itemRegistry.defineCustomItems(event);
    }
}
