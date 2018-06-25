package info.horriblesubs.sher.model.base;

import java.io.Serializable;

@SuppressWarnings("all")
public class Response implements Serializable {

    public String code;
    public String message;


    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Code: " + this.code +
                "\n" + "Message: " + this.message;
    }
}
