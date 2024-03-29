package org.vertex.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vertex.core.util.Triple;

import java.util.*;
import java.util.stream.Collectors;

public class ItemStackBuilder {
    private String title;
    private Material material = Material.APPLE;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    private Set<Triple<NamespacedKey, PersistentDataType<?,?>, Object>> persistentData = new HashSet<>();

    private Set<ItemFlag> flags = new HashSet<>();

    private Integer modelData = null;

    private int amount = 1;

    public static ItemStackBuilder factory() {
        return new ItemStackBuilder();
    }

    public ItemStackBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ItemStackBuilder flag(ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        this.lore.addAll(Arrays.asList(lore));
        return this;
    }

    public ItemStackBuilder lore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemStackBuilder glow() {
        this.flags.add(ItemFlag.HIDE_ENCHANTS);
        this.enchants.put(Enchantment.DAMAGE_ALL, 1);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder type(Material material) {
        this.material = material;
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchant, int level) {
        this.enchants.put(enchant, level);
        return this;
    }

    public ItemStackBuilder modelData(Integer data) {
        this.modelData = data;
        return this;
    }

    public ItemStackBuilder persistentData(NamespacedKey key, PersistentDataType<?,?> type, Object value) {
        this.persistentData.add(new Triple<>(key, type, value));
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material, amount);
        if(stack == null) {
            System.out.println("Error while creating stack.");
            return null;
        }
        stack.addUnsafeEnchantments(enchants);
        ItemMeta meta = Bukkit.getServer().getItemFactory().getItemMeta(material);
        if(title != null) {
            meta.setDisplayName(title.replace("&", "§"));
        }
        if(modelData != null) {
            meta.setCustomModelData(modelData);
        }
        flags.forEach(meta::addItemFlags);
        persistentData.forEach(x -> {
            meta.getPersistentDataContainer().set(x.getFirst(), (PersistentDataType) x.getSecond(), x.getThird());
        });
        meta.setLore(lore.stream().map(lore -> lore.replace("&", "§")).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return stack;
    }
}
