package dev.imlukas.partycrackersplugin.util.command.language.type;

import dev.imlukas.partycrackersplugin.util.command.language.type.impl.IntegerParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.NumericalParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.StringParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.filtered.PlayerParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.filtered.TimeParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.unit.MinecraftTime;
import org.bukkit.entity.Player;

public class ParameterTypes {

    public static final ParameterType<String> STRING = new StringParameterType();
    public static final ParameterType<Integer> INTEGER = new IntegerParameterType();
    public static final ParameterType<Double> NUMERICAL = new NumericalParameterType();
    public static final ParameterType<MinecraftTime> MINECRAFT_TIME = new TimeParameterType();
    public static final ParameterType<Player> PLAYER = new PlayerParameterType();
    public static final ParameterType<Player> OFFLINE_PLAYER = new PlayerParameterType();

}
