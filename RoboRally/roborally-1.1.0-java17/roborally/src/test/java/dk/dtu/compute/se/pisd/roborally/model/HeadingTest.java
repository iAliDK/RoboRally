package dk.dtu.compute.se.pisd.roborally.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HeadingTest {
    /**
     * Method under test: {@link Heading#getOpposite()}
     */
    @Test
    void testGetOpposite() {
        assertEquals(Heading.NORTH, Heading.SOUTH.getOpposite());
        assertEquals(Heading.SOUTH, Heading.NORTH.getOpposite());
        assertEquals(Heading.WEST, Heading.EAST.getOpposite());
        assertEquals(Heading.EAST, Heading.WEST.getOpposite());
    }

    /**
     * Method under test: {@link Heading#next()}
     */
    @Test
    void testNext() {
        assertEquals(Heading.WEST, Heading.SOUTH.next());
        assertEquals(Heading.NORTH, Heading.WEST.next());
        assertEquals(Heading.EAST, Heading.NORTH.next());
        assertEquals(Heading.SOUTH, Heading.EAST.next());
    }

    /**
     * Method under test: {@link Heading#prev()}
     */
    @Test
    void testPrev() {
        assertEquals(Heading.EAST, Heading.SOUTH.prev());
        assertEquals(Heading.SOUTH, Heading.WEST.prev());
        assertEquals(Heading.WEST, Heading.NORTH.prev());
        assertEquals(Heading.NORTH, Heading.EAST.prev());
    }
}

