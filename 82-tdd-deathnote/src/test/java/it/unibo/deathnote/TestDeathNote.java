package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNodeImplementation;

class TestDeathNote {

    private final String LAWLIET = "L Lawliet";
    private final String HIGUCHI = "Kyosuke Higuchi";
    private final long DEATH_CAUSE_DELAY = 100;
    private final long DETAILS_DELAY = DEATH_CAUSE_DELAY + 6000;
    private DeathNote dn;

    @BeforeEach
    void setUp() {
        dn = new DeathNodeImplementation();
    }

    /*
     * Tests that no rule is null or blank
     */
    @Test
    void testValidRules() {
        for (final String rule : DeathNote.RULES) {
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    /*
     * Tests the rules number validity
     */
    @Test
    void testLegalRules() {
        for (int rule = 1; rule <= DeathNote.RULES.size(); rule++) {
            assertEquals(DeathNote.RULES.get(rule - 1), dn.getRule(rule));
        }

        for (final int rule : List.of(0, DeathNote.RULES.size() + 1)) {
            try {
                dn.getRule(rule);
                fail("Test failed: Read number out of boundaries");
            } catch (IllegalArgumentException e) {
                checkErrorMessageFormat(e);
            }
        }
    }

    /*
     * Test the death node name writing function
     */
    @Test
    void testHumanDeath() {
        assertFalse(dn.isNameWritten(LAWLIET));
        dn.writeName(LAWLIET);
        assertTrue(dn.isNameWritten(LAWLIET));
        assertFalse(dn.isNameWritten(HIGUCHI));
        assertFalse(dn.isNameWritten(""));
        try {
            dn.writeName(null);
            fail("Was able to write a null name on the deathnote");
        } catch (NullPointerException e) {
            assertFalse(dn.isNameWritten(null));
            checkErrorMessageFormat(e);
        }
    }

    /*
     * Tests that the cause of the death can be written within a
     * predefined time
     */
    @Test
    void testDeathCause() throws InterruptedException {
        assertFalse(dn.isNameWritten(LAWLIET));
        try {
            dn.writeDeathCause("heart attack");
            fail("Death cause has been written without a name");
        } catch (IllegalStateException e) {
            checkErrorMessageFormat(e);
        }

        dn.writeName(LAWLIET);
        assertTrue(dn.isNameWritten(LAWLIET));
        assertTrue(dn.writeDeathCause("heart attack"));
        assertEquals("heart attack", dn.getDeathCause(LAWLIET));

        dn.writeName(HIGUCHI);
        assertTrue(dn.isNameWritten(HIGUCHI));
        assertTrue(dn.writeDeathCause("karting accident"));
        assertEquals("karting accident", dn.getDeathCause(HIGUCHI));
        Thread.sleep(DEATH_CAUSE_DELAY);
        dn.writeDeathCause("hearth attack");
        assertEquals("karting accident", dn.getDeathCause(HIGUCHI));
    }

    /*
     * Tests that the details can be written within a predefined time
     */
    @Test
    void testDeathDetails() throws InterruptedException {
        assertFalse(dn.isNameWritten(LAWLIET));
        try {
            dn.writeDetails("bled to death");
            fail("Was able to write death details without a name");
        } catch (IllegalStateException e) {
            checkErrorMessageFormat(e);
        }

        dn.writeName(LAWLIET);
        assertTrue(dn.isNameWritten(LAWLIET));
        assertEquals("", dn.getDeathDetails(LAWLIET));
        assertTrue(dn.writeDetails("ran for too long"));
        assertEquals("ran for too long", dn.getDeathDetails(LAWLIET));

        dn.writeName(HIGUCHI);
        assertTrue(dn.isNameWritten(HIGUCHI));
        assertEquals("", dn.getDeathDetails(HIGUCHI));
        Thread.sleep(DETAILS_DELAY);
        assertFalse(dn.writeDetails("bled to death"));
        assertEquals("", dn.getDeathDetails(HIGUCHI));
    }

    private void checkErrorMessageFormat(RuntimeException e) {
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
    }
}