package net.declension.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SlotsMapTest {

    private static final List<Integer> TEST_KEYS = asList(3, 1, 5);
    public static final String TEST_DEFAULT = "banana";
    private SlotsMap<Integer, String> slots;

    @Before
    public void setUp() {
        slots = new SlotsMap<>(TEST_KEYS, () -> TEST_DEFAULT);
    }

    @Test
    public void isDoneShouldFailBeforeFilled() {
        assertThat(slots.isDone()).isEqualTo(false);
    }

    @Test
    public void isDoneShouldWork() {
        fillSlotsCorrectly();
        assertThat(slots.isDone()).isEqualTo(true);
    }

    private void fillSlotsCorrectly() {
        slots.put(1, "one");
        slots.put(3, "three");
        slots.put(5, "five");
    }

    @Test
    public void sizeShouldReflectUnderlying() {
        assertThat(slots).hasSize(0);
        fillSlotsCorrectly();
        assertThat(slots).hasSize(3);
    }

    @Test
    public void isEmptyShould() {
        assertThat(slots).isEmpty();
        fillSlotsCorrectly();
        assertThat(slots).isNotEmpty();
    }

    @Test
    public void defaultingShouldWork() {
        assertThat(slots.get(3)).isEqualTo(TEST_DEFAULT);
        assertThat(slots).isEmpty();
    }

    @Test
    public void containsKeyShouldWork() {
        assertThat(slots).containsKey(3);
        fillSlotsCorrectly();
        assertThat(slots).containsKey(3);
    }

    @Test
    public void containsValueShould() {
        assertThat(slots).doesNotContainValue("three");
        fillSlotsCorrectly();
        assertThat(slots).containsValue("three");
    }

    @Test
    public void getShouldReturnDefaultForMissing() {
        String actual = slots.get(1);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(TEST_DEFAULT);
    }

    @Test
    public void getShouldThrowForInvalidKey() {
        assertThatThrownBy(() -> slots.get(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown key");
    }

    @Test
    public void getShouldWorkNormally() {
        fillSlotsCorrectly();
        assertThat(slots.get(1)).isEqualTo("one");
        assertThat(slots.get(3)).isEqualTo("three");
    }

    @Test
    public void removeShouldThrow() {
        assertThatThrownBy(() -> slots.remove(1))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void putAllShouldPutThemAll() {
        slots.putAll(new HashMap() {{
            put(3, "thirty");
            put(5, "fifty");
            put(1, "ten");
        }});
        assertThat(slots.values()).containsSequence("thirty", "ten", "fifty");
    }

    @Test
    public void keySetShouldBeInSameOrderAsKeys() {
        assertThat(slots.keySet()).hasSize(TEST_KEYS.size());
        assertThat(slots.keySet()).containsExactly(3, 1, 5);
    }

    @Test
    public void entrySetShouldBeInSameOrderAsKeys() {
        assertThat(slots.entrySet()).hasSize(TEST_KEYS.size());
        assertThat(slots.entrySet()).extracting(Map.Entry::getKey).containsExactly(3, 1, 5);
        assertThat(slots.entrySet()).extracting(Map.Entry::getValue)
                .containsExactly(TEST_DEFAULT, TEST_DEFAULT, TEST_DEFAULT);
        fillSlotsCorrectly();
        assertThat(slots.entrySet()).extracting(Map.Entry::getValue).containsExactly("three", "one", "five");
    }

    @Test
    public void valueShouldBeInSameOrderAsKeys() {
        fillSlotsCorrectly();
        assertThat(slots.values()).containsExactly("three", "one", "five");
    }

    @Test
    public void clearShouldClear() {
        fillSlotsCorrectly();
        slots.clear();
        assertThat(slots).isEmpty();
    }
}