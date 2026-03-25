package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
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

        assert this.keywords.stream().allMatch(keyword -> !keyword.isBlank());
    }

    /**
     * Returns true if the person's name contains at least one word that starts
     * with any of the stored keywords. Matching is case-insensitive.
     */
    @Override
    public boolean test(Person person) {
        requireNonNull(person);

        String[] nameWords = person.getName().fullName.toLowerCase().split("\\s+");

        return keywords.stream()
                .anyMatch(keyword ->
                        Arrays.stream(nameWords)
                                .anyMatch(nameWord -> nameWord.startsWith(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

