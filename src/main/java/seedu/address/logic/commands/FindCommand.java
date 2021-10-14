package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ModuleCodesContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String MESSAGE_USAGE = "find: Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive)\n"
            + "Alternatively, finds all persons whose module codes contains all of the specified\n"
            + "module code(s) (case-insensitive).\n"
            + "Displays the results as a list with index numbers.\n"
            + "Parameters: " + PREFIX_NAME + " [name]...\n"
            + "OR\n"
            + PREFIX_MODULE_CODE + "[module code]...\n"
            + "Example:\n"
            + "find " + PREFIX_NAME + "alice bob charlie\n"
            + "find " + PREFIX_MODULE_CODE + "CS2030S CS2100";
    public static final String MESSAGE_SINGLE_PREFIX_SEARCH = "You can only search with a single prefix.";

    private final Predicate<Person> predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(ModuleCodesContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindCommand(TagsContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
