package om.maryam.measurement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import om.maryam.measurement.service.MeasurementService;
import om.maryam.measurement.util.ClientIpResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public REST endpoint that performs measurement decoding.
 *
 * Exposed at <code>GET /maryam/convert-measurements?input=...</code>.
 * The response body is a raw JSON array of integers, exactly matching the
 * evaluation contract (e.g. {@code [2,7,7]}).
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Measurement Conversion")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ClientIpResolver ipResolver;

    public MeasurementController(MeasurementService measurementService,
                                 ClientIpResolver ipResolver) {
        this.measurementService = measurementService;
        this.ipResolver = ipResolver;
    }

    @Operation(summary = "Convert an encoded measurement string into package totals.")
    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convert(
            @Parameter(description = "Encoded measurement payload", example = "abcdabcdab")
            @RequestParam("input") @NotNull String input,
            HttpServletRequest request) {

        final String ip = ipResolver.resolve(request);
        final List<Integer> result = measurementService.convertAndRecord(input, ip);
        return ResponseEntity.ok(result);
    }
}
