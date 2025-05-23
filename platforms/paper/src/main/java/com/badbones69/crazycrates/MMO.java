package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.objects.ItemBuilder;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MMO {
    @Nullable
    public static ItemStack getDisplayItem(String mmoType, String mmoId, ItemBuilder itemBuilder) {
        Type type = MMOItems.plugin.getTypes().get(mmoType);
        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplate(type, mmoId);
        if (template == null) {
            CrazyCrates.getPlugin().getLogger().warning("找不到 MMOItems 物品 " + mmoType + ":" + mmoId);
            return null;
        }
        ItemStackBuilder builder = template.newBuilder().build().newBuilder();
        builder.getLore().getLore().addAll(itemBuilder.getUpdatedLore());
        ItemStack newItem = builder.build(true);
        if (newItem == null) {
            CrazyCrates.getPlugin().getLogger().warning("MMOItems 物品 " + mmoType + ":" + mmoId + " 构建失败");
        }
        return newItem;
    }
}
