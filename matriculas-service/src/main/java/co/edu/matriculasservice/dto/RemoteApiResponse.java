package co.edu.matriculasservice.dto;

/**
 * Copia local del contrato de respuesta de los otros servicios: {success, message, data}.
 * Cada microservicio define sus propias clases; no se comparte código entre servicios.
 */
public class RemoteApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
