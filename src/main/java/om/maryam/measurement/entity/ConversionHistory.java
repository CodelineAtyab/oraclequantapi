package om.maryam.measurement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA entity that persists every measurement conversion request.
 *
 * Table:    MARYAM_CONVERSION_HISTORY
 * Sequence: MARYAM_CONV_HIST_SEQ
 */
@Entity
@Table(name = "MARYAM_CONVERSION_HISTORY")
public class ConversionHistory {

    @Id
    @SequenceGenerator(name = "maryam_conv_hist_seq",
            sequenceName = "MARYAM_CONV_HIST_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maryam_conv_hist_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_TS", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "SOURCE_IP", length = 64, nullable = false)
    private String sourceIpAddress;

    @Lob
    @Column(name = "INPUT_VALUE", nullable = false)
    private String input;

    @Lob
    @Column(name = "OUTPUT_VALUE", nullable = false)
    private String output;

    public ConversionHistory() { }

    public ConversionHistory(Long id, LocalDateTime timestamp, String sourceIpAddress,
                             String input, String output) {
        this.id = id;
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    /* ----------------------------- builder ----------------------------- */

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long id;
        private LocalDateTime timestamp;
        private String sourceIpAddress;
        private String input;
        private String output;

        public Builder id(Long id)                                 { this.id = id; return this; }
        public Builder timestamp(LocalDateTime timestamp)          { this.timestamp = timestamp; return this; }
        public Builder sourceIpAddress(String sourceIpAddress)     { this.sourceIpAddress = sourceIpAddress; return this; }
        public Builder input(String input)                         { this.input = input; return this; }
        public Builder output(String output)                       { this.output = output; return this; }

        public ConversionHistory build() {
            return new ConversionHistory(id, timestamp, sourceIpAddress, input, output);
        }
    }

    /* ----------------------------- getters / setters ------------------ */

    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }

    public LocalDateTime getTimestamp()                  { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp)    { this.timestamp = timestamp; }

    public String getSourceIpAddress()                   { return sourceIpAddress; }
    public void setSourceIpAddress(String sourceIpAddress){ this.sourceIpAddress = sourceIpAddress; }

    public String getInput()                             { return input; }
    public void setInput(String input)                   { this.input = input; }

    public String getOutput()                            { return output; }
    public void setOutput(String output)                 { this.output = output; }
}
