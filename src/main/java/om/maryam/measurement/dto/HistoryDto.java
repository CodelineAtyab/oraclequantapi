package om.maryam.measurement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Transport object representing a persisted history record returned by the API.
 * Decouples the JPA entity from the public REST contract.
 */
public class HistoryDto {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String sourceIpAddress;
    private String input;
    private String output;

    public HistoryDto() { }

    public HistoryDto(Long id, LocalDateTime timestamp, String sourceIpAddress,
                      String input, String output) {
        this.id = id;
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long id;
        private LocalDateTime timestamp;
        private String sourceIpAddress;
        private String input;
        private String output;

        public Builder id(Long id)                              { this.id = id; return this; }
        public Builder timestamp(LocalDateTime timestamp)       { this.timestamp = timestamp; return this; }
        public Builder sourceIpAddress(String sourceIpAddress)  { this.sourceIpAddress = sourceIpAddress; return this; }
        public Builder input(String input)                      { this.input = input; return this; }
        public Builder output(String output)                    { this.output = output; return this; }

        public HistoryDto build() {
            return new HistoryDto(id, timestamp, sourceIpAddress, input, output);
        }
    }

    public Long getId()                                   { return id; }
    public void setId(Long id)                            { this.id = id; }

    public LocalDateTime getTimestamp()                   { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp)     { this.timestamp = timestamp; }

    public String getSourceIpAddress()                    { return sourceIpAddress; }
    public void setSourceIpAddress(String sourceIpAddress){ this.sourceIpAddress = sourceIpAddress; }

    public String getInput()                              { return input; }
    public void setInput(String input)                    { this.input = input; }

    public String getOutput()                             { return output; }
    public void setOutput(String output)                  { this.output = output; }
}
