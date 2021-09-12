package com.github.imthenico.containerfactory;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class ContainerModule {

    protected final Map<Class<? extends Inventory>, ContainerFactory.ContainerProvider<?>>  providers;

    public ContainerModule() {
        this.providers = new HashMap<>();
    }

    public Map<Class<? extends Inventory>, ContainerFactory.ContainerProvider<?>> getProviders() {
        return providers;
    }
}