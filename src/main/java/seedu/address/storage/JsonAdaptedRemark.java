package seedu.address.storage;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Remark;

/**
 * Jackson-friendly version of {@link Remark}.
 */
class JsonAdaptedRemark {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Remark's %s field is missing!";

    private final String text;
    private final String date;

    /**
     * Constructs a {@code JsonAdaptedRemark} with the given remark details.
     */
    @JsonCreator
    public JsonAdaptedRemark(@JsonProperty("text") String text,
                             @JsonProperty("date") String date) {
        this.text = text;
        this.date = date;
    }

    /**
     * Converts a given {@code Remark} into this class for Jackson use.
     */
    public JsonAdaptedRemark(Remark source) {
        this.text = source.getText();
        this.date = source.getDate().toString();
    }

    /**
     * Converts this Jackson-friendly adapted remark object into the model's {@code Remark} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Remark toModelType() throws IllegalValueException {
        if (text == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, "text"));
        }

        if (date == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }

        try {
            return new Remark(text, LocalDate.parse(date));
        } catch (Exception e) {
            throw new IllegalValueException("Invalid remark date: " + date);
        }
    }
}
