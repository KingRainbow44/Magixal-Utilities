package moe.seikimo.handler;

import java.util.HashMap;
import java.util.Map;

public final class ObjectHandler<O extends DataReceiver, D> {
    private final Map<Class<D>, ObjectDataHandler<O, D>> handlers = new HashMap<>();

    /**
     * Registers a handler.
     *
     * @param clazz The class.
     * @param handler The handler.
     */
    public void register(Class<D> clazz, ObjectDataHandler<O, D> handler) {
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
