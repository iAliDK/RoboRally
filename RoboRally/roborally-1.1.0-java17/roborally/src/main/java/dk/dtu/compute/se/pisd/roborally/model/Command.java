/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the different commands that can be given to a
 * robot.
 * <p>
 *     The class is implemented as an enum, which is a special kind of
 *     class in Java. The enum has a fixed set of instances, which are
 *     defined in the beginning of the class. Each instance has a
 *     display name, which is used to display the command in the GUI.
 *     The enum also has a method {@link #isInteractive()}, which
 *     returns true, if the command is interactive, i.e., if the
 *     command has options, which can be chosen by the user.
 *     Interactive commands have a method {@link #getOptions()}, which
 *     returns the list of options.
 *     <p>
 *         The enum also has a constructor, which can be used to
 *         define interactive commands. The constructor takes a
 *         display name and a list of options as parameters. The
 *         options are defined as a list of commands, which are
 *         defined in the beginning of the class.
 *         <p>
 *             The enum also has a method {@link #getOptions()}, which
 *             returns the list of options.
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    FORWARD("Fwd"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Fwd"),
    FAST_FAST_FORWARD("Move 3 Fwd"),
    U_TURN("U-turn"),
    BACK_UP("Back up"),

    // XXX Assignment P3
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);
    /**
     * The display name of the command.
     * <p>
     *     This is used to display the command in the GUI.
     */

    final public String displayName;

    // XXX Assignment P3
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    //
    // replaced by the code below:

    final private List<Command> options;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * Returns true, if the command is interactive, i.e., if the
     * command has options, which can be chosen by the user.
     * <p>
     *      Interactive commands have a method {@link #getOptions()},
     *      which returns the list of options.
     *      <p>
     *          The method returns true for all commands, which have
     *          options, and false for all other commands.
     *          <p>
     *              The method is used in the GUI to decide, if the
     *              command should be displayed as a button or as a
     *              menu item.
     *              <p>
     *                  @return true, if the command is interactive
     *
     * @return
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * Returns the list of options for an interactive command.
     * <p>
     *     The method returns null, if the command is not interactive.
     *     <p>
     *         The method is used in the GUI to display the options of
     *         an interactive command.
     *         <p>
     *             @return the list of options for an interactive command
     *             or null, if the command is not interactive
     *             <p>
     * @return
     */
    public List<Command> getOptions() {
        return options;
    }
}
