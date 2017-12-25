package org.betterworldinternational.hugapi;

import com.google.gson.Gson;
import spark.ResponseTransformer;

class JsonResponseTransformer implements ResponseTransformer {
    private final Gson gson;

    public JsonResponseTransformer(Gson gson) {
        this.gson = gson;
    }

    /**
     * Method called for rendering the output.
     *
     * @param model object used to render output.
     * @return message that it is sent to client.
     * @throws Exception when render fails
     */
    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}