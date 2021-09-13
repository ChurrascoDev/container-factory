package com.github.imthenico.containerfactory;

import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface ContainerManager<K> {

    <T extends Inventory> T createContainer(Class<T> containerType, String name, int size, K key);

    Inventory create(InventoryType type, String name, int size, K key);

    Inventory create(InventoryType type, String name, K key);

    Inventory create(InventoryType type, K key);

    World getWorld();

    ContainerManager<K> toggleUpdater(boolean toggle);

    void unregister(K key);

    <T extends Inventory> T get(K key);

    boolean isUpdating();
}