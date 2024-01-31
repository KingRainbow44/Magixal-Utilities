package moe.seikimo.general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public interface EncodingUtils {
    AtomicReference<Gson> JSON = new AtomicReference<>(
            new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .registerTypeAdapter(JObject.class, new JObject.Adapter())
                    .setPrettyPrinting()
                    .create()
    );

    /**
     * Converts an object to JSON.
     *
     * @param object The object to convert.
     * @return The JSON element.
     */
    static JsonElement toJson(Object object) {
        return JSON.get().toJsonTree(object);
    }

    /**
     * Converts an object to JSON.
     *
     * @param object The object to convert.
     * @return The JSON object.
     */
    static String jsonEncode(Object object) {
        return JSON.get().toJson(object);
    }

    /**
     * Reads a file for JSON contents.
     * Converts the JSON to an object.
     *
     * @param file The file to read.
     * @param type The type of the object.
     * @return The object.
     */
    static <T> T jsonDecode(File file, Class<T> type)
            throws IOException {
        return JSON.get().fromJson(new FileReader(file), type);
    }

    /**
     * Uses a reader for JSON contents.
     * Converts the JSON to an object.
     *
     * @param reader The reader to pull data from.
     * @param type The type of the object.
     * @return The object.
     */
    static <T> T jsonDecode(Reader reader, Class<T> type) {
        return JSON.get().fromJson(reader, type);
    }

    /**
     * Reads a file for JSON contents.
     * Converts the JSON to an object.
     * This will throw an unchecked exception if an error occurs.
     *
     * @param file The file to read.
     * @param type The type of the object.
     * @return The object.
     */
    static <T> T silentJsonDecode(File file, Class<T> type) {
        try {
            return EncodingUtils.jsonDecode(file, type);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Converts a JSON string to an object.
     *
     * @param json The JSON string.
     * @param type The type of the object.
     * @return The object.
     */
    static <T> T jsonDecode(String json, Class<T> type) {
        return JSON.get().fromJson(json, type);
    }

    /**
     * Converts a JSON string to an object.
     *
     * @param json The JSON string.
     * @param type The type of the object.
     * @return The object.
     */
    static <T> T jsonDecode(String json, Type type) {
        return JSON.get().fromJson(json, type);
    }

    /**
     * Decodes a Base64 string.
     *
     * @param input The input string.
     * @return The decoded byte array.
     */
    static byte[] base64Decode(String input) {
        return Base64.getUrlDecoder().decode(input);
    }

    /**
     * Decodes a Base64 byte array.
     *
     * @param input The input byte array.
     * @return The decoded byte array.
     */
    static byte[] base64Decode(byte[] input) {
        return Base64.getUrlDecoder().decode(input);
    }

    /**
     * Decodes a Base64 string.
     *
     * @param input The input string.
     * @return The decoded string.
     */
    static String strBase64Decode(String input) {
        return new String(Base64.getUrlDecoder().decode(input));
    }

    /**
     * Decodes a Base64 byte array.
     *
     * @param input The input byte array.
     * @return The decoded string.
     */
    static String strBase64Decode(byte[] input) {
        return new String(Base64.getUrlDecoder().decode(input));
    }

    /**
     * Encodes a string to Base64.
     *
     * @param data The data to encode.
     * @return The encoded data as a string.
     */
    static String base64Encode(String data) {
        return Base64.getUrlEncoder().encodeToString(data.getBytes());
    }

    /**
     * Encodes a byte array to Base64.
     *
     * @param data The data to encode.
     * @return The encoded data as a string.
     */
    static String base64Encode(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data);
    }

    /**
     * Encodes a string to Base64.
     *
     * @param data The data to encode.
     * @return The encoded data as a byte array.
     */
    static byte[] bytesBase64Encode(String data) {
        return Base64.getUrlEncoder().encode(data.getBytes());
    }

    /**
     * Encodes a byte array to Base64.
     *
     * @param data The data to encode.
     * @return The encoded data as a byte array.
     */
    static byte[] bytesBase64Encode(byte[] data) {
        return Base64.getUrlEncoder().encode(data);
    }
}
