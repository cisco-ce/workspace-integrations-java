package com.cisco.workspaceintegrations.common.xapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.identityHashCode;

/**
 * A key is a structure that is on the form 'audio.input[1].volume'. The key supports segments that can be either pure text, a
 * wildcard, or text with array specification. A wildcard segment is denoted by '*' and the wildcard can not be combined with
 * text or array specification. The array spec is on the form '[]', where the content is either wildcard '*', a specific index
 * or a range with a fixed lower bound and a fixed or unbound upper bound denoted by 'n'. Examples of these are '[*]', '[3]',
 * '[1..3]', and '[1..n]'
 * <p>
 * <p>This class implements a couple of methods to aid in the following tasks:
 * <p>
 * <p>Validate queries filters against schema
 * <ul>
 * <li>Schema is non-wildcard key
 * <li>Query filter is any-type key
 * <li>Query filter is valid if it is overlapping with schema
 * </ul>
 * <p>
 * <p>Validate status keys against filter
 * <ul>
 * <li>Status is an absolute key
 * <li>Filter is any-type key
 * <li>Status key is valid if it is enclosed in filter
 * </ul>
 * <p>
 * <p>Validate status keys against schema
 * <ul>
 * <li>Status is an absolute key
 * <li>Schema is non-wildcard key
 * <li>Status key is valid if it is enclosed in schema
 * </ul>
 * <p>
 * <p>Validate status keys against auth
 * <ul>
 * <li>Status is an absolute key
 * <li>Auth is any-type key
 * <li>Status key is valid if it is enclosed in auth
 * </ul>
 * <p>
 * <p>Validate command key against auth
 * <ul>
 * <li>Command is an absolute key
 * <li>Auth is any-type key
 * <li>Command key is valid if it is enclosed in auth
 * </ul>
 * <p>
 * <p>Validate command key against schema
 * <ul>
 * <li>Command is an absolute key
 * <li>Schema is non-wildcard key
 * <li>Command key is valid if it is enclosed in schema
 * </ul>
 * <p>
 * <p>Validate event key against schema
 * <ul>
 * <li>Event is an absolute key
 * <li>Schema is non-wildcard key
 * <li>Event key is valid if it is enclosed in schema
 * </ul>
 * <p>
 * <p>Validate event key against auth
 * <ul>
 * <li>Event is an absolute key
 * <li>Auth is any-type key
 * <li>Event key is valid if it is enclosed in auth
 * </ul>
 *
 * @see #isEnclosed(Key)
 * @see #isOverlapping(Key)
 */
public final class Key implements Comparable<Key> {

    private static final String WILDCARD_TOKEN = "*";
    private static final String RANGE_INDICATOR_TOKEN = "..";
    private static final char ARRAY_END_TOKEN = ']';
    private static final char ARRAY_START_TOKEN = '[';
    private static final String INFINITY_TOKEN = "n";
    private static final String SEGMENT_SEPARATOR = ".";
    private static final Parser.Node<KeyBuilder, Key> PARSER_NODE_GRAPH = Parser.buildNodes(() -> {
        Parser.Node<KeyBuilder, Key> segmentStart = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> segmentWildcard = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> segmentContinuation = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayStart = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayWildcard = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayLower = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayRangeDot1 = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayRangeDot2 = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayUpper = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayUnbound = new Parser.Node<>();
        Parser.Node<KeyBuilder, Key> arrayEnd = new Parser.Node<>();

        segmentStart
            .on('_', KeyBuilder::segmentAppend, segmentContinuation)
            .on('a', 'z', KeyBuilder::segmentAppend, segmentContinuation)
            .on('A', 'Z', KeyBuilder::segmentAppend, segmentContinuation)
            .on('*', KeyBuilder::segmentAppend, segmentWildcard);
        segmentContinuation
            .on('_', KeyBuilder::segmentAppend, segmentContinuation)
            .on('0', '9', KeyBuilder::segmentAppend, segmentContinuation)
            .on('a', 'z', KeyBuilder::segmentAppend, segmentContinuation)
            .on('A', 'Z', KeyBuilder::segmentAppend, segmentContinuation)
            .on('.', KeyBuilder::addSegment, segmentStart)
            .on('[', KeyBuilder::moveOn, arrayStart)
            .onEnd(KeyBuilder::addSegmentAndBuild);
        segmentWildcard
            .on('.', KeyBuilder::addSegment, segmentStart)
            .onEnd(KeyBuilder::addSegmentAndBuild);
        arrayStart
            .on('*', KeyBuilder::arrayLowerAppend, arrayWildcard)
            .on('0', '9', KeyBuilder::arrayLowerAppend, arrayLower);
        arrayWildcard
            .on(']', KeyBuilder::addSegment, arrayEnd);
        arrayLower
            .on('0', '9', KeyBuilder::arrayLowerAppend, arrayLower)
            .on('.', KeyBuilder::moveOn, arrayRangeDot1)
            .on(']', KeyBuilder::addSegment, arrayEnd);
        arrayRangeDot1
            .on('.', KeyBuilder::moveOn, arrayRangeDot2);
        arrayRangeDot2
            .on('n', KeyBuilder::arrayUpperAppend, arrayUnbound)
            .on('0', '9', KeyBuilder::arrayUpperAppend, arrayUpper);
        arrayUnbound
            .on(']', KeyBuilder::addSegment, arrayEnd);
        arrayUpper
            .on('0', '9', KeyBuilder::arrayUpperAppend, arrayUpper)
            .on(']', KeyBuilder::addSegment, arrayEnd);
        arrayEnd
            .on('.', KeyBuilder::moveOn, segmentStart)
            .onEnd(KeyBuilder::build);

        return segmentStart;
    });

