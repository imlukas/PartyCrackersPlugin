package dev.imlukas.partycrackersplugin.util.schedulerutil.builders;

import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleBuilderBase;
import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleData;
import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleThread;
import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleTimestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter(AccessLevel.PACKAGE)
public class ScheduleBuilder implements ScheduleBuilderBase {

    private ScheduleData data;

    // Presets

    public ScheduleBuilder(JavaPlugin plugin) {
        this.data = new ScheduleData();
        this.data.setPlugin(plugin);
    }

    public static ScheduleThread runIn1Tick(JavaPlugin plugin, Runnable runnable) {
        return new ScheduleBuilder(plugin)
                .in(1)
                .ticks()
                .run(runnable);
    }

    public ScheduleTimestamp<RepeatableT2> every(long number) {
        data.setRepeating(true);
        return new ScheduleTimestamp<>(new RepeatableT2(data), number, data::setTicks);
    }

    public ScheduleTimestamp<ScheduleBuilderT2> in(long number) {
        data.setRepeating(false);
        return new ScheduleTimestamp<>(new ScheduleBuilderT2(data), number, data::setTicks);
    }
}
