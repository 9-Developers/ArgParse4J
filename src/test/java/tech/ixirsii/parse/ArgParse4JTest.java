package tech.ixirsii.parse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tech.ixirsii.parse.command.Command;
import tech.ixirsii.parse.command.CommandResult;
import tech.ixirsii.parse.command.OptionalArgument;
import tech.ixirsii.parse.command.PositionalArgument;
import tech.ixirsii.parse.parser.ListParser;
import tech.ixirsii.parse.parser.Parser;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Test cases.
 *
 * **Example:**
 *   `command -flags --option1 value1 option2=value2 argument3 "value 3" argument4=value4,"value,5" value6`
 * **Where:**
 *   `-flags` are the POSIX short options `-f -l -a -g -s`
 *   `--option1 value1` is the GNU long option `option1` with the value `value1`
 *   `option2=value2` is a named option `option2` with the value `value2`
 *   `argument3 value3` is a named positional argument `argument3` with the value `value 3`
 *   `argument4=value4,"value,5"` is a named positional argument list containing the values [`value4`, `value,5`]
 *   `value6` is a positional argument with the value `value6`
 *
 * **Example:**
 *   `command value2 argument1=value1`
 */
class ArgParse4JTest {
    private final Command command;

    ArgParse4JTest() {
        final List<PositionalArgument<?>> positionalArguments = List.of(
                new PositionalArgument<>(
                        "argumentBoolean",
                        "Boolean positional argument",
                        Parser.BOOLEAN_PARSER),
                new PositionalArgument<>(
                        "argumentByte",
                        "Byte positional argument",
                        Parser.BYTE_PARSER),
                new PositionalArgument<>(
                        "argumentChar",
                        "Character positional argument",
                        Parser.CHAR_PARSER),
                new PositionalArgument<>(
                        "argumentDouble",
                        "Double positional argument",
                        Parser.DOUBLE_PARSER),
                new PositionalArgument<>(
                        "argumentFloat",
                        "Float positional argument",
                        Parser.FLOAT_PARSER),
                new PositionalArgument<>(
                        "argumentInt",
                        "Integer positional argument",
                        Parser.INT_PARSER),
                new PositionalArgument<>(
                        "argumentList",
                        "List positional argument",
                        new ListParser<>(Parser.STRING_PARSER)),
                new PositionalArgument<>(
                        "argumentLong",
                        "Long positional argument",
                        Parser.LONG_PARSER),
                new PositionalArgument<>(
                        "argumentShort",
                        "Short positional argument",
                        Parser.SHORT_PARSER),
                new PositionalArgument<>(
                        "argumentString",
                        "String positional argument",
                        Parser.STRING_PARSER));
        final List<OptionalArgument<?>> optionalArguments = List.of(
                new OptionalArgument<>(
                        "optionBoolean",
                        'b',
                        "Boolean optional argument",
                        Parser.BOOLEAN_PARSER),
                new OptionalArgument<>(
                        "optionByte",
                        'B',
                        "Byte positional option",
                        Parser.BYTE_PARSER),
                new OptionalArgument<>(
                        "optionChar",
                        'c',
                        "Character positional option",
                        Parser.CHAR_PARSER),
                new OptionalArgument<>(
                        "optionDouble",
                        'd',
                        "Double positional option",
                        Parser.DOUBLE_PARSER),
                new OptionalArgument<>(
                        "optionFloat",
                        'f',
                        "Float positional option",
                        Parser.FLOAT_PARSER),
                new OptionalArgument<>(
                        "optionInt",
                        'i',
                        "Integer positional option",
                        Parser.INT_PARSER),
                new OptionalArgument<>(
                        "optionList",
                        'l',
                        "List positional option",
                        new ListParser<>(Parser.STRING_PARSER)),
                new OptionalArgument<>(
                        "optionLong",
                        'L',
                        "Long positional option",
                        Parser.LONG_PARSER),
                new OptionalArgument<>(
                        "optionShort",
                        's',
                        "Short positional option",
                        Parser.SHORT_PARSER),
                new OptionalArgument<>(
                        "optionString",
                        'S',
                        "String positional option",
                        Parser.STRING_PARSER));

        command = new Command(
                "test",
                "Command for unit tests",
                "test [options] <argumentBoolean>",
                optionalArguments,
                positionalArguments);
    }

