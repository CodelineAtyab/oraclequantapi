package om.maryam.measurement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Uniform error envelope returned for every non-2xx response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    public ApiError() { }

    public ApiError(LocalDateTime timestamp, int status, String error,
                    String message, String path, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private List<String> details;

        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder status(int status)                 { this.status = status; return this; }
        public Builder error(String error)                { this.error = error; return this; }
        public Builder message(String message)            { this.message = message; return this; }
        public Builder path(String path)                  { this.path = path; return this; }
        public Builder details(List<String> details)      { this.details = details; return this; }

        public ApiError build() {
            return new ApiError(timestamp, status, error, message, path, details);
        }
    }

    public LocalDateTime getTimestamp()           { return timestamp; }
    public void setTimestamp(LocalDateTime t)     { this.timestamp = t; }

    public int getStatus()                        { return status; }
    public void setStatus(int status)             { this.status = status; }

    public String getError()                      { return error; }
    public void setError(String error)            { this.error = error; }

    public String getMessage()                    { return message; }
    public void setMessage(String message)        { this.message = message; }

    public String getPath()                       { return path; }
    public void setPath(String path)              { this.path = path; }

    public List<String> getDetails()              { return details; }
    public void setDetails(List<String> details)  { this.details = details; }
}
