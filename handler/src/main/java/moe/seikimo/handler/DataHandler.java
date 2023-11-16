package moe.seikimo.handler;

@SuppressWarnings("unchecked")
public interface DataHandler<D> {
    /**
     * Public facing method for handling data.
     *
     * @param data The data.
     */
    default void handle(Object data) {
        this.handle0((D) data);
    }

    /**
     * Handle the data.
     *
     * @param data The data.
     */
    void handle0(D data);
}
