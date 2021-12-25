package org.jabref.gui.importer;

import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.preferences.PreferencesService;
import com.github.sarxos.webcam.*;

import javax.swing.*;

import static org.jabref.gui.actions.ActionHelper.needsDatabase;

public class NewEntryScan extends SimpleCommand {

    private final JabRefFrame jabRefFrame;
    private final DialogService dialogService;
    private final PreferencesService preferences;

    public NewEntryScan(JabRefFrame jabRefFrame, DialogService dialogService, PreferencesService preferences, StateManager stateManager){
        this.jabRefFrame=jabRefFrame;
        this.dialogService= dialogService;
        this.preferences=preferences;

        this.executable.bind(needsDatabase(stateManager));
    }

    @Override
    public void execute() {
        new webcamTest();
    }
}
