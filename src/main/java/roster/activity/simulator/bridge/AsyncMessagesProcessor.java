package roster.activity.simulator.bridge;

import org.springframework.context.event.EventListener;
import roster.activity.simulator.bridge.domain.model.PluginMessage;
import roster.activity.simulator.generation.domain.model.StudentActivityPerformed;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class AsyncMessagesProcessor {

    private final Consumer<PluginMessage> consumer;
    private final ExecutorService executorService;

    public AsyncMessagesProcessor(Consumer<PluginMessage> consumer, ExecutorService executorService) {
        this.consumer = consumer;
        this.executorService = executorService;
    }

    @EventListener(StudentActivityPerformed.class)
    public void onStudentActivityPerformed(StudentActivityPerformed studentActivityPerformed) {
        executorService.submit(() -> consumer.accept(PluginMessage.fromActivityEvent(studentActivityPerformed)));
    }

}
