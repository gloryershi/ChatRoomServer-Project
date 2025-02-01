import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomSentenceGeneratorTest {

  @Test
  public void testGenerateInsult() {
    RandomSentenceGenerator rsg = new RandomSentenceGenerator();
    String insult = rsg.generateInsult();
    Assertions.assertNotNull(insult);
    Assertions.assertFalse(insult.isEmpty());
  }

  @Test
  public void testEqualsHashCode() {
    RandomSentenceGenerator r1 = new RandomSentenceGenerator();
    RandomSentenceGenerator r2 = new RandomSentenceGenerator();

    Assertions.assertNotEquals(r1, r2);
    Assertions.assertNotEquals(r1, null);
    Assertions.assertNotEquals(r1, new Object());

    Assertions.assertNotEquals(r1.hashCode(), r2.hashCode());
  }

  @Test
  public void testToString() {
    RandomSentenceGenerator r = new RandomSentenceGenerator();
    String str = r.toString();
    Assertions.assertNotNull(str);
  }
}
