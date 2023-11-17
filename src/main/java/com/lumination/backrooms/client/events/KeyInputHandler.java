package com.lumination.backrooms.client.events;

import com.lumination.backrooms.client.screens.LevelScreen;
import com.lumination.backrooms.levels.Backroom;
import com.lumination.backrooms.utils.extensions.ConvertRooms;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class KeyInputHandler {
    private static final ConvertRooms cr = new ConvertRooms();
    public static final String KEY_CATEGORY = "mod.backrooms.name";
    public static final String KEY_LEVEL_INFO = "key.backrooms.level_info";

    public static KeyBinding levelInfoKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END.register(client -> {
            if (KeyInputHandler.levelInfoKey.wasPressed()) {
                assert client.player != null;
                String dimension = getBackroom(client.player).getNamespace();
                Backroom backroom = cr.convert(dimension);
                client.setScreen(new LevelScreen(backroom));
            }
        });
    }

    public static void register() {
        KeyInputHandler.levelInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LEVEL_INFO,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY
        ));

        KeyInputHandler.registerKeyInputs();
    }

    public static Identifier getBackroom(ClientPlayerEntity player) {
        RegistryKey<DimensionType> dim = player.getWorld().getDimensionKey();
        return dim.getValue();
    }
}
