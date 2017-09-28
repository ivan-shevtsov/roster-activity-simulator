package roster.activity.simulator.bridge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import roster.activity.simulator.generation.domain.model.StudentActivity;
import roster.activity.simulator.generation.domain.model.StudentActivityPerformed;

public @Data class PluginMessage {
    String url;
    String title;
    String favicon;
    Long time;
    String email;
    String guid;
    String device_id;
    String profile_id;
    String user_agent;

    public static PluginMessage fromActivityEvent(StudentActivityPerformed studentActivityPerformed) {
        StudentActivity studentActivity = studentActivityPerformed.getStudentActivity();

        PluginMessage pluginMessage = new PluginMessage();
        pluginMessage.url = studentActivity.getApplication().randomAction();
        pluginMessage.title = studentActivity.getApplication().getApplicationId();
        pluginMessage.time = studentActivityPerformed.getSimulatedInstant().toEpochMilli();
        pluginMessage.guid = studentActivity.getStudent().getEntityId().getIdValue();
        pluginMessage.user_agent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";

        return pluginMessage;
    }

    @Override
    public String toString() {
        return "PluginMessage{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", favicon='" + favicon + '\'' +
                ", time=" + time +
                ", email='" + email + '\'' +
                ", guid='" + guid + '\'' +
                ", device_id='" + device_id + '\'' +
                ", profile_id='" + profile_id + '\'' +
                ", user_agent='" + user_agent + '\'' +
                '}';
    }
}
