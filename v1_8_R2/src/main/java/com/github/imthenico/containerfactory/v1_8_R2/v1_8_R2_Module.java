package com.github.imthenico.containerfactory.v1_8_R2;

import com.github.imthenico.containerfactory.ContainerModule;
import org.bukkit.inventory.BrewerInventory;

public class v1_8_R2_Module extends ContainerModule {

    public v1_8_R2_Module() {
        this.providers.put(BrewerInventory.class, new BrewingContainerProvider());
    }
}