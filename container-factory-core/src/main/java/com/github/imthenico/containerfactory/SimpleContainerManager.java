package com.github.imthenico.containerfactory;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SimpleContainerManager<K> implements ContainerManager<K> {

    private final World world;

    private final Map<K, Inventory> containers;

    private final JavaPlugin plugin;

    private BukkitTask updaterTask;

    public SimpleContainerManager(World world, JavaPlugin plugin) {
        if (ContainerFactory.getModule() == null)
            throw new UnsupportedOperationException("cannot instantiate this class if no module is found in factory");

        this.world = Objects.requireNonNull(world, "world");
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.containers = new HashMap<>();
    }

    @Override
    public <U extends Inventory> U createContainer(Class<U> containerType, String name, int size, K key) {
        U container = ContainerFactory.createContainer(containerType, name, size, world);

        if (checkKey(key))
            containers.put(key, container);

        return container;
    }

    @Override
    public Inventory create(InventoryType type, String name, int size, K key) {
        Inventory container = ContainerFactory.createContainer(type, type.getDefaultTitle(), size, world);

        if (checkKey(key))
            containers.put(key, container);

        return container;
    }

    @Override
    public Inventory create(InventoryType type, String name, K key) {
        return create(type, name, type.getDefaultSize(), key);
    }

    @Override
    public Inventory create(InventoryType type, K key) {
        return create(type, type.getDefaultTitle(), type.getDefaultSize(), key);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public ContainerManager<K> toggleUpdater(boolean toggle) {
        if (toggle && updaterTask == null) {
            updaterTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (Inventory value : containers.values()) {
                    ((UpdatableContainer) value).update();
                }
            }, 0, 1);
        } else if (!toggle && updaterTask != null) {
            updaterTask.cancel();
            updaterTask = null;
        }

        return this;
    }

    @Override
    public void unregister(K key) {
        this.containers.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Inventory> T get(K key) {
        try {
            return (T) containers.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public boolean isUpdating() {
        return updaterTask != null;
    }

    private boolean checkKey(K key) {
        if (containers.containsKey(key))
            throw new IllegalArgumentException("there is already a container registered with this key");

        return key != null;
    }
}