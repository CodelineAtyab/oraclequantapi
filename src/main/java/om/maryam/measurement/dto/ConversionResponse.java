package om.maryam.measurement.dto;

import java.util.List;

/**
 * Wrapper for the decoded conversion result.
 * The raw API endpoint returns the list directly (e.g. [2,7,7]) as required
 * by the evaluation specification.  This DTO is retained for internal reuse.
 */
public class ConversionResponse {

    private List<Integer> result;

    public ConversionResponse() { }

    public ConversionResponse(List<Integer> result) {
        this.result = result;
    }

    public List<Integer> getResult()                  { return result; }
    public void setResult(List<Integer> result)       { this.result = result; }
}
