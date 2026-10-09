package SystemITR.JuanWhite2A.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse <T>{

    private boolean success;
    private String messaje;
    private T data;

    public ApiResponse(boolean success, String messaje, T data) {
        this.success = success;
        this.messaje = messaje;
        this.data = data;
    }

    public ApiResponse(boolean success, String messaje) {
        this.success = success;
        this.messaje = messaje;
        this.data = null;
    }
}
