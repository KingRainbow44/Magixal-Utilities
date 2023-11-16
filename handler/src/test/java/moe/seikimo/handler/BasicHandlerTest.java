package moe.seikimo.handler;

import org.junit.Assert;
import org.junit.Test;

public final class BasicHandlerTest {
    @Test
    public void handle() {
        var data = new Foo();
        data.bar = "foo";

        var handler = new BasicHandler<Foo>();
        handler.register(Foo.class, received ->
                Assert.assertEquals(received.bar, "foo"));
        handler.handle(data);
    }

    static class Foo {
        public String bar = "";
    }
}
