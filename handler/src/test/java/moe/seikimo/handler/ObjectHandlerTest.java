package moe.seikimo.handler;

import org.junit.Assert;
import org.junit.Test;

public final class ObjectHandlerTest {
    @Test
    public void handleLocally() {
        var data = new Foo();
        data.bar = "foo";

        var receiver = new FooReceiver();
        var handler = new ObjectHandler<FooReceiver, Foo>();
        handler.register(Foo.class, FooReceiver::isFoo);
        handler.handle(receiver, data);
    }

    @Test
    public void handleStatically() {
        var data = new Foo();
        data.bar = "bar";

        var receiver = new FooReceiver();
        var handler = new ObjectHandler<FooReceiver, Foo>();
        handler.register(Foo.class, FooReceiver::checkForBar);
        handler.handle(receiver, data);
    }

    static class Foo {
        public String bar = "";
    }

    static class FooReceiver implements DataReceiver {
        public void isFoo(Foo foo) {
            Assert.assertEquals(foo.bar, "foo");
        }

        public void notFoo(Foo foo) {
            Assert.assertNotEquals(foo.bar, "foo");
        }

        public static void checkForBar(FooReceiver receiver, Foo foo) {
            Assert.assertEquals(foo.bar, "bar");
            receiver.notFoo(foo);
        }
    }
}
