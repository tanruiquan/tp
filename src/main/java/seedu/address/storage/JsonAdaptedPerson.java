package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.TeleHandle;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String email;
    private final String remark;
    private final List<JsonAdaptedModuleCode> moduleCodes = new ArrayList<>();
    private final String phone;
    private final String teleHandle;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("email") String email,
                             @JsonProperty("moduleCodes") List<JsonAdaptedModuleCode> moduleCodes,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("teleHandle") String teleHandle,
                             @JsonProperty("remark") String remark,
                             @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.email = email;
        this.remark = remark;
        if (moduleCodes != null) {
            this.moduleCodes.addAll(moduleCodes);
        }
        this.phone = phone;
        this.teleHandle = teleHandle;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        email = source.getEmail().value;
        remark = source.getRemark().value;
        moduleCodes.addAll(source.getModuleCodes().stream()
                .map(JsonAdaptedModuleCode::new)
                .collect(Collectors.toList()));
        phone = source.getPhone().value;
        teleHandle = source.getTeleHandle().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        final List<ModuleCode> personModuleCodes = new ArrayList<>();
        for (JsonAdaptedModuleCode moduleCode : moduleCodes) {
            personModuleCodes.add(moduleCode.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (remark == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName()));
        }
        final Remark modelRemark = new Remark(remark);

        if (teleHandle == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    TeleHandle.class.getSimpleName()));
        }
        if (!TeleHandle.isValidTeleHandle(teleHandle)) {
            throw new IllegalValueException(TeleHandle.MESSAGE_CONSTRAINTS);
        }
        final TeleHandle modelTeleHandle = new TeleHandle(teleHandle);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<ModuleCode> modelModuleCodes = new HashSet<>(personModuleCodes);
        return new Person(modelName, modelEmail, modelModuleCodes, modelPhone, modelTeleHandle, modelRemark, modelTags);
    }

}
