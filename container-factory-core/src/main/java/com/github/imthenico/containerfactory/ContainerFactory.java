package com.github.imthenico.containerfactory;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContainerFactory {

    private static final ContainerModule EMPTY_MODULE = new ContainerModule();

    private static ContainerModule PROVIDERS;

    private static final Map<InventoryType, Class<?>> classesByType = new HashMap<>();

    private static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];

    static {
        classesByType.put(InventoryType.BREWING, BrewerInventory.class);
        classesByType.put(InventoryType.FURNACE, Furnace.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Inventory> T createContainer(Class<T> inventoryClass, String name, int size, World world) {
        ContainerProvider<T> provider = (ContainerProvider<T>) getModule().getProviders().get(inventoryClass);

        if (provider != null)
            return provider.create(Objects.requireNonNull(name), size, world);

        throw new IllegalArgumentException("no provider registered for that type");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Inventory> T createContainer(InventoryType type, String name, int size, World world) {
        return createContainer((Class<? extends T>) classesByType.get(type), name != null ? name : type.getDefaultTitle(), size, world);
    }

    public static void findAnyModule() {
        if (PROVIDERS != null)
            throw new UnsupportedOperationException("unable to re-instantiate provider module");

        String className = "com.github.imthenico.containerfactory." + VERSION + "." + VERSION + "_Module";

        try {
            Class<ContainerModule> moduleClass = (Class<ContainerModule>) Class.forName(className);

            PROVIDERS = moduleClass.newInstance();
        } catch (Exception e) {
            PROVIDERS = EMPTY_MODULE;
            throw new RuntimeException("failed on find internal module", e);
        }
    }

    public static ContainerModule getModule() {
        if (PROVIDERS == null) {
            findAnyModule();
        } else if (PROVIDERS == EMPTY_MODULE) {
            throw new UnsupportedOperationException("no container found");
        }

        return PROVIDERS;
    }

    public interface ContainerProvider<T extends Inventory> {

        T create(String name, int size, World world);

    }
}