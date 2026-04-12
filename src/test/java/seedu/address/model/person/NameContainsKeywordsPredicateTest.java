package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for {@code NameContainsKeywordsPredicate}.
 */
public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // EP: same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // EP: same values -> returns true
        assertEquals(firstPredicate, new NameContainsKeywordsPredicate(firstPredicateKeywordList));

        // EP: different types -> returns false
        assertNotEquals(1, firstPredicate);

        // EP: null -> returns false
        assertNotEquals(null, firstPredicate);

        // EP: different values -> returns false
        assertNotEquals(firstPredicate, secondPredicate);
    }

    @Test
    public void test_exactNameWordMatch_returnsTrue() {
        // EP: Keyword matches a full word exactly
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_prefixMatch_returnsTrue() {
        // EP: Keyword is a valid prefix of the first word
        NameContainsKeywordsPredicate firstWordPred =
                new NameContainsKeywordsPredicate(Collections.singletonList("Ali"));
        assertTrue(firstWordPred.test(new PersonBuilder().withName("Alice Bob").build()));

        // EP: Keyword is a valid prefix of the second word
        NameContainsKeywordsPredicate secondWordPred =
                new NameContainsKeywordsPredicate(Collections.singletonList("Bo"));
        assertTrue(secondWordPred.test(new PersonBuilder().withName("Alice Bob").build()));

        // BV: Single character prefix (Minimum valid prefix length)
        NameContainsKeywordsPredicate singleCharPred =
                new NameContainsKeywordsPredicate(Collections.singletonList("A"));
        assertTrue(singleCharPred.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_caseInsensitivity_returnsTrue() {
        // EP: Keyword and Name have mixed casing
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Arrays.asList("aLI", "bO"));
        assertTrue(predicate.test(new PersonBuilder().withName("ALICE BOB").build()));
    }

    @Test
    public void test_nonMatching_returnsFalse() {
        // EP: No keywords match any word prefix
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // EP: Keyword is a substring but NOT a prefix
        assertFalse(new NameContainsKeywordsPredicate(List.of("lic"))
                .test(new PersonBuilder().withName("Alice").build()));

        // BV: Keyword is exactly one character longer than the word
        assertFalse(new NameContainsKeywordsPredicate(List.of("Alicee"))
                .test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void test_multipleWordsInName_oneMatches() {
        // EP: Multiple words in name, only the last one matches
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Sov"));
        assertTrue(predicate.test(new PersonBuilder().withName("Isha Sovasaria").build()));
    }

    @Test
    public void test_emptyKeywords_returnsFalse() {
        // EP: Empty list of keywords (should not match anything)
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void test_nameWithExtraWhitespaces_returnsTrue() {
        // EP: Name contains leading/trailing/multiple internal spaces
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("  Alice   Bob  ").build()));
    }

    @Test
    public void test_keywordMatchesMiddleOfName_returnsFalse() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("lice"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }
}
