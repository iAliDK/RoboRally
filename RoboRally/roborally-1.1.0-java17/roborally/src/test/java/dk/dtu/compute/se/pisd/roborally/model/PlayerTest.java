package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    /**
     * TODO: look back at this test and make sure it's testing correctly
     * Method under test: {@link Player#createTemplate()}
     */
    @Test
    void testCreateTemplate1() {
        Board board = new Board(8, 8);

        Player player = new Player(board, "Color", "Name", new GameController(board));
        player.setSpace(new Space(board, 2, 3), true);
        PlayerTemplate actualCreateTemplateResult = player.createTemplate();

        List<CommandCardFieldTemplate> commandCardFieldTemplateList = actualCreateTemplateResult.cards;
        List<CommandCardFieldTemplate> commandCardFieldTemplateList2 = actualCreateTemplateResult.program;

        assertEquals(8, commandCardFieldTemplateList.size());
        assertEquals(5, commandCardFieldTemplateList2.size());

        assertEquals(2, actualCreateTemplateResult.x);
        assertEquals(3, actualCreateTemplateResult.y);

        assertEquals("Color", actualCreateTemplateResult.color);

        assertEquals(Heading.SOUTH, actualCreateTemplateResult.heading);

        assertNull(commandCardFieldTemplateList.get(0).card);
        assertNull(commandCardFieldTemplateList.get(1).card);
        assertNull(commandCardFieldTemplateList.get(7).card);
        assertNull(commandCardFieldTemplateList.get(6).card);
        assertNull(commandCardFieldTemplateList.get(2).card);
        assertNull(commandCardFieldTemplateList.get(5).card);

        assertNull(commandCardFieldTemplateList2.get(2).card);
        assertNull(commandCardFieldTemplateList2.get(3).card);
        assertNull(commandCardFieldTemplateList2.get(1).card);
        assertNull(commandCardFieldTemplateList2.get(4).card);
        assertNull(commandCardFieldTemplateList2.get(0).card);


        assertEquals(8, commandCardFieldTemplateList.size());
        assertEquals(5, commandCardFieldTemplateList2.size());


        for (CommandCardFieldTemplate template : commandCardFieldTemplateList) {
            assertNull(template.card);
        }

        for (CommandCardFieldTemplate template : commandCardFieldTemplateList2) {
            assertNull(template.card);
        }

    }

}

