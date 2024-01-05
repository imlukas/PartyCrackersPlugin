package dev.imlukas.partycrackersplugin.util.schedulerutil.builders;

import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleBuilderBase;
import dev.imlukas.partycrackersplugin.util.schedulerutil.data.ScheduleData;
import lombok.Getter;

@Getter
public class RepeatableT2 implements ScheduleBuilderBase {

    private final ScheduleData data;

    RepeatableT2(ScheduleData data) {
        this.data = data;
    }

    public RepeatableBuilder run(Runnable runnable) {
        data.setRunnable(runnable);
        return new RepeatableBuilder(data);
    }
}
