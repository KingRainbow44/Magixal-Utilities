package moe.seikimo.handler;

import java.util.HashMap;
import java.util.Map;

public final class BasicHandler<D> {
    private final Map<Class<D>, DataHandler<D>> handlers = new HashMap<>();

    /**
     * Registers a handler.
     *
     * @param clazz The class.
     * @param handler The handler.
     */
    public void register(Class<D> clazz, DataHandler<D> handler) {
        this.handlers.put(clazz, handler);
    }

    /**
     * Handles data.
     *
     * @param data The data.
     */
    @SuppressWarnings("unchecked")
    public void handle(Object data) {
        var clazz = (Class<D>) data.getClass();
        var handler = this.handlers.get(clazz);
        if (handler != null) {
            handler.handle(data);
        }
    }
}
