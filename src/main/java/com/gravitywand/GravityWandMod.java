package com.gravitywand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GravityWandMod implements ModInitializer {
	public static final String MOD_ID = "gravitywand";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Define the item ResourceKey (RegistryKey) mandatory for 26.1.2+
	public static final ResourceKey<Item> GRAVITY_WAND_KEY = ResourceKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(MOD_ID, "gravity_wand")
	);

	// Register our Gravity Wand item and link the key to its Properties
	public static final Item GRAVITY_WAND = new GravityWandItem(
		new Item.Properties().setId(GRAVITY_WAND_KEY).stacksTo(1)
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Gravity Wand Mod...");

		Registry.register(BuiltInRegistries.ITEM, GRAVITY_WAND_KEY, GRAVITY_WAND);

		// Add item to combat and tools creative tabs
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> {
			creativeTab.accept(GRAVITY_WAND);
		});
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> {
			creativeTab.accept(GRAVITY_WAND);
		});
	}
}