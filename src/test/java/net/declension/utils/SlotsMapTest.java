package net.declension.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SlotsMapTest {

    private static final List<Integer> TEST_KEYS = asList(1, 3, 5);
    private SlotsMap<Integer, String> slots;

    @Before
    public void setUp() {
        slots = new SlotsMap<>(TEST_KEYS);
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
    public void getShouldReturnNullForMissing() {
        assertThat(slots.get(1)).isNull();
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
    public void putAllShould() {
        assertThatThrownBy(() -> slots.putAll(new HashMap<>()))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}