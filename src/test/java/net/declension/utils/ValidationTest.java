package net.declension.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static net.declension.utils.Validation.requireNonEmptyParam;
import static net.declension.utils.Validation.requireNonNullParam;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidationTest {

    public static final String VARIABLE_NAME = "value";

    @Before
    public void setUp() {

    }

    @Test
    public void requireNonEmptyParamShouldThrowForNull() {
        assertThatThrownBy(() -> requireNonEmptyParam(null, VARIABLE_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null")
                .hasMessageContaining(VARIABLE_NAME);
    }

    public void requireNonEmptyParamShouldThrowForEmptySet() {
        assertThatThrownBy(() -> requireNonEmptyParam(new HashSet<String>(), VARIABLE_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(VARIABLE_NAME)
                .hasMessageContaining("empty");
    }

    @Test
    public void requireNonNullParamShouldThrowForNull() {
        assertThatThrownBy(() -> requireNonNullParam(null, VARIABLE_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null")
                .hasMessageContaining(VARIABLE_NAME);
    }
}