    @Test
    void GIVEN_emptyParameters_WHEN_parse_THEN_throwsIllegalArgumentException() {
        // Given
        final String expected = """
                argumentBoolean is missing but is required
                argumentByte is missing but is required
                argumentChar is missing but is required
                argumentDouble is missing but is required
                argumentFloat is missing but is required
                argumentInt is missing but is required
                argumentList is missing but is required
                argumentLong is missing but is required
                argumentShort is missing but is required
                argumentString is missing but is required""";

        // When
        final CommandResult actual = command.parse(Collections.emptyList());

        // Then
        assertFalse(actual.isSuccess(), "Result should not be success");
        assertEquals(expected, actual.errorMessage(), "Error message should equal expected");
    }
    
    @Test
    void GIVEN_shortOptionsList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "-b true",
                "-B 8",
                "-c C",
                "-d 8.8",
                "-f 8.8",
                "-i 8",
                "-l value1,value2",
                "-L 8",
                "-s 8",
                "-S String",
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");
        
        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertTrue(actual.event().get("optionBoolean"), "optionBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("optionByte"), "optionByte should equal expected");
        assertEquals('C', actual.event().<Character>get("optionChar"), "optionChar should equal expected");
        assertEquals(8.8, actual.event().get("optionDouble"), "optionDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("optionFloat"), "optionFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("optionInt"), "optionInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("optionList"), "optionList should equal expected");
        assertEquals(8L, actual.event().<Long>get("optionLong"), "optionLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("optionShort"), "optionShort should equal expected");
        assertEquals("String", actual.event().get("optionString"), "optionString should equal expected");
    }

    @Test
    void GIVEN_shortOptionsBooleanPresenceList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "-b",
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertTrue(actual.event().get("optionBoolean"), "optionBoolean should equal expected");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_longOptionsWithSpaceList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "--optionBoolean true",
                "--optionByte 8",
                "--optionCharacter C",
                "--optionDouble 8.8",
                "--optionFloat 8.8",
                "--optionInt 8",
                "--optionList value1,value2",
                "--optionLong 8",
                "--optionShort 8",
                "--optionString String",
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertTrue(actual.event().get("optionBoolean"), "optionBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("optionByte"), "optionByte should equal expected");
        assertEquals('C', actual.event().<Character>get("optionChar"), "optionChar should equal expected");
        assertEquals(8.8, actual.event().get("optionDouble"), "optionDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("optionFloat"), "optionFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("optionInt"), "optionInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("optionList"), "optionList should equal expected");
        assertEquals(8L, actual.event().<Long>get("optionLong"), "optionLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("optionShort"), "optionShort should equal expected");
        assertEquals("String", actual.event().get("optionString"), "optionString should equal expected");
    }

    @Test
    void GIVEN_longOptionsWithEqualsList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "--optionBoolean=true",
                "--optionByte=8",
                "--optionCharacter=C",
                "--optionDouble=8.8",
                "--optionFloat=8.8",
                "--optionInt=8",
                "--optionList=value1,value2",
                "--optionLong=8",
                "--optionShort=8",
                "--optionString=String",
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertTrue(actual.event().get("optionBoolean"), "optionBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("optionByte"), "optionByte should equal expected");
        assertEquals('C', actual.event().<Character>get("optionChar"), "optionChar should equal expected");
        assertEquals(8.8, actual.event().get("optionDouble"), "optionDouble should equal expected");
        assertEquals(8.8f, actual.event().<Double>get("optionFloat"), "optionFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("optionInt"), "optionInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("optionList"), "optionList should equal expected");
        assertEquals(8L, actual.event().<Long>get("optionLong"), "optionLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("optionShort"), "optionShort should equal expected");
        assertEquals("String", actual.event().get("optionString"), "optionString should equal expected");
    }

    @Test
    void GIVEN_longOptionsBooleanPresenceList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "--optionBoolean",
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertTrue(actual.event().get("optionBoolean"), "optionBoolean should equal expected");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"t", "true", "y", "yes"})
    void GIVEN_trueList_WHEN_parse_THEN_returnsTrue(final String parameter) {
        // Given
        final List<String> arguments = List.of(
                parameter,
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"f", "false", "n", "no"})
    void GIVEN_falseList_WHEN_parse_THEN_returnsFalse(final String parameter) {
        // Given
        final List<String> arguments = List.of(
                parameter,
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertFalse(actual.event().get("argumentBoolean"), "argumentBoolean should be false");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_doubleQuotedStringList_WHEN_parse_THEN_returnsString() {
        // Given
        final List<String> arguments = List.of(
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "\"abc'` ,=cba\"");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertFalse(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("abc'` ,=cba", actual.event().get("argumentString"), "argumentString should be abc cba");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_singleQuotedStringList_WHEN_parse_THEN_returnsString() {
        // Given
        final List<String> arguments = List.of(
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "'abc\"` ,=cba'");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertFalse(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("abc\"` ,=cba", actual.event().get("argumentString"), "argumentString should be abc cba");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_backtickQuotedStringList_WHEN_parse_THEN_returnsString() {
        // Given
        final List<String> arguments = List.of(
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2",
                "8",
                "8",
                "`abc\"' ,=cba`");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertFalse(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("abc'` ,=cba", actual.event().get("argumentString"), "argumentString should be abc cba");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_listList_WHEN_parse_THEN_returnsString() {
        // Given
        final List<String> arguments = List.of(
                "true",
                "8",
                "C",
                "8.8",
                "8.8",
                "8",
                "value1,value2,\"value'` ,=3\",'value\"` ,=4',`value\"' ,=5`",
                "8",
                "8",
                "String");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertFalse(actual.event().get("argumentBoolean"), "argumentBoolean should be false");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(
                List.of("value1", "value2", "value'` ,=3", "value\"` ,=4", "value\"' ,=5"),
                actual.event().get("argumentList"),
                "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_namedArgumentsWithSpacesList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "argumentString String",
                "argumentShort 8",
                "argumentLong 8",
                "argumentList value1,value2",
                "argumentInt 8",
                "argumentFloat 8.8",
                "argumentDouble 8.8",
                "argumentChar C",
                "argumentByte 8",
                "argumentBoolean true");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }

    @Test
    void GIVEN_namedArgumentsWithEqualsList_WHEN_parse_THEN_returnsEvent() {
        // Given
        final List<String> arguments = List.of(
                "argumentString=String",
                "argumentShort=8",
                "argumentLong=8",
                "argumentList=value1,value2",
                "argumentInt=8",
                "argumentFloat=8.8",
                "argumentDouble=8.8",
                "argumentChar=C",
                "argumentByte=8",
                "argumentBoolean=true");

        // When
        final CommandResult actual = command.parse(arguments);

        // Then
        assertTrue(actual.isSuccess(), "Result should be success");
        assertTrue(actual.event().get("argumentBoolean"), "argumentBoolean should equal expected");
        assertEquals((byte) 8, actual.event().<Byte>get("argumentByte"), "argumentByte should equal expected");
        assertEquals('C', actual.event().<Character>get("argumentChar"), "argumentChar should equal expected");
        assertEquals(8.8, actual.event().get("argumentDouble"), "argumentDouble should equal expected");
        assertEquals(8.8f, actual.event().<Float>get("argumentFloat"), "argumentFloat should equal expected");
        assertEquals(8, actual.event().<Integer>get("argumentInt"), "argumentInt should equal expected");
        assertEquals(List.of("value1", "value2"), actual.event().get("argumentList"), "argumentList should equal expected");
        assertEquals(8L, actual.event().<Long>get("argumentLong"), "argumentLong should equal expected");
        assertEquals((short) 8, actual.event().<Short>get("argumentShort"), "argumentShort should equal expected");
        assertEquals("String", actual.event().get("argumentString"), "argumentString should equal expected");
        assertNull(actual.event().get("optionBoolean"), "optionBoolean should be null");
        assertNull(actual.event().get("optionByte"), "optionByte should be null");
        assertNull(actual.event().get("optionChar"), "optionChar should be null");
        assertNull(actual.event().get("optionDouble"), "optionDouble should be null");
        assertNull(actual.event().get("optionFloat"), "optionFloat should be null");
        assertNull(actual.event().get("optionInt"), "optionInt should be null");
        assertNull(actual.event().get("optionList"), "optionList should be null");
        assertNull(actual.event().get("optionLong"), "optionLong should be null");
        assertNull(actual.event().get("optionShort"), "optionShort should be null");
        assertNull(actual.event().get("optionString"), "optionString should be null");
    }
}
