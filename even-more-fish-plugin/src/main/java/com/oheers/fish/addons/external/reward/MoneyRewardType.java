package com.oheers.fish.addons.external.reward;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.api.economy.Economy;
import com.oheers.fish.api.economy.EconomyType;
import com.oheers.fish.api.reward.RewardType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MoneyRewardType extends RewardType {

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value, Location hookLocation) {
        Economy economy = Economy.getInstance();
        int amount;
        try {
            amount = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            EvenMoreFish.getInstance().getLogger().warning("Invalid number specified for RewardType " + getIdentifier() + ": " + value);
            return;
        }
        if (!economy.isEnabled()) {
            return;
        }
        EconomyType vault = economy.getEconomyType("Vault");
        if (vault == null) {
            return;
        }
        vault.deposit(player, amount, false);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "MONEY";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Oheers";
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return EvenMoreFish.getInstance();
    }

}
