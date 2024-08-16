package moe.seikimo.handler;

import java.util.HashMap;
import java.util.Map;

public final class ObjectHandler<Handler, BaseType> {
    private final Map<Class<? extends BaseType>, ObjectDataHandler<Handler, ? extends BaseType>> handlers = new HashMap<>();

    /**
     * Registers a handler.
     *
     * @param clazz The class.
     * @param handler The handler.
     */
    public <ActualType extends BaseType> void register(Class<ActualType> clazz, ObjectDataHandler<Handler, ActualType> handler) {
        this.handlers.put(clazz, handler);
    }

    /**
     * Handles data.
     *
     * @param receiver The receiver.
     * @param data The data.
     */
    @SuppressWarnings("unchecked")
    public void handle(Handler receiver, Object data) {
        var clazz = (Class<BaseType>) data.getClass();
        var handler = this.handlers.get(clazz);
        if (handler != null) {
            handler.handle(receiver, data);
        }
    }
}
