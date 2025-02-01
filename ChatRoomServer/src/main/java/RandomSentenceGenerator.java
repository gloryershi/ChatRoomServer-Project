import java.util.*;

/**
 * The {@code RandomSentenceGenerator} class is a utility for generating random insult sentences. It
 * maintains a predefined list of insults and randomly selects one when requested.
 *
 * <p>This class is used in applications that require humorous or sarcastic interactions,
 * such as chat-based systems or games.</p>
 */
public class RandomSentenceGenerator {

  final List<String> insults = Arrays.asList(
      "You look like a botched code merge.",
      "Your logic is as flawed as an infinite loop.",
      "You must have been compiled with errors.",
      "You couldn’t debug your way out of a print statement.",
      "You’re like a deprecated API: outdated and unnecessary.",
      "May a festering platoon of nasty monkeys manicly smite you nine-hundred, ninty-nine times in the sewer you call home .",
      "With the rage of Alah's fist , may a curdled and malignant group of hirsute monkeys and a defective platoon of rabid maggots seek a battleground in your entrails .",
      "You are so asinine that even a neanderthal would not want to hug you .",
      "You nasty , stupid , mung for brains ."
  );

  private final Random random = new Random();

  /**
   * Generates a random insult from the predefined list of insults.
   *
   * @return a randomly selected insult
   */
  public String generateInsult() {
    return insults.get(random.nextInt(insults.size()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RandomSentenceGenerator that = (RandomSentenceGenerator) o;
    return Objects.equals(insults, that.insults) && Objects.equals(random,
        that.random);
  }

  @Override
  public int hashCode() {
    return Objects.hash(insults, random);
  }
}
