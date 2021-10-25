package io.maybank.currenciesservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Error object
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Schema(name = "Error")
public class ErrorDTO {

    /**
     * One of a server-defined set of error codes.
     */
    private String code;

    /**
     * A human-readable representation of the error.
     */
    private String message;

    /**
     * The target of the error.
     */
    @With
    private String target;

    /**
     * An array of details about specific errors that led to this reported error.
     */
    @With
    private List<ErrorDTO> details;

    /**
     * Masked Error identification (UUID).
     */
    @With
    private String errorId;

    public ErrorDTO(String code, String message) {
        this(code, message, null, null, null);
    }

    public ErrorDTO(String code, String message, String target) {
        this(code, message, target, null, null);
    }

    public ErrorDTO(String code, String message, List<ErrorDTO> details) {
        this(code, message, null, details, null);
    }

    public ErrorDTO(String code, String message, String target, List<ErrorDTO> details) {
        this(code, message, target, details, null);
    }

    /**
     * Simple null-safe comparison whether this error has given code.
     *
     * @param code Code to be compared against this {@link #code}
     * @return Whether given code matches with the {@link #code}
     */
    public boolean hasCode(String code) {
        return Objects.equals(this.code, code);
    }

    /**
     * Simple null-safe comparison whether this error has given target.
     *
     * @param target Target to be compared against this {@link #target}
     * @return Whether given target matches with the {@link #target}
     */
    public boolean hasTarget(String target) {
        return Objects.equals(this.target, target);
    }

    /**
     * Simple combination of {@link #hasCode(String)} and {@link #hasTarget(String)}.
     *
     * @param code   Code to be compared against this {@link #code}
     * @param target Target to be compared against this {@link #target}
     * @return Whether given both code and target match with this error
     */
    public boolean hasCodeAndTarget(String code, String target) {
        return hasCode(code) && hasTarget(target);
    }

    /**
     * @param predicate Predicate to be evaluated on errors in {@link #details}
     * @return Whether any error from {@link #details} match with given predicate
     */
    public boolean anyErrorFromDetailsMatch(Predicate<? super ErrorDTO> predicate) {
        return details != null && details.stream().anyMatch(predicate);
    }

    /**
     * @param predicate Predicate to be evaluated on errors in {@link #details}
     * @return Whether all errors from {@link #details} match with given predicate (true is returned also if no detail is present)
     */
    public boolean allErrorsFromDetailsMatch(Predicate<? super ErrorDTO> predicate) {
        return details == null || details.stream().allMatch(predicate);
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        appendErrorToStringBuilder(stringBuilder, this, "");
        return stringBuilder.toString();
    }

    private static void appendErrorToStringBuilder(StringBuilder stringBuilder, ErrorDTO error, String prefix) {
        stringBuilder.ensureCapacity(stringBuilder.length() + 32);
        stringBuilder.append(prefix);
        if (error.target != null) {
            stringBuilder.append(error.target).append(": ");
        }
        if (error.code != null) {
            stringBuilder.append('[').append(error.code).append("] ");
        }
        stringBuilder.append(error.message);
        if (!CollectionUtils.isEmpty(error.details)) {
            stringBuilder.append('\n');
            appendErrorsToStringBuilder(stringBuilder, error.details, (prefix.isEmpty() ? "\t* " : ("\t" + prefix)));
        }
    }

    static void appendErrorsToStringBuilder(StringBuilder stringBuilder, List<ErrorDTO> errors, String prefix) {
        if (errors != null) {
            var detailsMaxIndex = errors.size() - 1;
            if (detailsMaxIndex >= 0) {
                for (int i = 0; i <= detailsMaxIndex; i++) {
                    var detail = errors.get(i);
                    appendErrorToStringBuilder(stringBuilder, detail, prefix);
                    if (i < detailsMaxIndex) {
                        stringBuilder.append('\n');
                    }
                }
            }
        }
    }
}
