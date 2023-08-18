package moe.seikimo.js;

import moe.seikimo.general.EncodingUtils;
import moe.seikimo.general.FileUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

public interface RenderingUtils {
    String POLYFILL_CONTENT = "const window=this;" +
            "window.setTimeout=()=>{};" +
            "window.clearTimeout=()=>{};" +
            "const document=this;";

    /**
     * Gets a template.
     *
     * @param name The name of the template.
     * @return The template.
     */
    static String getTemplate(String name) {
        return new String(FileUtils.getResource("html/" + name + ".html"));
    }

    /**
     * Renders a React.JS component.
     *
     * @param javaScriptBundle The JavaScript bundle.
     * @param bundleName The name of the JavaScript bundle.
     * @param template The template to render.
     * @param component The component to render.
     * @param props The component's props.
     * @param state The component's state.
     * @return The client HTML.
     */
    static String renderReact(
            String javaScriptBundle, String bundleName,
            String template, String component,
            Object props, Object state
    ) {
        try (var context = Context.newBuilder()
                .allowNativeAccess(true)
                .build()) {
            // Load the JavaScript bundle.
            context.eval("js", POLYFILL_CONTENT);
            context.eval(Source.newBuilder("js",
                    javaScriptBundle, bundleName).buildLiteral());

            // Fetch the render function.
            var renderFunc = context.getBindings("js")
                    .getMember("render")
                    .getMember(component);
            return renderFunc.execute(template,
                    EncodingUtils.jsonEncode(props),
                    EncodingUtils.jsonEncode(state))
                    .asString();
        }
    }
}
