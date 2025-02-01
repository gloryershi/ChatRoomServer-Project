import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The {@code IOUtils} class provides utility methods for handling I/O operations, specifically for
 * reading and writing strings as byte arrays over streams.
 *
 * <p>This utility class ensures that strings are encoded and decoded using UTF-8
 * encoding, making it suitable for network-based or binary communication systems.</p>
 */
public class IOUtils {

  /**
   * Writes a string to the specified {@link DataOutputStream} as a length-prefixed byte array.
   *
   * <p>The string is first converted to a UTF-8 byte array. The length of the byte array
   * is written to the stream, followed by the actual bytes of the string.</p>
   *
   * @param out the {@link DataOutputStream} to write the string to
   * @param str the string to be written
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public static void writeStringAsBytes(DataOutputStream out, String str) throws IOException {
    byte[] data = str.getBytes(StandardCharsets.UTF_8);
    out.writeInt(data.length);
    out.write(data);
  }

  /**
   * Reads a length-prefixed string from the specified {@link DataInputStream}.
   *
   * <p>The method first reads an integer representing the length of the byte array,
   * then reads the bytes of the string, and finally decodes the bytes into a UTF-8 string.</p>
   *
   * <p>If the length is negative, an {@link IOException} is thrown.</p>
   *
   * @param in the {@link DataInputStream} to read the string from
   * @return the string read from the stream
   * @throws IOException if an I/O error occurs while reading from the stream, or if the string
   *                     length is invalid
   */
  public static String readBytesAsString(DataInputStream in) throws IOException {
    int length = in.readInt();
    if (length < 0) {
      throw new IOException("Invalid string length: " + length);
    }
    byte[] data = new byte[length];
    in.readFully(data);
    return new String(data, StandardCharsets.UTF_8);
  }
}
