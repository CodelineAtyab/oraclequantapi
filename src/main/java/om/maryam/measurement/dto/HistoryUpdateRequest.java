package om.maryam.measurement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload used for PUT/PATCH update of an existing history record.
 * Only mutable business fields are exposed.
 */
public class HistoryUpdateRequest {

    @NotBlank(message = "input must not be blank")
    private String input;

    @NotBlank(message = "output must not be blank")
    private String output;

    public HistoryUpdateRequest() { }

    public HistoryUpdateRequest(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public String getInput()                 { return input; }
    public void setInput(String input)       { this.input = input; }

    public String getOutput()                { return output; }
    public void setOutput(String output)     { this.output = output; }
}
