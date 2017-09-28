package roster.activity.simulator.bridge;

import org.springframework.context.event.EventListener;
import roster.activity.simulator.bridge.domain.model.PluginMessage;
import roster.activity.simulator.generation.domain.model.StudentActivityPerformed;

import java.util.function.Consumer;

public class ConsoleBridge implements Consumer<PluginMessage> {

    @Override
    public void accept(PluginMessage pluginMessage) {
        System.out.println(pluginMessage);
    }

}