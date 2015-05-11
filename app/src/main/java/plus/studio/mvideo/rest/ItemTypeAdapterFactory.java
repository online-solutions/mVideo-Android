package plus.studio.mvideo.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by yohananjr13 on 5/11/15.
 */
public class ItemTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);


        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                delegate.write(jsonWriter, t);
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {

                JsonElement jsonElement = elementAdapter.read(jsonReader);
                if(jsonElement.isJsonObject()){
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if(jsonObject.has("code") && jsonObject.get("code").getAsInt() == 404){
                        throw new IllegalArgumentException(jsonObject.get("message").getAsString());
                    }
                }
                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}
