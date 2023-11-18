package moe.seikimo.handler;

import java.util.HashMap;
import java.util.Map;

public final class ObjectHandler<O extends DataReceiver, D> {
    private final Map<Class<? extends D>, ObjectDataHandler<O, ? extends D>> handlers = new HashMap<>();

    /**
     * Registers a handler.
     *
     * @param clazz The class.
     * @param handler The handler.
     */
    public void register(Class<? extends D> clazz, ObjectDataHandler<O, ? extends D> handler) {
        this.handlers.put(clazz, handler);
    }

    /**
     * Handles data.
     *
     * @param receiver The receiver.
     * @param data The data.
     */
    @SuppressWarnings("unchecked")
    public void handle(O receiver, Object data) {
        var clazz = (Class<D>) data.getClass();
        var handler = this.handlers.get(clazz);
        if (handler != null) {
            handler.handle(receiver, data);
        }
    }
}
