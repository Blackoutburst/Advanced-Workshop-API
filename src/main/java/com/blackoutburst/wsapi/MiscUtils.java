package com.blackoutburst.wsapi;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MiscUtils {

    public static String capitalize(String str) {
        return Arrays.stream(str.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String getItemName(Material item) {
        NBTCompound itemNBT = NBTItem.convertItemtoNBT(new ItemStack(item));
        String name = itemNBT.getString("id");

        if (name.length() < 10) return "";
        String filteredName = name.substring(10).replace("_", " ");
        filteredName = capitalize(filteredName);

        return filteredName;
    }

}
