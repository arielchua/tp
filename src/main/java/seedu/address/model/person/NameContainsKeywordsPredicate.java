package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} contains words starting with any of the given keywords.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * Creates a predicate that matches persons whose name contains words
     * starting with any of the given keywords.
     *
     * @param keywords A non-null list of non-blank keywords.
     */
    public NameContainsKeywordsPredicate(List<String> keywords) {
        requireNonNull(keywords);
        this.keywords = keywords.stream()
                .map(String::toLowerCase)
                .toList();

        assert this.keywords.stream().noneMatch(String::isBlank);
    }
    /**
     * Returns true if the person's name contains at least one word that starts
     * with any of the stored keywords. Matching is case-insensitive.
     */
    @Override
    public boolean test(Person person) {
        requireNonNull(person);

        // Split name into individual words for prefix comparison
        String[] nameWords = person.getName().fullName.toLowerCase().split("\\s+");

        return keywords.stream()
                .anyMatch(keyword -> isKeywordPrefixOfAnyWord(keyword, nameWords));
    }

    /**
     * Returns true if the given keyword is a prefix of any of the words in the given array.
     *
     * @param keyword The keyword to check.
     * @param nameWords The array of words to check against.
     * @return True if the keyword is a prefix of any word, false otherwise.
     */
    private boolean isKeywordPrefixOfAnyWord(String keyword, String[] nameWords) {
        return Arrays.stream(nameWords).anyMatch(word -> word.startsWith(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof NameContainsKeywordsPredicate otherPredicate
                && keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .toString();
    }
}
