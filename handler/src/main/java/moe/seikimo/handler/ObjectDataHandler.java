package moe.seikimo.handler;

@SuppressWarnings("unchecked")
public interface ObjectDataHandler<O, D> {
    /**
     * Public facing method for handling data.
     *
     * @param receiver The receiver.
     * @param data The data.
     */
    default void handle(O receiver, Object data) {
        this.handle0(receiver, (D) data);
    }

    /**
     * Handle the data.
     *
     * @param receiver The receiver.
     * @param data The data.
     */
    void handle0(O receiver, D data);
}
