package turkey5.creategeyser.Create;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.v2.CustomItemDefinition;
import org.geysermc.geyser.api.util.Identifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemRegistry {

    private final CreateExtension extension;
    private JsonNode root;
    private final ObjectMapper mapper = new ObjectMapper();

    public ItemRegistry(CreateExtension extension) {
        this.extension = extension;
    }

public boolean detectHydraulic(Path modsFolder) {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsFolder)) {
        for (Path file : stream) {
            String name = file.getFileName().toString().toLowerCase();
            if (name.contains("hydraulic") && name.endsWith(".jar")) {
                return true;
            }
        }
    } catch (IOException e) {
        extension.logger().error("Could not scan mods folder",e);
    }
    return false;
}

    public void loadJson() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("items.json")) {
            if (in == null) {
                extension.logger().error("items.json not found!");
                return;
            }
            root = mapper.readTree(in);
            extension.logger().info("Loaded items.json");
        } catch (Exception e) {
            extension.logger().error("Failed to load items.json", e);
        }
    }

    public void DefineCustomItems(GeyserDefineCustomItemsEvent event) {
        if (root == null || !root.has("items")) {
            extension.logger().warning("No items to register");
            return;
        }

        JsonNode items = root.get("items");

        items.fields().forEachRemaining(entry -> {
            String javaId = entry.getKey();
            JsonNode defs = entry.getValue();

            for (JsonNode def : defs) {
                String type = def.has("type") ? def.get("type").asText() : "vanilla";

                if (type.equals("non_vanilla")) {
                    extension.logger().info("Skipping non-vanilla registration for " + javaId + " (Hydraulic handles this)");
                    continue;
                }
                if (type.equals("vanilla")) {
                    registerDefinition(event, javaId, def);
                }
            }
        });
    }

    private void registerDefinition(GeyserDefineCustomItemsEvent event, String javaId, JsonNode def) {
        String bedrockId = def.get("bedrock_identifier").asText();
        String model = def.has("model") ? def.get("model").asText() : bedrockId;
        String displayName = def.has("display_name") ? def.get("display_name").asText() : null;

        CustomItemDefinition.Builder builder = CustomItemDefinition.builder(
                Identifier.of(bedrockId),
                Identifier.of(model)
        );

        if (displayName != null) {
            builder.displayName(displayName);
        }

        CustomItemDefinition definition = builder.build();
        event.register(Identifier.of(javaId), definition);

        extension.logger().info("Overriding definition for " + javaId + " -> " + bedrockId);
    }
}