    private final List<Segment> segments;

    private Key(Iterable<Segment> segments) {
        this.segments = ImmutableList.copyOf(checkNotNull(segments));
        checkArgument(!this.segments.isEmpty(), "A key must consist of at least one segment");
    }

    /**
     * Returns index of the specified segment (exact reference) such that:
     * <code>segments.get(index) == segment</code>
     *
     * @param segment segment to search for.
     * @return the index of the first occurrence of the specified segment. Positive number or 0
     * @throws IllegalArgumentException if specified segment is not part of the key.
     */
    public int identityIndexOf(Segment segment) throws IllegalArgumentException {
        int index = 0;
        for (Segment s : segments) {
            if (identityHashCode(s) == identityHashCode(segment)) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException(
            "Segment " + ObjectUtils.identityToString(segment) + " with value '" + segment + "' is not part of key '" + this + "'"
        );
    }

    /**
     * Returns the segment before specified segment. Empty if no segment before.
     *
     * @param segment segment part of key
     * @param key     the key to search in
     * @return list of segments before specified segment. Empty list if no segments before specified segment.
     * @throws IllegalArgumentException if segment is not part of key.
     */
    public static Optional<Segment> segmentBefore(Segment segment, Key key) throws IllegalArgumentException {
        int index = checkNotNull(key).identityIndexOf(segment);
        return index - 1 < 0 ? Optional.empty() : Optional.of(key.segments.get(index - 1));
    }

    /**
     * Answers if this key consists of only absolute segments. An absolute segment is a segment that does not contain
     * a wildcard or an array range.
     *
     * @return <code>true</code> if all segments are absolute
     */
    public boolean isAbsolute() {
        return segments.stream().allMatch(Segment::isAbsolute);
    }

    /**
     * Returns weather or not this key contains any wildcards.
     *
     * @return <code>true</code> if this key contains any wildcards
     */
    public boolean isWildcard() {
        return segments.stream().anyMatch(Segment::isWildcard);
    }

    /**
     * Answers if this key encloses the argument key.
     * <p>
     * <p>To enclose means that the argument key falls within the definition of this key. That is within the array ranges and
     * queries specified in this key. The argument must be a key with no wildcards.
     *
     * @param other the key to check if falls within the specifications of this
     * @return <code>true</code> if enclosed
     */
    public boolean encloses(Key other) {
        return captures(other, false);
    }

    /**
     * Answers if this key is enclosing the argument key.
     * <p>
     * <p>To enclose means that the argument key falls within the definition of this key. That is within the array ranges and
     * queries specified in this key. The argument must be a key with no wildcards.
     *
     * @param key the key to check if falls within the specifications of this
     * @return <code>true</code> if enclosed
     * @deprecated; use {@link #encloses(Key)} instead
     */
    @Deprecated
    public boolean isEnclosed(Key key) {
        return encloses(key);
    }

    /**
     * Answers if the argument key represents is overlapping this key.
     * <p>
     * <p>To overlapping means that the argument key falls within or is overlapping with the definition of this key. Overlapping only
     * applies to array specifications. The argument must be a key with no wildcards.
     *
     * @param key the key to check if falls within or is overlapping with the specifications of this key
     * @return <code>true</code> if argument represents a key that is overlapping with this key
     */
    public boolean isOverlapping(Key key) {
        return captures(key, true);
    }

    public Segment lastSegment() {
        return segments.get(segments.size() - 1);
    }

    /**
     * Helper to get index in array segments. Will return array index of first matching segment name if
     * segment is and array and is absolute.
     *
     * @param arraySegmentName case insensitive name of array segment.
     * @return index in array segment.
     * @throws IllegalArgumentException if no matching segment name or segment is not an absolute array.
     */
    public int arrayIndexOf(String arraySegmentName) throws IllegalArgumentException {
        Segment segment = segments.stream()
                                  .filter(s -> s.value.equalsIgnoreCase(arraySegmentName))
                                  .findFirst()
                                  .orElseThrow(
                                      () -> new IllegalArgumentException(
                                          "Key '" + this + "' doesn't contain a segment named '" + arraySegmentName + "'"
                                      )
                                  );
        return segment.arraySpecification
            .filter(ArraySpecification::isAbsolute)
            .orElseThrow(
                () -> new IllegalArgumentException("Segment '" + segment + "' is not an absolute array. For key: " + this)
            ).absoluteIndex();
    }

    private boolean captures(Key other, boolean includeOverlaps) {
        Iterator<Segment> thisSegments = segments.iterator();
        Iterator<Segment> otherSegments = other.segments.iterator();

        Segment thisSegment = thisSegments.next();
        Segment otherSegment = otherSegments.next();
        while (true) {
            if (thisSegment.isSegmentWildcard()) {
                if (!thisSegments.hasNext()) {
                    return true;
                }
            } else {
                if (!Objects.equals(thisSegment.value().toLowerCase(), otherSegment.value().toLowerCase())) {
                    return false;
                }
                if (thisSegment.isArray() != otherSegment.isArray()) {
                    return false;
                }
                if (thisSegment.isArray() && !thisSegment.array().captures(otherSegment.array(), includeOverlaps)) {
                    return false;
                }
            }
            if (thisSegments.hasNext() != otherSegments.hasNext()) {
                return false;
            }
            if (!thisSegments.hasNext()) {
                return true;
            }
            thisSegment = thisSegments.next();
            otherSegment = otherSegments.next();
        }
    }

    public List<Segment> segments() {
        return segments;
    }

    @JsonValue
    @Override
    public String toString() {
        return Joiner.on(SEGMENT_SEPARATOR).join(segments);
    }

    @Override
    public int hashCode() {
        return segments.stream().mapToInt(Segment::hashCode).sum();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Key) {
            Key other = (Key) obj;
            if (other.segments.size() != segments.size()) {
                return false;
            }
            for (int i = 0; i < segments.size(); i++) {
                if (!Objects.equals(segments.get(i), other.segments.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @JsonCreator
    public static Key parse(String key) {
        return Parser.parse(key, new KeyBuilder(), PARSER_NODE_GRAPH);

    }

    public static Key key(String key) {
        return parse(key);
    }

    public static Key key(Segment... following) {
        return new Key(Arrays.asList(following));
    }

    public static Key key(Iterable<Segment> segments) {
        return new Key(segments);
    }

    @Override
    public int compareTo(Key other) {
        return recursiveCompare(segments().iterator(), other.segments().iterator());
    }

    private static int recursiveCompare(Iterator<Segment> i1, Iterator<Segment> i2) {
        if (i1.hasNext() && i2.hasNext()) {
            Segment s1 = i1.next();
            Segment s2 = i2.next();
            int comparedKeyName = s1.value.compareTo(s2.value);
            if (comparedKeyName != 0) {
                return comparedKeyName;
            }
            if (s1.isArray() && s2.isArray()) {
                return compareArrays(s1.array(), s2.array(), i1, i2);
            } else {
                return s1.isArray() ? 1 : s2.isArray() ? -1 : recursiveCompare(i1, i2);
            }
        }
        return i1.hasNext() ? 1 : i2.hasNext() ? -1 : 0;
    }

    private static int compareArrays(ArraySpecification a1, ArraySpecification a2,
                                     Iterator<Segment> i1, Iterator<Segment> i2) {
        if (a1.isWildcard() || a2.isWildcard()) {
            return a1.isWildcard() && a2.isWildcard() ? recursiveCompare(i1, i2) : a1.isWildcard() ? -1 : 1;
        }
        if (a1.isAbsolute() || a2.isAbsolute()) {
            if (a1.isAbsolute() && a2.isAbsolute()) {
                int comparedIndexes = a1.absoluteIndex() - a2.absoluteIndex();
                return comparedIndexes == 0 ? recursiveCompare(i1, i2) : comparedIndexes;
            }
            return a1.isAbsolute() ? 1 : -1;
        }
        int comparedLowerBounds = compareLowerBounds(a1, a2);
        if (comparedLowerBounds != 0) {
            return comparedLowerBounds;
        }
        int comparedUpperBounds = compareUpperBounds(a1, a2);
        return comparedUpperBounds == 0 ? recursiveCompare(i1, i2) : comparedUpperBounds;
    }

    private static int compareLowerBounds(ArraySpecification a1, ArraySpecification a2) {
        return a1.lowerBound() - a2.lowerBound();
    }

    private static int compareUpperBounds(ArraySpecification a1, ArraySpecification a2) {
        return a1.upperBound.orElse(Integer.MAX_VALUE) - a2.upperBound.orElse(Integer.MAX_VALUE);
    }

    /**
     * Append segment(s) to the end of this key
     *
     * @param segmentValues
     * @return
     */
    public Key append(String... segmentValues) {
        List<Segment> newSegments = new ArrayList<>(segments());
        Stream.of(segmentValues).map(Segment::segment).forEach(newSegments::add);
        return new Key(newSegments);
    }

    /**
     * Will strip off specific segments from the start of the key.
     *
     * @param segmentsToStrip ordered list of segments to strip.
     * @return
     */
    public Key strip(Segment... segmentsToStrip) {
        return strip(Arrays.asList(segmentsToStrip));
    }

    /**
     * Will strip off specific segments from the start of the key.
     *
     * @param segmentsToStrip ordered list of segments to strip.
     * @return
     */
    public Key strip(List<Segment> segmentsToStrip) {
        List<Segment> newSegments = new ArrayList<>();
        int i = 0;
        for (Segment segment : segments) {
            if (i >= segmentsToStrip.size() || !segmentsToStrip.get(i).captures(segment, false)) {
                newSegments.add(segment);
            }
            i++;
        }
        return key(newSegments);
    }

    /**
     * Returns a list of segments before the specified segment
     *
     * @param segment
     * @return
     */
    public List<Segment> segmentsBefore(Segment segment) {
        return segments.subList(0, identityIndexOf(segment));
    }

    public static final class Segment {

        private final String value;
        private final Optional<ArraySpecification> arraySpecification;

        private Segment(String value, Optional<ArraySpecification> arraySpecification) {
            // For reasons of speed this value is not checked for validity. This is done in the parser.
            this.value = checkNotNull(value);
            this.arraySpecification = checkNotNull(arraySpecification);
            if (Objects.equals(value, WILDCARD_TOKEN)) {
                checkArgument(
                    !arraySpecification.isPresent(),
                    "You can not combine a wildcard segment with an array specification"
                );
            }
        }

        public String value() {
            return value;
        }

        public boolean isAbsolute() {
            return !isSegmentWildcard()
                && (!isArray() || array().isAbsolute());
        }

        public boolean isWildcard() {
            return isSegmentWildcard() || isArrayWildcard();
        }

        public boolean isSegmentWildcard() {
            return Objects.equals(value, WILDCARD_TOKEN);
        }

        public boolean isArrayWildcard() {
            return arraySpecification.filter(ArraySpecification::isWildcard).isPresent();
        }

        public boolean isArray() {
            return arraySpecification.isPresent();
        }

        public ArraySpecification array() {
            return arraySpecification.orElseThrow(
                () -> new IllegalStateException("This is not an array. Check 'isArray' before accessing.")
            );
        }

        @Override
        public String toString() {
            return value + arraySpecification.map(ArraySpecification::toString).orElse("");
        }

        @Override
        public int hashCode() {
            return value.toLowerCase().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Segment) {
                Segment other = (Segment) obj;
                return Objects.equals(value.toLowerCase(), other.value.toLowerCase())
                    && Objects.equals(arraySpecification, other.arraySpecification);
            }
            return false;
        }

        public static Segment segment(String value) {
            try {
                Key key = parse(value);
                checkArgument(key.segments().size() == 1, "Segment has to represent a single segment, '" + value + "'");
                return key.segments.iterator().next();
            } catch (Parser.ParseFailedException ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex);
            }
        }

        public static Segment wildcard() {
            return new Segment(WILDCARD_TOKEN, Optional.empty());
        }

        public static Segment segment(String value, ArraySpecification arraySpecification) {
            Segment segment = segment(value);
            checkArgument(!segment.isArray(), "Segment name also has array in its specification.");
            return new Segment(segment.value(), Optional.of(arraySpecification));
        }

        /**
         * Tells if this segment captures other segment. Wildcards allowed for this segment, but not other
         *
         * @param otherSegment
         * @return
         */
        public boolean captures(Segment otherSegment, boolean includeOverlaps) {
            if (otherSegment == null) {
                return false;
            }
            if (isSegmentWildcard()) {
                return true;
            }
            if (value.equals(otherSegment.value)) {
                if (isArray()) {
                    return otherSegment.isArray() && array().captures(otherSegment.array(), includeOverlaps);
                }
                return true;
            }
            return false;
        }
    }

    public static final class ArraySpecification {

        private final OptionalInt lowerBound;
        private final OptionalInt upperBound;

        private ArraySpecification(OptionalInt lowerBound, OptionalInt upperBound) {
            this.lowerBound = checkNotNull(lowerBound);
            this.upperBound = checkNotNull(upperBound);
            lowerBound.ifPresent(
                value -> checkElementIndex(value, Integer.MAX_VALUE)
            );
            upperBound.ifPresent(
                value -> checkElementIndex(value, Integer.MAX_VALUE)
            );
            if (lowerBound.isPresent() && upperBound.isPresent()) {
                checkArgument(
                    upperBound.getAsInt() >= lowerBound.getAsInt(),
                    "Upper bound must be greater or equal to lower bound: " + upperBound.getAsInt() + ">=" + lowerBound.getAsInt()
                );
            }
        }

        public boolean isWildcard() {
            return !lowerBound.isPresent() && !upperBound.isPresent();
        }

        public boolean isAbsolute() {
            return !lowerBound.isPresent() && upperBound.isPresent();
        }

        public boolean isUnboundedRange() {
            return lowerBound.isPresent() && !upperBound.isPresent();
        }

        public boolean isBoundedRange() {
            return lowerBound.isPresent() && upperBound.isPresent();
        }

        public int absoluteIndex() {
            if (isAbsolute()) {
                return upperBound.getAsInt();
            }
            throw new IllegalStateException("Check 'isAbsolute' before use");
        }

        public int lowerBound() {
            if (isBoundedRange() || isUnboundedRange()) {
                return lowerBound.getAsInt();
            }
            throw new IllegalStateException("Check 'isUnboundRange' or 'isBoundRange' before use");
        }

        public int upperBound() {
            if (isBoundedRange()) {
                return upperBound.getAsInt();
            }
            throw new IllegalStateException("Check 'isBoundRange' before use");
        }

        private boolean captures(ArraySpecification other, boolean includeOverlaps) {
            if (isWildcard()) {
                return true;
            }
            if (includeOverlaps) {
                return upperBound(other) >= lowerBound(this)
                    && lowerBound(other) <= upperBound(this);
            }
            return lowerBound(this) <= lowerBound(other)
                && upperBound(this) >= upperBound(other);
        }

        private static int lowerBound(ArraySpecification spec) {
            return spec.isAbsolute() ?
                spec.absoluteIndex() :
                spec.lowerBound();
        }

        private static int upperBound(ArraySpecification spec) {
            return spec.isAbsolute() ?
                spec.absoluteIndex() :
                spec.isUnboundedRange() ?
                    Integer.MAX_VALUE :
                    spec.upperBound();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder().append(ARRAY_START_TOKEN);
            if (isWildcard()) {
                builder.append(WILDCARD_TOKEN);
            } else if (isAbsolute()) {
                builder.append(absoluteIndex());
            } else if (isBoundedRange() || isUnboundedRange()) {
                builder.append(lowerBound());
                builder.append(RANGE_INDICATOR_TOKEN);
                builder.append(isBoundedRange() ? upperBound() : INFINITY_TOKEN);
            }
            return builder.append(ARRAY_END_TOKEN).toString();
        }

        @Override
        public int hashCode() {
            return lowerBound.hashCode() + upperBound.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof ArraySpecification) {
                ArraySpecification other = (ArraySpecification) obj;
                return Objects.equals(lowerBound, other.lowerBound)
                    && Objects.equals(upperBound, other.upperBound);
            }
            return false;
        }

        public static ArraySpecification absoluteIndex(int index) {
            return new ArraySpecification(OptionalInt.empty(), OptionalInt.of(index));
        }

        public static ArraySpecification unboundedRange(int lowerBound) {
            return new ArraySpecification(OptionalInt.of(lowerBound), OptionalInt.empty());
        }

        public static ArraySpecification boundedRange(int lowerBound, int upperBound) {
            return new ArraySpecification(OptionalInt.of(lowerBound), OptionalInt.of(upperBound));
        }

        public static ArraySpecification wildcard() {
            return new ArraySpecification(OptionalInt.empty(), OptionalInt.empty());
        }

    }

    /**
     * A single pass, character iterating, graph specification, no forwards or backwards looking string parser.
     * <p>
     * <p>Speed was regarded as a vital part as the service needs to parse large number of
     * keys. For each request, the schema needs to be parsed, as well as the request/response.
     * If the schema contains about 200 keys, the request in general about 10, the number of
     * endpoints is at about 10,000, and they make in average 1 request per 10 seconds, then we end
     * up having to parse approx 200,000 keys a second across our nodes. Since we have at least two
     * nodes this leaves a node load of approx 100,000 keys a second.
     * <p>
     * <p>Two strategies for parsing keys was tried out. One based on Regex and this one.
     * <p>
     * <p>The regex solution was incomplete (not checking the overall validity of the key as it broke
     * it up into pieces), and as such would be slower than measured in its final form. On my laptop,
     * this incomplete version managed to parse about 180,000 keys a second.
     * <p>
     * <p>This parser was tested in its final form, fully checking the syntax. It managed to parse about
     * 700,000 keys a second. It is therefor estimated that this parser is at least five times quicker
     * and likely seven times than a complete regex parser.
     * <p>
     * <p>This parser also have some other advantages. It gives really good error messages when failing to
     * parse, giving the exact position of the character that was invalid and what characters it expected
     * to find there.
     * <p>
     * <p>Although both have complexity, the complexity of regexp expressions, even though you know regex, is
     * hard to understand and maintain without breaking it up into smaller pieces, which again may even slow
     * it down further. The chance of making a mistake in those expressions was regarded as high.
     * <p>
     * <p>This parser can be hard to read the first times, but when the graph nature is understood, it is really
     * easy to understand and maintain.
     * <p>
     * <p>It is also a general purpose parser, that can easily be used to parse strings into other types.
     */
    static final class Parser {

        private Parser() {
        }

        public static <B, T> Node<B, T> buildNodes(NodesBuilder<B, T> nodes) {
            return nodes.build();
        }

        public static <B, T> T parse(String string, B builder, Node<B, T> rootNode) {
            checkNotNull(string);
            checkNotNull(builder);
            Node<B, T> currentNode = checkNotNull(rootNode, "Root node can not be null.");
            for (int currentIndex = 0; currentIndex < string.length(); currentIndex++) {
                try {
                    currentNode = currentNode.handle(builder, string.charAt(currentIndex));
                } catch (RuntimeException ex) {
                    throw new ParseFailedException(
                        "Failed to parse '" + string + "'. " + "Illegal character '" + string.charAt(currentIndex)
                            + "' at position " + currentIndex + ". " + ex.getMessage(),
                        ex
                    );
                }
            }
            try {
                return currentNode.finalize(builder);
            } catch (RuntimeException ex) {
                throw new ParseFailedException(
                    "Failed to parse '" + string + "'. Unexpected end of string. " + ex.getMessage(),
                    ex
                );
            }
        }

        @FunctionalInterface
        public interface NodesBuilder<B, T> {

            Node<B, T> build();

        }

        @FunctionalInterface
        public interface Action<B> {

            void perform(B builder, char character);

        }

        //TODO Consider naming, finalize has a special meaning in java
        @FunctionalInterface
        public interface Finalizer<B, T> {

            T finalize(B builder);

        }

        public static final class Node<B, T> {

            private final Collection<OnCharacters<B, T>> onEntries = new ArrayList<>();
            private Optional<Finalizer<B, T>> finalizer = Optional.empty();

            public Node<B, T> on(char low, char high, Action<B> action, Node<B, T> continuation) {
                onEntries.add(new OnCharacters<>(low, high, action, continuation));
                return this;
            }

            public Node<B, T> on(char character, Action<B> action, Node<B, T> continuation) {
                return on(character, character, action, continuation);
            }

            public Node<B, T> onEnd(Finalizer<B, T> f) {
                finalizer = Optional.of(f);
                return this;
            }

            private Node<B, T> handle(B builder, char character) {
                for (OnCharacters<B, T> on : onEntries) {
                    if (on.isMatch(character)) {
                        on.action.perform(builder, character);
                        return on.continuation;
                    }
                }
                throw failure();
            }

            private T finalize(B builder) {
                if (finalizer.isPresent()) {
                    return finalizer.get().finalize(builder);
                }
                throw failure();
            }

            private ParseFailedException failure() {
                return new ParseFailedException("Expected to be "
                                                    + onEntries.stream()
                                                               .map(s -> "'" + s + "'")
                                                               .collect(Collectors.joining(" or ")) + ".");
            }

            private static final class OnCharacters<B, F> {

                private final char low;
                private final char high;
                private final Action<B> action;
                private final Node<B, F> continuation;

                OnCharacters(char low, char high, Action<B> action, Node<B, F> continuation) {
                    this.low = checkNotNull(low);
                    this.high = checkNotNull(high);
                    this.action = checkNotNull(action);
                    this.continuation = checkNotNull(continuation);
                }

                boolean isMatch(char character) {
                    return character >= low && character <= high;
                }

                @Override
                public String toString() {
                    if (low == high) {
                        return Character.toString(low);
                    }
                    return low + "-" + high;
                }
            }

        }

        public static final class ParseFailedException extends RuntimeException {

            ParseFailedException(String message) {
                super(message);
            }

            ParseFailedException(String message, Throwable cause) {
                super(message, cause);
            }

        }

    }

    private static final class KeyBuilder {

        private final List<Segment> key = new ArrayList<>();
        private final StringBuilder segment = new StringBuilder(128);
        private final StringBuilder lower = new StringBuilder(5);
        private final StringBuilder upper = new StringBuilder(5);

        void segmentAppend(char character) {
            segment.append(character);
        }

        void arrayLowerAppend(char character) {
            lower.append(character);
        }

        void arrayUpperAppend(char character) {
            upper.append(character);
        }

        void addSegment(char character) {
            boolean hasLowerBound = lower.length() > 0;
            OptionalInt lowerBound = toArrayBound(lower.toString());
            boolean hasUpperBound = upper.length() > 0;
            OptionalInt upperBound = toArrayBound(upper.toString());

            key.add(new Segment(
                segment.toString(),
                hasLowerBound ?
                    Optional.of(
                        hasUpperBound ?
                            new ArraySpecification(lowerBound, upperBound) :
                            new ArraySpecification(OptionalInt.empty(), lowerBound)
                    ) :
                    Optional.empty()
            ));
            segment.delete(0, Integer.MAX_VALUE);
            lower.delete(0, Integer.MAX_VALUE);
            upper.delete(0, Integer.MAX_VALUE);
        }

        void moveOn(char character) {
            // Do nothing with character
        }

        Key addSegmentAndBuild() {
            addSegment((char) 0);
            return build();
        }

        Key build() {
            return new Key(key);
        }

        private static OptionalInt toArrayBound(String value) {
            if (value.isEmpty() || Objects.equals(value, "*") || Objects.equals(value, "n")) {
                return OptionalInt.empty();
            }
            try {
                return OptionalInt.of(Integer.parseInt(value));
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Value '" + value + "' is not a valid 32-bit integer. Too big?", ex);
            }
        }
    }
}
