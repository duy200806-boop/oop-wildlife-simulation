import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class AudioSystem implements EventListener {
    private final Map<EventType, AudioClip> clips = new EnumMap<>(EventType.class);

    public AudioSystem(EventBus bus) {
        String base = "/resources/audio/";
        load(EventType.ATTACK, base + "roar.wav");
        load(EventType.EAT, base + "eat.wav");
        load(EventType.DEATH, base + "death.wav");
        load(EventType.BIRD_CHIRP, base + "bird.wav");
        load(EventType.LEAVES_RUSTLE, base + "leaves.wav");
        for (EventType t : EventType.values()) {
            bus.subscribe(t, this);
        }
    }

    private void load(EventType type, String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                clips.put(type, new AudioClip(url.toExternalForm()));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onEvent(EventType type) {
        AudioClip clip = clips.get(type);
        if (clip != null) {
            clip.play();
        } else {
            System.out.println("[Audio] " + type);
        }
    }
}
