package com.github.imthenico.containerfactory.v1_8_R2;

import com.github.imthenico.containerfactory.ContainerFactory;
import com.github.imthenico.containerfactory.UpdatableContainer;
import net.minecraft.server.v1_8_R2.TileEntityBrewingStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class BrewingContainerProvider implements ContainerFactory.ContainerProvider<BrewerInventory> {

    @Override
    public BrewerInventory create(String name, int size, World world) {
        return new BrewingInventoryWrapper(name, ((CraftWorld) world).getHandle());
    }

    private static class BrewingInventoryWrapper extends CraftInventoryBrewer implements UpdatableContainer {

        public BrewingInventoryWrapper(String name, net.minecraft.server.v1_8_R2.World world) {
            super(new TileEntityBrewingStand());

            TileEntityBrewingStand brewingStand = ((TileEntityBrewingStand) inventory);
            brewingStand.a(world);
            brewingStand.a(name);
        }

        @Override
        public void update() {
            ((TileEntityBrewingStand) inventory).c();
        }
    }
}