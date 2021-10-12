package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_CODE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.ModuleCodesContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MODULE_CODE);
        List<String> moduleCodes = argMultimap.getAllValues(PREFIX_MODULE_CODE);

        if (!moduleCodes.isEmpty()) {
            try {
                Set<ModuleCode> moduleCodeSet = ParserUtil.parseModuleCodes(moduleCodes);
                List<String> stringListOfModuleCodes = moduleCodeSet.stream()
                        .map(moduleCode -> moduleCode.toString())
                        .collect(Collectors.toList());

                return new DeleteCommand(
                        new ModuleCodesContainsKeywordsPredicate(stringListOfModuleCodes));
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        try {
            if (args.contains("-")) {
                Index start = ParserUtil.parseIndex(args.substring(1, args.indexOf("-")));
                Index end = ParserUtil.parseIndex(args.substring(args.indexOf("-") + 1));
                return new DeleteCommand(start, end);
            } else {
                Index index = ParserUtil.parseIndex(args);
                return new DeleteCommand(index);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
